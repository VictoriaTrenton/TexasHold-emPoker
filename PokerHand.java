//******************************************************************************
// 	  PokerHand.java
//									Authors:   Partner & Victoria Trenton
//									Class:	   CS152
//									Assignment:Texas Hold'em Game
//
// A poker hand that holds up to 7 playing cards
//******************************************************************************

public class PokerHand
{
	private final int HAND_SIZE;	//Maximum number of cards in hand
	private int cardCount;			//Number of cards in hand
	private Card[] hand;
	
	//--------------------------------------------------------------------------
	// Constructor: Creates a new PokerHand
	//--------------------------------------------------------------------------
	public PokerHand ()
	{
		HAND_SIZE = 7;
		cardCount = 0;
		hand = new Card[HAND_SIZE];	//Each hand holds 7 Cards
	}
	
	//--------------------------------------------------------------------------
	// Sorts cards in hand (disregarding the suit)
	//--------------------------------------------------------------------------
	public void sort ()
	{
		int minIndex;	//Index of the lowest-value card
		Card tempCard;	//Used in swapping 2 cards
	
		for (int index = 0; index < cardCount-1; index++)
		{
			minIndex = index;
			//Searches for cards that need to swap places
			for (int num = index+1; num < cardCount; num++)
			{
				//If the minIndex card's value is greater than a 
				//higher-index card's, assign minIndex the lower-index
				if (hand[minIndex].compareTo(hand[num]) > 0)
					minIndex = num;
			}
			//Swaps the cards
			tempCard = hand[minIndex];
			hand[minIndex] = hand[index];
			hand[index] = tempCard;
		}
	}
	
	//--------------------------------------------------------------------------
	// Adds a card to the hand. The playing cards are stored in sorted
	// order, based on card value (disregarding the suit)
	//--------------------------------------------------------------------------
	public void addCard (Card newCard)
	{
		if (cardCount < 7)	// 7 cards signal a full hand
		{
			hand[cardCount] = newCard;	//Adds card to hand
			cardCount++;
			sort();
		}
		else
			System.out.println ("Full hand");
	}
	
	//--------------------------------------------------------------------------
	// Returns a point value for the best 5 cards in the hand
	//--------------------------------------------------------------------------
	public double evaluate ()
	{
		HandRanking rankTheHand = new HandRanking (hand);
		
		return rankTheHand.rank();
	}
	
	//--------------------------------------------------------------------------
	// Compares this hand to a given hand and returns whether it is less than,
	// equal to, or greater than the given hand
	//--------------------------------------------------------------------------
	public int compareTo (PokerHand otherHand)
	{
		if (evaluate() < otherHand.evaluate())
			return -1;
		else if (evaluate() == otherHand.evaluate())
			return 0;
		else
			return 1;
	}
	
	//--------------------------------------------------------------------------
	// Flips all the cards in the hand to faceup
	//--------------------------------------------------------------------------
	public void flipFaceup ()
	{
		for (int i = 0; i < cardCount; i++)
			if (!hand[i].isFaceup())
				hand[i].flip();
	}
	
	//--------------------------------------------------------------------------
	// Flips all the cards in the hand to facedown
	//--------------------------------------------------------------------------
	public void flipFacedown ()
	{
		for (int i = 0; i < cardCount; i++)
			if (hand[i].isFaceup())
				hand[i].flip();
	}
	
	//--------------------------------------------------------------------------
	// Returns the card at the specified index
	//--------------------------------------------------------------------------
	public Card getCard (int index)
	{
		return hand[index];
	}
	
	//--------------------------------------------------------------------------
	// Returns the number of cards in the hand
	//--------------------------------------------------------------------------
	public int getCardCount()
	{
		return cardCount;
	}
	
	//--------------------------------------------------------------------------
	// Returns information on all cards in the hand in sorted order
	//--------------------------------------------------------------------------
	public String toString ()
	{
		sort();
		String list = "";
		
		for (Card playingCard : hand)
			if (playingCard != null)
				list += playingCard + " ";
						
		return list;
	}
}
