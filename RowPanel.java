//******************************************************************************
// 		RowPanel.java
//									Authors:   Jonathan Murley &
//											   Vicki Trenton
//									Class:	   CS152
//									Assignment:Texas Hold'em Game
//
// Represents a row of cards from a PokerHand
//******************************************************************************

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RowPanel extends JPanel
{
	private final int WIDTH = 600, HEIGHT = 600;
	private final int SPACE = 85;
	private final int DELAY = 3500;		//3.5 seconds
	
	private PokerHand hand;
	private Card[] cards;
	private ImageIcon image, win, tie, loss;
	private boolean showResult;
	private Timer timer;

	//--------------------------------------------------------------------------
	// Constructor: Sets up the panel
	//--------------------------------------------------------------------------
	public RowPanel ()
	{		
		setPreferredSize (new Dimension (WIDTH, HEIGHT/3));
		setOpaque (false);		//Makes panel transparent
		hand = null;
		
		//Creates win-tie-loss images
		win = new ImageIcon ("Playing Cards/cait_sith.gif");
		tie = new ImageIcon ("Playing Cards/lulu.gif");
		loss = new ImageIcon ("Playing Cards/vivi.gif");
		
		//Sets descriptions of images
		win.setDescription ("Cait Sith cheers!");
		tie.setDescription ("Lulu gives you luck");
		loss.setDescription ("Feel the burn of VIVI");
		
		//sets the timer for the win/tie/lose gifs.
		timer = new Timer (DELAY, new ImageListener());
	}
	
	//--------------------------------------------------------------------------
	// Paints the cards in the PokerHand
	//--------------------------------------------------------------------------
	public void showHand (PokerHand newHand)
	{
		hand = newHand;
		repaint();
	}
	
	//--------------------------------------------------------------------------
	// Displays a win image
	//--------------------------------------------------------------------------
	public void displayWin ()
	{
		image = win;
		showResult = true;
		repaint();
	}
	
	//--------------------------------------------------------------------------
	// Displays a tie image
	//--------------------------------------------------------------------------
	public void displayTie ()
	{
		image = tie;
		showResult = true;
		repaint();
	}
	
	//--------------------------------------------------------------------------
	// Displays a loss image
	//--------------------------------------------------------------------------
	public void displayLoss ()
	{
		image = loss;
		showResult = true;
		repaint();
	}
	
	//--------------------------------------------------------------------------
	// Draws the card and paints an image for a win, tie, or loss
	//--------------------------------------------------------------------------
	public void paintComponent (Graphics page)
	{
		super.paintComponent (page);
		
		if (hand != null)
			for (int i = 0; i < hand.getCardCount(); i++)
			{
				Card card = hand.getCard (i);
				card.draw (this, page, i*SPACE, 0);
			}
		
		if (showResult)
		{
			image.paintIcon (this, page, 510, 20);
			timer.start();
		}
	}
	
	//**************************************************************************
	// Allows the result image to be visible for a set period of time
	//**************************************************************************
	private class ImageListener implements ActionListener
	{
		//----------------------------------------------------------------------
		// Removes result image when the timer generates an event
		//----------------------------------------------------------------------
		public void actionPerformed (ActionEvent event)
		{
			showResult = false;	
			timer.stop();
		}
	}
}