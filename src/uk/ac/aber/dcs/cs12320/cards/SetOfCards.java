package uk.ac.aber.dcs.cs12320.cards;

import java.util.ArrayList;

/**
 * Superclass for Pile and Deck
 * 
 * @author Bluer
 *
 */
public class SetOfCards implements Comparable<Card> {
    public ArrayList<Card> cards;

    /**
     * Allows comparing cards
     */
    @Override
    public int compareTo(Card compareCard) {
        if (compareCard.equals(this)) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Compares the ranks of two cards
     * 
     * @param compareCard
     * @param set
     * @return
     */
    public int compareToRank(Card compareCard, ArrayList<Card> set) {
        for (Card card : set) {
            if (card.getRank() == compareCard.getRank()) {
                return 1;
            }
        }
        return 0;
    }

    /**
     * Compares the suits of two cards
     * 
     * @param compareCard
     * @param set
     * @return
     */
    public int compareToSuit(Card compareCard, ArrayList<Card> set) {
        for (Card card : set) {
            if (card.getSuit() == compareCard.getSuit()) {
                return 1;
            }
        }
        return 0;
    }

    /**
     * Provides an arraylist of cards for the subclasses
     */
    public SetOfCards() {
        cards = new ArrayList<Card>();
    }

    /**
     * Checks the image that is to be associated with the provided card information
     * 
     * @param cardSuit
     * @param cardRank
     * @param deckTheme
     * @return
     */
    public static String imageCheck(Suit cardSuit, Rank cardRank, ArrayList<String> deckTheme) {
        String result = null;
        String image = cardRank.getValueAsString() + cardSuit.getName() + ".gif";
        for (String i : deckTheme) {
            if (image.equals(i)) {
                result = i;
            }
        }
        return result;
    }

    /**
     * Returns a rank based on the provided int value
     * 
     * @param num
     * @return
     */
    public Rank numAsRank(int num) {
        Rank result = null;
        for (Rank rank : Rank.values()) {
            if (rank.getValue() == num) {
                result = rank;
            }
        }
        return result;
    }

    /**
     * Returns a suit based on the provided int value
     * 
     * @param num
     * @return
     */
    public Suit numAsSuit(int num) {
        Suit result = null;
        for (Suit suit : Suit.values()) {
            if (suit.getNameAsNum() == num) {
                result = suit;
            }
        }
        return result;
    }
}
