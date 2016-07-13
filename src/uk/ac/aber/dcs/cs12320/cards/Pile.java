package uk.ac.aber.dcs.cs12320.cards;

/**
 * Pile class for the board, containing cards;
 * is a subclass of SetOfCards
 * 
 * @author Bluer
 *
 */
public class Pile extends SetOfCards {
    /**
     * Constructs a pile with a card, adding it to
     * the supplied arraylist from the superclass
     * 
     * @param card
     */
    public Pile(Card card) {
        cards.add(card);
    }

    /**
     * Returns the card details
     * 
     * @return
     */
    public Card getCard() {
        Card cardToReturn = null;
        for (Card card : cards) {
            cardToReturn = card;
        }
        return cardToReturn;
    }
    
    /**
     * Returns the card information in a string
     * 
     */
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (Card card : cards) {
            str.append(card.getCard());
        }
        return str.toString();
    }
}
