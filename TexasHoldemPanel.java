//******************************************************************************
// 	  TexasHoldemPanel.java
//									Authors:   Partner & Victoria Trenton
//									Class:	   CS152
//									Assignment:Texas Hold'em Game
//
// Simulates one player playing a poker hand in the Texas Hold'em game
//******************************************************************************

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TexasHoldemPanel extends JPanel
{
	private DeckOfCards deck;
	private PokerHand player, dealer, house;
	private Card playerCard1, playerCard2, dealerCard1, dealerCard2;
	private Card flopCard1, flopCard2, flopCard3, turnCard, riverCard;
	
	private final int WIDTH = 840, HEIGHT = 600;
	
	private RowPanel topPanel;		//Are all added to centerPanel
	private RowPanel middlePanel;
	private RowPanel bottomPanel;
	
	private JButton dealButton, foldButton, showdownButton, playAgainButton;
	private boolean beginGame, donePreFlop, doneFlop, doneTurn;
	
	private JLabel dealerLabel, playerLabel, resultLabel;
	private String result, win, tie, loss, blank; //Outcomes of game
	private int playerWins, dealerWins, tieGames; //Number of wins, losses, & ties
	private JLabel winLabel, lossLabel, tieLabel; //Displays the score
	
	//--------------------------------------------------------------------------
	// Constructor: Sets up the panel
	//--------------------------------------------------------------------------
	public TexasHoldemPanel ()
	{
		ButtonListener buttonListener = new ButtonListener();
		beginGame = true;
		donePreFlop = doneFlop = doneTurn = false;
		playerWins = tieGames = dealerWins = 0;
		Dimension spacer1 = new Dimension (0, 10);
		Dimension spacer2 = new Dimension (0, 35);
		Dimension spacer3 = new Dimension (0, 130);
		Dimension spacer4 = new Dimension (0, 180);
		win = "   Win:  ";
		tie = "   Tie:  ";
		loss = "   Loss:  ";
		blank = " ";
		
		//Sets up this panel
		Color washyBlue = new Color (133, 184, 214);	//Wishy-washy blue			
		setPreferredSize (new Dimension (WIDTH, HEIGHT));
		setLayout (new BorderLayout());
		
		//Sets up the labels
		dealerLabel = new JLabel ("  Dealer");
		resultLabel = new JLabel (blank);
		winLabel = new JLabel (win + playerWins);
		tieLabel = new JLabel (tie + tieGames);
		lossLabel = new JLabel (loss + dealerWins);
		playerLabel = new JLabel ("    You");
		dealerLabel.setFont (new Font ("Papyrus", Font.BOLD, 22));
		resultLabel.setFont (new Font ("Papyrus", Font.BOLD, 16));
		winLabel.setFont (new Font ("Papyrus", Font.BOLD, 16));
		tieLabel.setFont (new Font ("Papyrus", Font.BOLD, 16));
		lossLabel.setFont (new Font ("Papyrus", Font.BOLD, 16));
		playerLabel.setFont (new Font ("Papyrus", Font.BOLD, 22));
		Color iceBlue = new Color (168, 228, 247);	//Light blue
		dealerLabel.setForeground (iceBlue);		//Colored text
		resultLabel.setForeground (iceBlue);
		winLabel.setForeground (iceBlue);
		tieLabel.setForeground (iceBlue);
		lossLabel.setForeground (iceBlue);
		playerLabel.setForeground (iceBlue);
		
		//Creates the sub-panels
		JPanel westPanel = new JPanel();
		JPanel centerPanel = new JPanel();
		JPanel eastPanel = new JPanel();
		topPanel = new RowPanel();		
		middlePanel = new RowPanel();
		bottomPanel = new RowPanel();
		JPanel buttonPanel = new JPanel();
		
		
		
		//Sets up the buttons on the buttonPanel
		dealButton = new JButton ("Deal");
		foldButton = new JButton ("Fold");		
		showdownButton = new JButton ("Showdown");
		playAgainButton = new JButton ("Play Again");
		
		//Gives the buttons a color
		Color softPeach = new Color (253, 228, 213);	//Light orange
		dealButton.setBackground (softPeach);
		foldButton.setBackground (softPeach);
		showdownButton.setBackground (softPeach);
		playAgainButton.setBackground (softPeach);
		
		//Initially sets the buttons to enabled or disabled
		dealButton.setEnabled (true);
		foldButton.setEnabled (false);
		showdownButton.setEnabled (false);
		playAgainButton.setEnabled (false);
		
		//Sets the key mnemonics for the buttons
		dealButton.setMnemonic ('d');
		foldButton.setMnemonic ('f');
		showdownButton.setMnemonic ('s');
		playAgainButton.setMnemonic ('p');
		
		//Adds the button listener to the buttons
		dealButton.addActionListener (buttonListener);
		foldButton.addActionListener (buttonListener);
		showdownButton.addActionListener (buttonListener);
		playAgainButton.addActionListener (buttonListener);
		
		//Sets up the button panel
		buttonPanel.setPreferredSize (new Dimension (WIDTH/8, HEIGHT*2));
		buttonPanel.setBackground (washyBlue);
		buttonPanel.setLayout (new BoxLayout (buttonPanel, BoxLayout.Y_AXIS));
		buttonPanel.add (Box.createRigidArea (spacer4));
		buttonPanel.add (dealButton);
		buttonPanel.add (Box.createRigidArea (spacer2));
		buttonPanel.add (foldButton);
		buttonPanel.add (Box.createRigidArea (spacer2));
		buttonPanel.add (showdownButton);
		buttonPanel.add (Box.createRigidArea (spacer2));
		buttonPanel.add (playAgainButton);
		eastPanel.add (buttonPanel);	
		
		
		
		//Sets up the west, center, and east panels
		westPanel.setPreferredSize (new Dimension (WIDTH/8, HEIGHT));
		westPanel.setBackground (Color.BLACK);
		westPanel.setLayout (new BoxLayout (westPanel, BoxLayout.Y_AXIS));
		westPanel.add (Box.createRigidArea (spacer2));
		westPanel.add (dealerLabel);
		westPanel.add (Box.createRigidArea (spacer3));
		westPanel.add (resultLabel);
		westPanel.add (Box.createRigidArea (spacer1));
		westPanel.add (winLabel);
		westPanel.add (tieLabel);
		westPanel.add (lossLabel);
		westPanel.add (Box.createRigidArea (spacer3));
		westPanel.add (playerLabel);
		Color vibrantOrange = new Color (255, 174, 64);
		centerPanel.setBackground (vibrantOrange);	//Deep orange
		centerPanel.add (topPanel);
		centerPanel.add (middlePanel);
		centerPanel.add (bottomPanel);
		eastPanel.setBackground (Color.BLACK);
		
		//Adds sub-panels to the main panel
		add (westPanel, BorderLayout.WEST);
		add (centerPanel, BorderLayout.CENTER);
		add (eastPanel, BorderLayout.EAST);
	}
	
	//--------------------------------------------------------------------------
	// Creates and shuffles a deck of cards.
	// Deals 2 cards each to the player and dealer
	//--------------------------------------------------------------------------
	public void preFlop ()
	{
		deck = new DeckOfCards();
		player = new PokerHand();
		dealer = new PokerHand();
		house = new PokerHand();
		
		//Creates and shuffles a deck of cards
		deck.create();
		deck.shuffle();
		
		//Deals 2 cards to the dealer
		dealerCard1 = deck.deal();
		dealerCard2 = deck.deal();
		dealer.addCard (dealerCard1);
		dealer.addCard (dealerCard2);
		dealer.flipFacedown();				//Turns all cards facedown
		topPanel.showHand (dealer);
		
		//Deals 2 cards to the player
		playerCard1 = deck.deal();		
		playerCard2 = deck.deal();		
		player.addCard (playerCard1);
		player.addCard (playerCard2);
		bottomPanel.showHand (player);
		
		foldButton.setEnabled (true);
	}
	
	//--------------------------------------------------------------------------
	// Deals three cards to the house and shows them
	//--------------------------------------------------------------------------
	public void flop ()
	{
		flopCard1 = deck.deal();
		flopCard2 = deck.deal();
		flopCard3 = deck.deal();
		
		house.addCard (flopCard1);
		house.addCard (flopCard2);
		house.addCard (flopCard3);
		middlePanel.showHand (house);
	}
	
	//--------------------------------------------------------------------------
	// Buries a card and deals one "turn" card to the house
	//--------------------------------------------------------------------------
	public void turn ()
	{
		deck.bury();
		turnCard = deck.deal();
		house.addCard (turnCard);
	}
	
	//--------------------------------------------------------------------------
	// Buries a card and deals one "river" card to the house
	//--------------------------------------------------------------------------
	public void river ()
	{
		deck.bury();
		riverCard = deck.deal();
		house.addCard (riverCard);
		
		dealButton.setEnabled (false);
		showdownButton.setEnabled (true);
	}
	
	//--------------------------------------------------------------------------
	// Reveals the dealer's hand. Evaluates the hands and determines the winner
	//--------------------------------------------------------------------------
	public void showdown ()
	{		
		dealer.addCard (flopCard1);
		dealer.addCard (flopCard2);
		dealer.addCard (flopCard3);
		dealer.addCard (turnCard);
		dealer.addCard (riverCard);
		dealer.flipFaceup();				//Turns all cards faceup
		
		player.addCard (flopCard1);
		player.addCard (flopCard2);
		player.addCard (flopCard3);
		player.addCard (turnCard);
		player.addCard (riverCard);
		
		//Increases the number of wins and updates the label		
		if (dealer.evaluate() < player.evaluate())
		{
			playerWins++;
			winLabel.setText (win + playerWins);
			result = "  You win!";
			middlePanel.displayWin();
		}
		
		//Increases the number of ties and updates the label	
		else if (dealer.evaluate() == player.evaluate())
		{
			tieGames++;
			tieLabel.setText (tie + tieGames);
			result = "     Tie";
			middlePanel.displayTie();
		}
		
		//Increases the number of losses and updates the label	
		else
		{
			dealerWins++;
			lossLabel.setText (loss + dealerWins);
			result = "  You lose";
			middlePanel.displayLoss();
		}
		
		resultLabel.setText (result);		//Tells outcome of game
		
		foldButton.setEnabled (false);
		showdownButton.setEnabled (false);
		playAgainButton.setEnabled (true);
	}
	
	//--------------------------------------------------------------------------
	// Resets the board to get ready for another game
	//--------------------------------------------------------------------------
	public void reset ()
	{
		player = dealer = house = null;
		
		topPanel.showHand (dealer);
		middlePanel.showHand (house);
		bottomPanel.showHand (player);
		
		resultLabel.setText (" ");
		
		dealButton.setEnabled (true);
		playAgainButton.setEnabled (false);
	}
	
	//**************************************************************************
	// Represents the action listener for the buttons
	//**************************************************************************
	private class ButtonListener implements ActionListener
	{
		//----------------------------------------------------------------------
		// Listens for the deal, fold, showdown, and playAgain buttons
		//----------------------------------------------------------------------
		public void actionPerformed (ActionEvent event)
		{			
			if (beginGame && event.getSource() == dealButton)
			{
				beginGame = false;
				preFlop();
				donePreFlop = true;
			}
			else if (donePreFlop && event.getSource() == dealButton)
			{
				donePreFlop = false;
				flop();
				doneFlop = true;
			}
			else if (doneFlop && event.getSource() == dealButton)
			{
				doneFlop = false;
				turn();
				doneTurn = true;
			}
			else if (doneTurn && event.getSource() == dealButton)
			{
				doneTurn = false;
				river();
			}
			
			//Increases the number of losses and updates the label
			if (event.getSource() == foldButton)	//Can fold after preFlop
			{
				dealerWins++;
				lossLabel.setText (loss + dealerWins);
				resultLabel.setText ("  You lose");
				middlePanel.displayLoss();
				
				dealButton.setEnabled (false);
				foldButton.setEnabled (false);
				showdownButton.setEnabled (false);
				playAgainButton.setEnabled (true);
			}
			
			if (event.getSource() == showdownButton) //Gives outcome of game
				showdown();
			
			if (event.getSource() == playAgainButton) //User can play again
			{
				reset();
				beginGame = true;
			}

			repaint();			
		}
	}
}
