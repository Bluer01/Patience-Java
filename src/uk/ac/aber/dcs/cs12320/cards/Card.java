package uk.ac.aber.dcs.cs12320.cards;

/**
 * The card class for the cards in the game
 * 
 * @author Bluer
 *
 */

public class Card {

    private Suit suit;
    private Rank rank;
    private String cardImage;
    private String pileName;

    /**
     * Constructs the card using enums for the suit and rank,
     * and then uses the provided image string for getting 
     * the appropriate image for the card
     * 
     * @param cardSuit
     * @param cardRank
     * @param image
     */
    public Card(Suit cardSuit, Rank cardRank, String image) {
        this.suit = cardSuit;
        this.rank = cardRank;
        this.cardImage = image;
    }

    /**
     * Gets the card's suit
     * 
     * @return
     */
    public Suit getSuit() {
        return suit;
    }
    
    /**
     * Gets the card's rank
     * 
     * @return
     */
    public Rank getRank() {
        return rank;
    }

    /**
     * Gets the card's image
     * 
     * @return
     */
    public String getImage() {
        return cardImage;
    }

    /**
     * Sets the image for the card
     * 
     */
    public void setImage(String image) {
        this.cardImage = image;
    }

    /**
     * Gets the rank and suit of the card as a string
     * 
     * @return
     */
    public String getCard() {
        return rank.toString() + " of " + suit.toString();
    }

    /**
     * Returns a string of all of the relevant information
     * for the card
     */
    public String toString() {
        StringBuilder str = new StringBuilder("This card is the ");
        return str.append(rank + " of " + suit).append(", and its image is called '" + cardImage + "'").toString();
    }

    /**
     * Gets the card's pile information
     * 
     * @return
     */
    public String getPile() {
        return pileName;
    }

    /**
     * Sets the pile that the card is part of
     * 
     * @param pile
     */
    public void setPile(String pile) {
        this.pileName = pile;
    }
}
