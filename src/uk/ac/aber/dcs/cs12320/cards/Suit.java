package uk.ac.aber.dcs.cs12320.cards;

/**
 * An enum for the suit for the Card class
 * 
 * @author Bluer
 *
 */
public enum Suit {
    Spades("s"), Hearts("h"), Diamonds("d"), Clubs("c");

    private String name;

    /**
     * The details of the suit are the string for the name
     * 
     * @param s
     */
    private Suit(String s) {
        name = s;
    }

    /**
     * Gets the value of the suit
     * 
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the name of the suit as a number
     * 
     * @return
     */
    public int getNameAsNum() {
        switch (this.getName()) {
        case "s":
            return 1;
        case "h":
            return 2;
        case "d":
            return 3;
        case "c":
            return 4;
        }
        return 0;
    }

    /**
     * Returns the suit
     * 
     * @return
     */
    public Suit getSuit() {
        return this;
    }
}
