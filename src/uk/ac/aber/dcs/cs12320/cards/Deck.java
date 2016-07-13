package uk.ac.aber.dcs.cs12320.cards;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * The deck class for the cards to be dealt
 * 
 * @author Bluer
 *
 */
public class Deck extends SetOfCards {
    boolean shuffled = false;
    
    /**
     * Creates the deck by getting the images for the cards from the cards.txt
     * file within the user specified theme folder, then stores them into the
     * deckTheme arraylist. The deck is then made by looping through enums for
     * the suits and ranks, checking the image associated with the card, and
     * creating a new card from the results, to then be added to the deck's
     * cards arraylist.
     * 
     * @throws FileNotFoundException
     * @throws IOException
     */
    public Deck(String deckFolder) throws FileNotFoundException, IOException {
        ArrayList<String> deckTheme = new ArrayList<String>();
        try (FileReader fr = new FileReader("cards.txt");
                BufferedReader br = new BufferedReader(fr);
                Scanner infile = new Scanner(br)) {
            for (int i = 0; i <= 50; i++) {

                deckTheme.add(infile.nextLine());
            }
            deckTheme.add(infile.next());
        } catch (FileNotFoundException e) {
            System.err.println("The file: " + " does not exist. Assuming first use and an empty file."
                    + " If this is not the first use then have you accidentally deleted the file?");
        } catch (IOException e) {
            System.err.println("An unexpected error occurred when trying to open the file in folder " + deckFolder);
            System.err.println(e.getMessage());
        }
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                Card card = new Card(suit.getSuit(), rank.getRank(), SetOfCards.imageCheck(suit.getSuit(),
                        rank.getRank(), deckTheme));
                this.add(card);
            }
        }
    }  

    /**
     * Deals a card from the deck's arraylist 
     * to the cardsInPlay in the game in a new pile
     */
    public boolean dealCard() {
        if (cards.size() > 0){
        Card cardToDeal = cards.get(cards.size() - 1);
        makeNewPile(cardToDeal);
        cards.remove(cardToDeal);
        TestFrame.cardsInPlay++;
        return true;
        } else {
            System.out.println("Sorry, all of the cards have been dealt from the deck");
        }
        return false;
    }

    /**
     * Adds a new pile for displaying with a card
     * 
     * @param card
     */
    public void makeNewPile(Card card) {
        TestFrame.piles.add(new Pile(card));
    }

    /**
     * Used to shuffle the cards arraylist in deck,
     * and if the deck has already been shuffled then
     * it won't shuffle it again
     */
    public void shuffle() {
            Collections.shuffle(cards);
    }

    /**
     * Adds a new cards to the deck
     */
    public void add(Card card) {
        cards.add(card);
    }

    /**
     * Used to display the contents of the deck
     */
    public String toString() {
        int cardNum = 1;
        StringBuilder str = new StringBuilder("Deck contents: \n");
        for (Card card : this.cards) {
            str.append("Card " + cardNum + ": " + card.getCard() + "\n");
            cardNum++;
        }
        return str.toString();
    }

}
