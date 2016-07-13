package uk.ac.aber.dcs.cs12320.cards;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.Scanner;

import uk.ac.aber.dcs.cs12320.cards.gui.TheFrame;

/**
 * Our application class
 * 
 * @author Bluer
 * @Version 1.00 (8th May 2015)
 */
public class TestFrame {
    // Used for "playForMe" to prevent move feedback
    private boolean hideMoveFeedback = false;

    // Used for toggling the display text option
    private boolean boardAsText = false;
    private boolean boardAsFile = false;
    private static boolean shuffleAtBeginning = false;
    private String movesLog = "";
    private int movesMade = 0;
    public static Deck deck;
    private static ArrayList<Score> scores;
    private static TheFrame frame; // For the GUI
    private static Scanner scan;
    private static String deckFolder; // Location of the deck theme to use
    public static int cardsInPlay = 0;
    public static ArrayList<Pile> piles = new ArrayList<Pile>(); // Stores each
                                                                 // pile in play

    /**
     * Sets up the scanner for the frame, and asks the user for the folder where
     * the deck theme is located
     * 
     * @throws FileNotFoundException
     * @throws IOException
     */
    private TestFrame() throws FileNotFoundException, IOException {
        obtainDeckFolder();
    }
    
    private void obtainDeckFolder() {
        scan = new Scanner(System.in);
        do {
            deckFolder = getString("Please enter the theme to use "
                    + "(Currently supported: 'Animals' or 'Classic'): ");
            if (!(deckFolder.equals("Animals") || deckFolder.equals("Classic"))){
                System.out.println("Try again; please choose from the options displayed.");
            }
        } while (!(deckFolder.equals("Animals") || deckFolder.equals("Classic")));
    }

    /**
     * Runs the main menu
     * 
     */
    private void runGameMenu() {
        String response;
        do {
            updateDisplay();
            printGameMenu();
            scan = new Scanner(System.in);
            response = getString("Please choose an option: ").toUpperCase();
            switch (response) {
            case "1":
                printPack();
                break;
            case "2":
                shuffleDeck(false);
                break;
            case "3":
                dealCard();
                break;
            case "4":
                makeMove();
                break;
            case "5":
                amalgamatePiles();
                break;
            case "6":
                playForMe();
                break;
            case "7":
                displayLowestPackSizes();
                break;
            case "A":
                controlTextDisplay();
                break;
            case "Q":
                break;
            default:
                System.out.println("Try again");
            }
        } while (!(response.equals("Q")));
        if (response.equals("Q")) {
            quit();
        }
    }

    /**
     * When the program is ready to end, whether manually quit, or the game ends
     * otherwise, the save method calls the addScore method for a name from the
     * user for their score if the game has properly ended (empty deck). The
     * scores are then sorted with the new score, removing the worst of the
     * scores to keep it the top 10. It then writes the results to scores.txt
     * through saveScores
     */
    private void save() {
        addScore();
        saveScores();
    }

    /**
     * Writes the updated scores to scores.txt
     */
    private void saveScores() {
        try (FileWriter fw = new FileWriter("scores.txt");
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter outfile = new PrintWriter(bw);) {
            outfile.println(scores.size());
            for (int i = 0; i < scores.size(); i++) {
                outfile.println(scores.get(i).getPlayerName());
                outfile.println(scores.get(i).getNumOfPiles());
            }
        } catch (IOException e) {
            System.err.println("Could not write to scores.txt");
        }
    }

    /**
     * Asks the user for a name to associate with their score, adds the score,
     * then removes the previous worst one
     */
    private void addScore() {
        if (deck.cards.size() == 0) {
            String name = getString("Please enter your name for your score on the scoreboard: ");

            if (name != null) {
                scores.add(new Score(name, cardsInPlay));
            }
            sortScores();
            if (scores.size() > 10) {
                for (int i = (scores.size() - 1); i >= 10; i--) {
                    scores.remove(i);
                }
            }
        }
    }

    /**
     * Prints the deck contents
     */
    private void printPack() {
        System.out.println(deck.toString());
    }

    /**
     * Creates and appends to a string to potentially be saved to a file of the
     * moves made
     * 
     * @param lastMoveMade
     */
    private String updateMovesLog(String lastMoveMade) {
        String newLine = System.lineSeparator();
        StringBuilder str = new StringBuilder();
        movesMade++;

        str.append("---------------------------------------" + newLine)
                .append("Total moves made: " + movesMade + newLine).append("Move just made: " + lastMoveMade + newLine)
                .append("Number of piles now in play: " + cardsInPlay + newLine).append("Piles in play: " + newLine);
        for (Pile pile : piles) {
            str.append(pile.getCard().getCard() + newLine);
        }
        movesLog = str.toString();
        return movesLog;
    }

    /**
     * Saves the moves made to 'moveslog.txt'
     */
    private void saveMovesLog() {
        try (FileWriter fw = new FileWriter("lastSavedMoveLog.txt");
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter outfile = new PrintWriter(bw);) {
            outfile.println(movesLog);
        } catch (IOException e) {
            System.err.println("Could not write to moveslog.txt");
        }
    }

    /**
     * Checks whether the user wants the deck to be shuffled
     */
    private static void shuffleDeck(boolean beginningShuffle) {
        if (!(deck.shuffled)) {
            String answer = "";
            do {
                answer = getString("Would you like the deck to the shuffled? (Y/N)").toUpperCase();
                if (!(answer.equals("Y") || answer.equals("N"))) {
                    System.err.println("Please provide a 'Y' or 'N' answer \n");
                }
            } while (!(answer.equals("Y") || answer.equals("N")));
            if (answer.equals("Y")) {
                deck.shuffle();
                deck.shuffled = true;
                if(beginningShuffle){
                    shuffleAtBeginning = true;
                }
            }
        } else {
            System.out.println("The deck is already shuffled.");
        }
    }

    /**
     * Used to deal a new card
     * 
     * @return
     */
    private boolean dealCard() {
        if (deck.dealCard()) {
            if (boardAsFile) {
                movesLog += updateMovesLog("Dealt a card");
            }
            updateDisplay();
            return true;
        }
        return false;
    }

    /**
     * Updates the frame for the current state of play
     */
    private static void updateDisplay() {
        frame.cardDisplay(piles);
    }

    /**
     * Used to determine whether a particular move is legal within the rules.
     * The move is legal is there are either 2 piles between the cards or are
     * adjacent to each other, and are either the same suit or the same rank.
     * 
     * @param positionFrom
     * @param positionTo
     * @param cardLeft
     * @param cardRight
     * @return
     */
    private boolean legalMove(int positionFrom, int positionTo, Card cardLeft, Card cardRight) {
        if (3 == (positionFrom - positionTo) || 3 == (positionTo - positionFrom) || 1 == (positionFrom - positionTo)
                || 1 == (positionTo - positionFrom)) {
            if (cardLeft.getSuit().equals(cardRight.getSuit()) || cardLeft.getRank().equals(cardRight.getRank())) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Helps to reduce the length of the menu, by generalising the "Make a move"
     * option and then asking which method they want to use of the 2 choices.
     */
    private void makeMove() {
        int which;
        do {
            which = getInt("Move last pile onto previous one (1), or move last pile back over 2 piles (2)? (Please type 1 or 2): ");
            if (1 == which) {
                makeMovePrevious();
            } else if (2 == which) {
                makeMove2Piles();
            }
        } while (!((1 == which) || (2 == which)));

    }

    /**
     * Initial method used for playForMe to help any issues with looping
     * iterations, rather than having a loop inside the normal playForMe method
     * 
     * @param movesToMake
     */
    private void playForMe() {
        int howManyTimes;
        howManyTimes = getInt("How many moves would you like to be made?");
        if (0 == howManyTimes) {
            return;
        }
        for (int i = 0; i < howManyTimes; i++) {
            autoPlay();
        }
    }

    /**
     * If it would be within the range of cards in play, try to amalgamate the
     * card next to it
     * 
     * @param cardLeft
     * @param cardRight
     * @param moveMade
     * @return
     */
    private boolean amalgamatePreviousAttempt(int cardLeft, int cardRight, boolean moveMade) {
        if (cardRight <= (piles.size() - 1)) {
            if (legalMove(cardRight, cardLeft, piles.get(cardLeft).getCard(), piles.get(cardRight).getCard())) {
                if (amalgamatePiles(cardRight, cardLeft)) {
                    moveMade = true;
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Tries to amalgamate piles that are 2 piles apart, by checking that 2
     * piles ahead of cardRight's index position value would be a legal move,
     * and if it isn't, then tries the adjacent piles.
     * 
     * @param cardLeft
     * @param cardRight
     * @param moveMade
     * @return
     */
    private boolean amalgamate2PilesAttempt(int cardLeft, int cardRight, boolean moveMade) {
        if ((cardRight + 2) <= (piles.size() - 1)) {
            if (legalMove(cardRight + 2, cardLeft, piles.get(cardLeft).getCard(), piles.get((cardRight + 2)).getCard())) {
                if (amalgamatePiles((cardRight + 2), cardLeft)) {
                    moveMade = true;
                    return true;
                } else if (amalgamatePreviousAttempt(cardLeft, cardRight, moveMade)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Tries the amalgamations if there are at least 4 cards in play, and then
     * tries a normal adjacent attempt if not
     * 
     * @param cardLeft
     * @param cardRight
     * @param moveMade
     * @return
     */
    private boolean amalgamateAttempts(int cardLeft, int cardRight, boolean moveMade) {
        if (cardsInPlay >= 4) {
            do {
                if (amalgamate2PilesAttempt(cardLeft, cardRight, moveMade)) {
                    return true;
                } else if (amalgamatePreviousAttempt(cardLeft, cardRight, moveMade)) {
                    return true;
                }
                cardLeft++;
                cardRight++;
            } while (cardRight <= (piles.size() - 1) && !moveMade);
        } else if (amalgamatePreviousAttempt(cardLeft, cardRight, moveMade)) {
            return true;
        }
        return false;
    }

    /**
     * Checks the piles where there are too few piles for amalgamating
     * 
     * @return
     */
    private boolean tryMakeMoves() {
        if (cardsInPlay >= 2) {
            if (makeMovePrevious()) {
                return true;
            }
            if (cardsInPlay >= 4) {
                if (makeMove2Piles()) {
                    return true;
                }
            }
            if (makeMovePrevious()) {
                return true;
            } else if (dealCard()) {
                return true;
            } else {
                System.out.println("No more possible moves; you have lost your patience.");
                quit();
            }
        }
        return false;
    }

    /**
     * playForMe is called to make at least one move automatically, and is
     * called multiple times if several moves are requested.
     */
    private void autoPlay() {
        // cardLeft and cardRight used for iterating through the piles
        int cardLeft = 0;
        int cardRight = 1;
        boolean moveMade = false;
        hideMoveFeedback = true;

        if (amalgamateAttempts(cardLeft, cardRight, moveMade)) {
            return;
        }

        if (!tryMakeMoves()) {
            if (dealCard()) {
                return;
            } else {
                System.out.println("No more possible moves; you have lost your patience.");
                quit();
            }
        }
    }

    /**
     * Used for when the game is finished, to save the user's score, or at least
     * inform the user of the scores to beat
     */
    private void quit() {
        System.out.println("Your final score was " + cardsInPlay + " piles.");
        if (cardsInPlay < scores.get(9).getNumOfPiles()) {
            if (shuffleAtBeginning) {
                System.out.println("You have a top 10 score!");
                save();
            } else {
                System.out.println("You have a top 10 score! " + "But you didn't shuffle the deck"
                        + " at the beginning of the game");
            }
        }
        if (boardAsFile) {
            saveMovesLog();
        }
        printScores();
        System.exit(0);
    }

    /**
     * Checks if it would be a legal move, swaps the piles with the cards and
     * then removes the unwanted pile.
     * 
     * @return
     */
    private boolean makeMovePrevious() {
        if (cardsInPlay >= 2) {
            if (legalMove(piles.size() - 1, (piles.size() - 2), piles.get(piles.size() - 1).getCard(),
                    piles.get(piles.size() - 2).getCard())) {

                if (boardAsFile) {
                    movesLog += updateMovesLog("Combined the " + piles.get(piles.size() - 1).getCard().getCard()
                            + " with the " + piles.get(piles.size() - 2).getCard().getCard());
                }

                Collections.swap(piles, (piles.size() - 1), (piles.size() - 2));
                removePile((piles.size() - 1));
                return true;
            }
        } else {
            if (!hideMoveFeedback) {
                System.out.println("Illegal move, sorry, please try again");
            }

        }
        return false;
    }

    /**
     * After checking that it's a legal move, it swaps the piles in the piles
     * arraylist, and then remove the one that the card moved from.
     * 
     * @return
     */
    private boolean makeMove2Piles() {
        if (cardsInPlay >= 4) {
            if (legalMove(piles.size() - 1, piles.size() - 4, piles.get(piles.size() - 1).getCard(),
                    piles.get(piles.size() - 4).getCard())) {

                if (boardAsFile) {
                    movesLog += updateMovesLog("Combined the " + piles.get(piles.size() - 1).getCard().getCard()
                            + " with the " + piles.get(piles.size() - 4).getCard().getCard());
                }

                Collections.swap(piles, piles.size() - 1, piles.size() - 4);
                removePile((piles.size() - 1));
                return true;
            }
            } else {
                if (!hideMoveFeedback) {
                    System.out.println("Illegal move, sorry, please try again");
                }
            
        }
        return false;
    }

    /**
     * Takes user input for the pile locations that are being moved from and
     * moved to, then checks the legality of the move, and then swaps the piles
     * and removes the unwanted pile.
     * 
     * @return
     */
    private boolean amalgamatePiles() {
        int movingPile = (getInt("Which pile is moving?") - 1);
        int movingToPile = (getInt("Moving to where?") - 1);

        if (movingPile < cardsInPlay && movingToPile < cardsInPlay){
        if (legalMove(movingPile, movingToPile, piles.get(movingPile).getCard(), piles.get(movingToPile).getCard())) {

            if (boardAsFile) {
                movesLog += updateMovesLog("Combined the " + piles.get(movingPile).getCard().getCard() + " with the "
                        + piles.get(movingToPile).getCard().getCard());
            }

            Collections.swap(piles, movingPile, movingToPile);
            removePile(movingPile);
            return true;
        }
        } else {
            if (!hideMoveFeedback) {
                System.out.println("Illegal move, sorry, please try again");
            }
        }
        return false;
    }

    /**
     * Removes a pile, decrements the value for how many cards are in play, and
     * then updates the display
     * 
     * @param pile
     */
    private void removePile(int pile) {
        piles.remove(pile);
        cardsInPlay--;
        updateDisplay();
    }

    /**
     * Takes provided pile locations (rather than user input), checks how legal
     * the move is, and then swaps with, and removes, the unwanted pile.
     * 
     * @param movingPile
     * @param movingToPile
     * @return
     */
    private boolean amalgamatePiles(int movingPile, int movingToPile) {
        if (legalMove(movingPile, movingToPile, piles.get(movingPile).getCard(), piles.get(movingToPile).getCard())) {

            if (boardAsFile) {
                movesLog += updateMovesLog("Combined the " + piles.get(movingPile).getCard().getCard() + " with the "
                        + piles.get(movingToPile).getCard().getCard());
            }

            Collections.swap(piles, movingPile, movingToPile);
            removePile(movingPile);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Displays the top 10 scores for the user to see, taken from the scores.txt
     * file
     */
    private void displayLowestPackSizes() {
        printScores();
    }

    /**
     * Asks if the user wants to have the cards displayed above the menu, and if
     * they do then it will toggle it appropriately for display.
     */
    private void controlTextDisplay() {
        String response;
        do {
            printTextDisplayMenu();
            scan = new Scanner(System.in);
            response = getString("Please select what text form you would like the state of the game to be printed in: ")
                    .toUpperCase();
            switch (response) {
            case "1":
                boardAsText = true;
                boardAsFile = false;
                break;
            case "2":
                boardAsText = false;
                boardAsFile = true;
                break;
            case "3":
                boardAsText = true;
                boardAsFile = true;
                break;
            case "B":
                break;
            default:
                System.out.println("Try again");
            }
        } while (!((response.equals("B")) || (response.equals("1")) || (response.equals("2")) || response.equals("3")));
    }

    /**
     * Prints the menu for the text display options
     */
    private void printTextDisplayMenu() {
        System.out.println("1 - Print in the console");
        System.out.println("2 - Save in a log file");
        System.out.println("3 - Both print and save");
        System.out.println("B - back");
    }

    /**
     * Prints the game menu for the user, and, depending on the user settings,
     * displays the cards that are in play
     */
    private void printGameMenu() {
        if (true == boardAsText) {
            System.out.println("Cards in play: " + cardsInPlay + "\n");
            printCardsInPlay();
        }
        System.out.println("1 - Print the pack out " + "(this is so you can check that it " + "plays properly)");
        System.out.println("2 - Shuffle");
        System.out.println("3 - Deal a card");
        System.out.println("4 - Make a move");
        System.out.println("5 - Amalgamate piles (by giving " + "their numbers - start with 1)");
        System.out.println("6 - Play for me");
        System.out.println("7 - Display lowest pack " + "sizes");
        System.out.println("A - Control text display");
        System.out.println("Q - Quit");
    }

    /**
     * Prints the high scores
     */
    private void printScores() {
        System.out.println(Score.printScores(scores).toString());
    }

    /**
     * Prints what cards are in play, by looping through piles and printing the
     * card
     */
    private void printCardsInPlay() {
        for (Pile pile : piles) {
            System.out.println(pile.getCard().getCard());
        }
    }

    /**
     * Finds the scores.txt file and takes the names and scores for the game,
     * storing them into a scores class
     */
    private static void readScores() {
        Score.readScores(scores);
        sortScores();
    }

    /**
     * sorts the high scores of the game
     */
    public static void sortScores() {
        Collections.sort(scores);
    }

    /**
     * getString is used for ensuring that the user doesn't insert a blank
     * value, and gets rid of the unnecessary whitespace before and after the
     * string.
     */
    public static String getString(String message) {
        boolean correct = false;
        String result = "";
        do {
            System.out.println(message);
            result = scan.nextLine().trim();
            if (result.isEmpty()) {
                System.err.println("Please input non-blank value");
            } else {
                correct = true;
            }
        } while (!correct);
        return result;
    }

    /**
     * Ensures that the user doesn't use a negative value or non-integer value
     */
    public static int getInt(String message) {
        boolean correct = false;
        int result = 0;
        do {
            System.out.println(message);
            try {
                result = scan.nextInt();
                if (result < 0) {
                    System.out.println("Please provide a non-negative value.");
                } else {
                    scan.nextLine();
                    correct = true;
                }
            } catch (InputMismatchException ime) {
                System.err.println("Please enter an number");
                scan.nextLine();
            }
        } while (!correct);
        return result;
    }

    /**
     * Welcomes the user; creates a TestFrame object; creates the deck; creates
     * a frame for the GUI and provides the folder for the deck images; displays
     * the cards; makes an arraylist for the scores, and then runs the game menu
     * 
     * @param args
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static void startGame() throws FileNotFoundException, IOException {
        System.out.println("**********Welcome to Patience***********");
        TestFrame test = new TestFrame();
        deck = new Deck(deckFolder);
        shuffleDeck(true);
        frame = new TheFrame(deckFolder);
        updateDisplay();
        scores = new ArrayList<Score>();
        readScores();
        test.runGameMenu();
    }

    /**
     * Calls startGame()
     * 
     * @param args
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void main(String args[]) throws FileNotFoundException, IOException {
        startGame();
    }
}
