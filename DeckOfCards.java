//******************************************************************************
// 	  DeckOfCards.java
//									Authors:   Partner &
//											   Vicki Trenton
//									Class:	   CS152
//									Assignment:Texas Hold'em Game
//
// Represents 52 cards in a deck
//******************************************************************************

import java.util.Random;

public class DeckOfCards
{
	private int size;	//Size of deck
	private Card[] deck;
	private Random generator;
	
	//--------------------------------------------------------------------------
	// Constructor: Assumes the standard 52 cards for the deck
	//--------------------------------------------------------------------------
	public DeckOfCards ()
	{
		size = 52;			   //Standard deck size
		deck = new Card[size]; //Deck begins with the standard 52 cards
	}
	
	//--------------------------------------------------------------------------
	// Initializes an array holding the 52 standard playing cards
	//--------------------------------------------------------------------------
	public void create ()
	{
		int valueIndex = 1;	//Range of 1 to 13 for values
		int suitIndex = 1;	//Range of 1 to 4 for suits
		
		//Assigns each suit 13 different values (2 to Ace)
		for (int index = 0; index < size; index++)
		{
			Card newCard = new Card (valueIndex-1, suitIndex-1);
			deck[index] = newCard;		//Adds card to deck
			
			//Cycles 4 times through values 1-13
			if (valueIndex == 13)
				valueIndex = 0;
			//Changes suit after creating 13 cards of one suit
			if (valueIndex % 13 == 0)
				suitIndex++;
				
			valueIndex++;
		}
	}
	
	//--------------------------------------------------------------------------
	// Shuffles the deck
	//--------------------------------------------------------------------------
	public void shuffle ()
	{
		int position;	//Index of random card in deck
		Card tempCard;	//Used in swapping 2 cards
		generator = new Random();
		
		//Switches each card of the deck with a random card
		for (int index = 0; index < size; index++)
		{
			position = generator.nextInt(size);
			//Swaps positions of 2 cards in deck 
			tempCard = deck[index];
			deck[index] = deck[position];
			deck[position] = tempCard;
		}
	}

	//--------------------------------------------------------------------------
	// Returns the "top" card and removes it from the deck
	//--------------------------------------------------------------------------
	public Card deal ()
	{
		Card top = deck[0];	//Top card of deck
		reduceByOne();
		
		return top;
	}
	
	//--------------------------------------------------------------------------
	// Discards the (unrevealed) "top" card from the deck
	//--------------------------------------------------------------------------
	public void bury ()
	{
		reduceByOne();
	}
	
	//--------------------------------------------------------------------------
	// Returns the number of cards left in the deck
	//--------------------------------------------------------------------------
	public int cardsLeft ()
	{
		return size;
	}
	
	//--------------------------------------------------------------------------
	// Reduces size of the deck by one card by removing the top card
	//--------------------------------------------------------------------------
	private void reduceByOne ()
	{
		Card[] tempDeck = new Card[size-1];
		
		for (int index = 0; index < size-1; index++)
			tempDeck[index] = deck[index+1];
		
		size--;
		deck = tempDeck;
	}
	
	//--------------------------------------------------------------------------
	// Returns a list of all cards in the deck
	//--------------------------------------------------------------------------
	public String toString ()
	{
		String list = "";
		for (Card playingCard : deck)
			list += playingCard + "\n";
		
		return list;
	}
}
