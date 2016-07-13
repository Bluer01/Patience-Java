package uk.ac.aber.dcs.cs12320.cards;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Class for the scores, implementing Comparable for 
 * sorting
 * 
 * @author Bluer
 *
 */
public class Score implements Comparable<Score>{
    
    private int score;
    private String playerName;
    
    /**
     * Creates the score with a name and score, which is
     * the number of piles left
     *     
     * @param name
     * @param numOfPiles
     */
    public Score(String name, int numOfPiles) {
        this.score = numOfPiles;
        this.playerName = name;
    }
    
    /**
     * Finds the scores.txt file and takes the names and scores for the game,
     * storing them into a scores class
     */
    public static void readScores(ArrayList<Score> scores) {
        try (FileReader fr = new FileReader("scores.txt");
                BufferedReader br = new BufferedReader(fr);
                Scanner infile = new Scanner(br)) {

            int numOfScores = Integer.parseInt(infile.nextLine());

            for (int i = 0; i < numOfScores; i++) {

                String name = infile.nextLine();
                int score = Integer.parseInt(infile.nextLine());
                scores.add(new Score(name, score));
            }
        }
        catch (FileNotFoundException e) {
            System.err.println("The file: " + " does not exist. Assuming first use and an empty file."
                    + " If this is not the first use then have you accidentally deleted the file?");
        } catch (IOException e) {
            System.err.println("An unexpected error occurred when trying to open the file " + "scores.txt");
            System.err.println(e.getMessage());
        }
    }
    
    /**
     * Returns the score
     * 
     * @return
     */
    public int getNumOfPiles () {
        return score;
    }
    
    /**
     * Sets the score
     * 
     * @param numOfPiles
     */
    public void setScore (int numOfPiles) {
        this.score = numOfPiles;
    }
    
    /**
     * Returns the name associated with the score
     * 
     * @return
     */
    public String getPlayerName() {
        return playerName;
    }
    
    /**
     * Sets the name to be associated with the score
     * 
     * @param name
     */
    public void setPlayerName(String name) {
        this.playerName = name;
    }
    
    /**
     * Prints the top 10 scores from the scores arraylist in a StringBuilder in
     * a loop
     * 
     * @return
     */
    public static String printScores(ArrayList<Score> scores) {
        StringBuilder sb = new StringBuilder();

        System.out.println("\n Top 10 scores (number of piles remaining): ");
        for (int i = 0; i < scores.size(); i++) {
            sb.append(i + 1);
            sb.append(". ");
            sb.append(scores.get(i).toString());
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Used for comparing the scores
     * 
     */
    @Override
    public int compareTo(Score compareScore) {
        return Integer.compare(score, compareScore.score);
    }
    
    /**
     * Displays the information of the score as a string
     */
    public String toString() {
        return this.playerName + ": " + this.score;
    }
}
