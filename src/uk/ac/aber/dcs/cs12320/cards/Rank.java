package uk.ac.aber.dcs.cs12320.cards;

/**
 * An enum for the rank for the Card class
 * 
 * @author Bluer
 *
 */
public enum Rank {
    Ace(1), Two(2), Three(3), Four(4), Five(5), Six(6), Seven(7), Eight(8), Nine(9), Ten(10), Jack(11), Queen(12), King(
            13);

    private int num;

    /**
     * Gives the enum an int value
     * 
     * @param v
     */
    private Rank(int v) {
        num = v;
    }

    /**
     * Gets the value of the rank
     * 
     * @return
     */
    public int getValue() {
        return num;
    }

    /**
     * Returns the value of the rank as a string
     * 
     * @return
     */
    public String getValueAsString() {
        return Integer.toString(this.num);
    }

    /**
     * Returns the rank details
     * 
     * @return
     */
    public Rank getRank() {
        return this;
    }
}