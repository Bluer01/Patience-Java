package uk.ac.aber.dcs.cs12320.cards.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import uk.ac.aber.dcs.cs12320.cards.Card;
import uk.ac.aber.dcs.cs12320.cards.Pile;
import uk.ac.aber.dcs.cs12320.cards.TestFrame;

/**
 * Represents a window on which to draw the cards
 * 
 * @author Lynda Thomas (and Chris Loftus)
 * @version 1.1 (5th March 2015)
 * 
 */
public class TheFrame extends JFrame {

    JScrollBar horizontal;
    public static ThePanel canvas;
    private String themeLocation;

    /**
     * The constructor creates a Frame ready to display the cards
     */
    public TheFrame(String themeFolder) {

        // Calls the constructor in the JFrame superclass passing up the name to
        // display in the title
        super("Becky & Dan's Patience");
        
        themeLocation = themeFolder;


        // When you click on the close window button the window will be closed
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // This has North, East, South, West and Center positions for components
        setLayout(new BorderLayout());
        
        // This is what we will draw on (see the inner class below)
        canvas = new ThePanel();
        
        JScrollPane scrollPane = new JScrollPane(canvas,
                JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        this.setSize(700,350);   
        
        this.add(scrollPane, BorderLayout.CENTER);
        setVisible(true); // Display the window
    }

    /**
     * Displays all cards
     * 
     * @param cards
     *            an arraylist of strings of the form 3h.gif for 3 of hearts
     */
    public void cardDisplay(ArrayList<Pile> cards) {
        ArrayList<String> boardPiles = new ArrayList<String>();
        for (Pile pile : cards) {
            for (Card card : pile.cards) {
                boardPiles.add(card.getImage());
            }
        }
        canvas.cardDisplay(boardPiles);
    }

    /**
     * Call before cardDisplay at end of game (takes away the unused pile)
     */
    public void allDone() {
        canvas.allDone();
    }

    // /////////////////////////////////////////////////

    /*
     * This is an example of an inner class (like Russian dolls) It private so
     * can only be seen by the outer class. It's part of the implementation of
     * TheFrame. Because it extends JPanel we can draw on it
     */
    public class ThePanel extends JPanel {
        ArrayList<String> cards = new ArrayList<String>();
        private Image image;
        private boolean done;

        private ThePanel() {
            setBackground(Color.green.darker());
            done = false;
        }

        private void cardDisplay(ArrayList<String> c) {
            cards = c;
            // Dynamically resizes for the scroll bar, dependent on the cards in play
            this.setPreferredSize(new Dimension(TestFrame.cardsInPlay * 75, 310));
            revalidate(); // Required for the scroll bar to display properly
            repaint();
        }

        private void allDone() {
            done = true;
        }

        /**
         * This is called automatically by Java when it want to draw this panel.
         * So we have to put our drawing command in here.
         * 
         * @param g
         *            Is the graphics object on which we draw.
         */
        @Override
        public void paintComponent(Graphics g) {
            // Always do this. It's giving the JPanel superclass a change to
            // paint its parts before we paint ours. E.g. we don't draw the
            // edge of the window, one of the super-classes does that.
            super.paintComponent(g);
            int x = 20;
            int y = 50;
            // Loop through all the cards get each image in turn
            for (String c : cards) {
                String file = themeLocation + "/" + c;
                image = Toolkit.getDefaultToolkit().getImage(file);
                g.drawImage(image, x, y, 70, 100, this);
                x += 72; 
            }
            if (!done) {
                // Draws the face-down top card of our pack of cards
                String file;
                file = themeLocation + "/" + "b.gif";
                image = Toolkit.getDefaultToolkit().getImage(file);
                // So that the deck only shows when there are cards in it
                if (TestFrame.deck.cards.size() > 0) {
                g.drawImage(image, 100, 152, 70, 100, this);
                }
            }
        }
    }
}
// ThePanel inner class

