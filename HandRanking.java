//******************************************************************************
// 	  HandRanking.java
//									Authors:   Jonathan Murley &
//											   Vicki Trenton
//									Class:	   CS152
//									Assignment:Texas Hold'em Game
//
// Determines the rank of the hand by finding its 5 best cards
//******************************************************************************

import java.text.DecimalFormat;

public class HandRanking
{
	private Card[] hand;			//7 cards to rank
	private int[] handValues;		//Number of times each value appears
	private int[] handSuits;		//Number of times each suit appears
	private Card[] best5Cards;		//5 best cards of the 7
	private int count;				//Number of non-kicker cards added to count
	private final int MAX_VAL_INDEX = 12;	//Maximum value index card can have
											//Is 12 in a regular deck
	private int highestGroup;	//stores the value of the highest pair, three 
								//of a kind, or four of a kind (depending on
								//what is best.
	private int lowestGroup;	//if there is also a pair in the best cards,
								//value stored here.
	private DecimalFormat fmt1, fmt2, fmt3, fmt4, fmt5; //formats output of
														//rank()
	//--------------------------------------------------------------------------
	// Constructor: Creates arrays to help evaluate the hand
	//--------------------------------------------------------------------------
	public HandRanking (Card[] newHand)
	{
		hand = newHand;
		handValues = new int[13];
		handSuits = new int[4];
		best5Cards = new Card[5];
		count = 0;
		highestGroup = lowestGroup = -1;
		
		//initiates all values in handValues to 0
		for(int i = 0; i < handValues.length; i++)
			handValues[i] = 0;
		//initiates all values in handSuits to 0
		for(int i = 0; i < handSuits.length; i++)
			handSuits[i] = 0;
		
		//Selects each value of handValues
		for (int value = 0; value <= MAX_VAL_INDEX; value++)
		{
			//Counts the number of times the value appears in the hand
			for (int i = 0; i < hand.length; i++)
				if (value == hand[i].getValueIndex())
					handValues[value]++;
		}
		
		//Selects each value of handSuits
		for (int suit = 0; suit < handSuits.length; suit++)
		{
			//Counts the number of times the suit appears in the hand
			for (int i = 0; i < hand.length; i++)
				if (suit == hand[i].getSuitIndex())
					handSuits[suit]++;
		}
		
		fmt1 = new DecimalFormat("0.##"); //for output with 1 set of 2 decimal
										  //places
		fmt2 = new DecimalFormat("0.####"); //for output with 2 sets of...
		fmt3 = new DecimalFormat("0.######"); //for output with 3 sets of...
		fmt4 = new DecimalFormat("0.########"); //etc.
		fmt5 = new DecimalFormat("0.##########");
	}
	
	//--------------------------------------------------------------------------
	// Returns a number to rank how good the hand is on a scale of 0(worst) to 9
	//--------------------------------------------------------------------------
	public double rank ()
	{
		//returns variables to normal in case rank() was called before
		best5Cards = new Card[5];
		count = 0;
		highestGroup = lowestGroup = -1;
		
		double result = 0;
		
		if (royalFlush())
		{
			result = 9;
			return result;
		}
		else if (straightFlush())
		{
			result = 8;
			//gives highest card in the straightFlush
			result += ((double) best5Cards[0].getValueIndex()) / 100;
			return Double.parseDouble(fmt1.format(result));
		}
		else if (fourOfAKind())
		{
			result = 7;
			//gives which values are 4 of a kind in the hundreth places
			//so that the comparison is more meaningful (for example,
			//4 aces give the result 7.12 and 4 jacks give the result
			//7.09. 4 aces are better than 4 jacks, which is shown by
			//the fact that 7.12 > 7.09.
			result += ((double) best5Cards[0].getValueIndex()) / 100;
			
			//as it is impossible for there to be two 4-of-a-kinds with
			//the same value when there is only one deck, kicker does
			//not need to be considered.
			
			return Double.parseDouble(fmt1.format(result));
		}
		else if (fullHouse())
		{
			result = 6;
			//gives the value of the three-of-a-kind in the hundreth place.
			result += ((double) best5Cards[0].getValueIndex()) / 100;
			//gives the value of the pair in the ten-thousandth place, for
			//similar reasons for putting the value of the three-of-a-kind
			//in the hundreth place.
			result += ((double) best5Cards[3].getValueIndex()) / 10000;
			
			return Double.parseDouble(fmt2.format(result));
		}
		else if (flush())
		{
			result = 5;
			//gives value of the highest flush card in hundreth place.
			result += ((double) best5Cards[0].getValueIndex()) / 100;
			//gives value of second-highest flush in 10000th place
			result += ((double) best5Cards[1].getValueIndex()) / 10000;
			//continues in a similar fashion for the other values to continue
			//comparing if the other cards are the same
			result += ((double) best5Cards[2].getValueIndex()) / 1000000;
			result += ((double) best5Cards[3].getValueIndex()) / 100000000;
			result += (((double) best5Cards[4].getValueIndex()) / 100000000)
																	/ 100;
			return Double.parseDouble(fmt5.format(result));
		}
		else if (straight())
		{
			result = 4;
			//gives value of the highest card in straight in hundreth place.
			result += ((double) best5Cards[0].getValueIndex()) / 100;
			return Double.parseDouble(fmt1.format(result));
		}
		else if (threeOfAKind())
		{
			result = 3;
			//gives value of three-of-a-kind in hundreth place.
			result += ((double) best5Cards[0].getValueIndex()) / 100;
			//gives value of highest kicker in the 10 000th place.
			result += ((double) best5Cards[3].getValueIndex()) / 10000;
			//gives value of second-highest kicker in 1 000 000th place.
			result += ((double) best5Cards[4].getValueIndex()) / 1000000;
			return Double.parseDouble(fmt3.format(result));
		}
		else if (twoPairs())
		{
			result = 2;
			//gives value of the first pair in hundreth place.
			result += ((double) best5Cards[0].getValueIndex()) / 100;
			//gives value of the second pair in the 10 000th place.
			result += ((double) best5Cards[2].getValueIndex()) / 10000;
			//gives value of the kicker in the 1 000 000th place.
			result += ((double) best5Cards[4].getValueIndex()) / 1000000;
			return Double.parseDouble(fmt3.format(result));
		}
		else if (onePair())
		{
			result = 1;
			//gives value of the pair in the 100th place.
			result += ((double) best5Cards[0].getValueIndex()) / 100;
			//gives the kickers in descending places off by multiples of 100
			result += ((double) best5Cards[2].getValueIndex()) / 10000;
			result += ((double) best5Cards[3].getValueIndex()) / 1000000;
			result += ((double) best5Cards[4].getValueIndex()) / 100000000;
			return Double.parseDouble(fmt4.format(result));
		}
		else
		{
			highestCards();
			//gives values of the cards in descending places off by multiples
			//of 100 (so that can show two digits).
			result += ((double) best5Cards[0].getValueIndex()) / 100;
			result += ((double) best5Cards[1].getValueIndex()) / 10000;
			result += ((double) best5Cards[2].getValueIndex()) / 1000000;
			result += ((double) best5Cards[3].getValueIndex()) / 100000000;
			result += (((double) best5Cards[4].getValueIndex()) / 100000000)
																	/ 100;
			return Double.parseDouble(fmt5.format(result));
		}
	}
	
	//--------------------------------------------------------------------------
	// Returns the 5 best cards from the 7-card hand
	//--------------------------------------------------------------------------
	public Card[] getBest5 ()
	{
		rank();
		
		return best5Cards;
	}
	
	//--------------------------------------------------------------------------
	// Determines if the hand has a Royal Flush
	//--------------------------------------------------------------------------
	private boolean royalFlush ()
	{
		boolean found = false;
		
		//Checks if straightFlush put an Ace as the highest of the 5 best cards
		if (straightFlush() && best5Cards[0].getValueIndex() == MAX_VAL_INDEX)
			found = true;
			
		return found;
	}
	
	//--------------------------------------------------------------------------
	// Determines if the hand has a Straight Flush
	//--------------------------------------------------------------------------
	private boolean straightFlush ()
	{
		boolean found = false, couldBeFlush = false;
		int suitIndexMarker = -1;	//marks what suit has enough to be flush
		
		//simplest case: if the best straight is flush
		if(straight() && isFlush(best5Cards))
			found = true;
		
		//more complicated cases
		else
		{
			//checks if there is a flush somewhere and what suit it is.
			for(int i = handSuits.length - 1; i >= 0 && !couldBeFlush; i--)
				if(handSuits[i] >= 5)
				{
					couldBeFlush = true;
					suitIndexMarker = i;
				}
			
			boolean couldBeStraight = false;
			int number = 1;
			int index;
			
			//runs through the highest possible top cards in a straight
			for(int i = hand.length - 1; i >= 4 && !found &&
							couldBeFlush; i--)
			{
				//if it is of the right suit
				if(hand[i].getSuitIndex() == suitIndexMarker)
				{
					best5Cards[0] = hand[i];
					couldBeStraight = true;	//true if the cards looked at to
											//this point are sequential in
											//value.
					
					index = i;
					
					for (int num = 1; index - number >= 0 && num < 5 &&
									couldBeStraight; num++)
					{
						//if there is a double here, add one to "number" so
						//that it skips that value when comparing.
						if((index-number)>=0 && hand[index].getValueIndex() ==
							hand[index - number].getValueIndex())
							number++;
						//again, as there could be a triple.
						if(index-number>=0 && hand[index].getValueIndex() ==
							hand[index - number].getValueIndex())
							number++;
						
						//possible doubles again--this time for between the
						//next number could be doubled.
						if(index-(number+1)>=0 &&
							hand[index-number].getSuitIndex()!=suitIndexMarker
								&& hand[index-number].getValueIndex()==
									hand[index-(number+1)].getValueIndex())
							number++;
						//again, possible triple
						if(index-(number+1)>=0 &&
							hand[index-number].getSuitIndex()!=suitIndexMarker
								&& hand[index-number].getValueIndex()==
									hand[index-(number+1)].getValueIndex())
							number++;
						
						//now compare for if could be in a straight with the
						//first number by seeing if it is "num" less than
						//the first number and the same suit, so long
						//the new card's index is not less than zero.
						if (index-number >= 0 && hand[index].getValueIndex() ==
								hand[index - number].getValueIndex() + 1
									&& suitIndexMarker == 
										hand[index-number].getSuitIndex())
							best5Cards[num] = hand[index-number];
						else
							couldBeStraight = false;
						
						index -= number;
						
						number = 1;
					}
					
					if(couldBeStraight && isStraight(best5Cards))
						found = true;
				}
			}
		}
		
		if(!found)
			//returns "best5Cards" to null
			for(int i = 0; i < best5Cards.length; i++)
				best5Cards[i] = null;
		
		return found;
	}
	
	//--------------------------------------------------------------------------
	// Determines if the hand has Four of a Kind
	//--------------------------------------------------------------------------
	private boolean fourOfAKind ()
	{
		boolean found = false;
		
		if (findSameValueCards (4, 1, 1))
			found = true;
		
		return found;
	}
	
	//--------------------------------------------------------------------------
	// Determines if the hand has a Full House
	//--------------------------------------------------------------------------
	private boolean fullHouse ()
	{
		boolean found = false;
		
		if(findSameValueCards(3, 1, 2) && findSameValueCards(2, 2, 2))
			found = true;
		
		return found;
	}
	
	//--------------------------------------------------------------------------
	// Determines if the hand has a Flush
	//--------------------------------------------------------------------------
	private boolean flush ()
	{
		boolean found = false;
		int suitIndex = -1;				//Index of suit of the 5 cards
		int num = 0;					//Number of cards added to best5Cards
	
		//Searches the hand for at least 5 cards of the same suit
		for (int i = 0; i < handSuits.length; i++)
			if (handSuits[i] >= 5)
			{
				found = true;
				suitIndex = i;
			}
				
		//Adds the 5 cards of the same suit to the 5 best cards
		if (found)
		{
			while (num != best5Cards.length)
			{
				//Searches the hand for cards of the highest value of that suit
				for (int value = MAX_VAL_INDEX; value >= 0; value--)
				{
					for (int i = 0; i < hand.length; i++)
					{
						if (hand[i].getValueIndex() == value
							&& hand[i].getSuitIndex() == suitIndex
							&& num < 5)
						{
							//Adds the cards to best5Cards
							best5Cards[num] = hand[i];
							num++;
						}	
					}
				}
			}
		}
		return found;
	}
	
	//--------------------------------------------------------------------------
	// Determines if the hand has a Straight
	//--------------------------------------------------------------------------
	private boolean straight ()
	{
		boolean found = false;
		boolean couldWork;	//for inner for loop. is false when it is
							//found that this straight will not work.
		int numWork; 	//counts number that seem to be working with this
						//straight. originally 1, as a single card is a
						//run with itself.
		
		//runs through the possible highest values for a straight
		for(int i = 6; i >= 4 && !found; i--)
		{
			couldWork = true;
			numWork = 1;
			
			best5Cards[numWork - 1] = hand[i];
			//runs through the values less than i to see if they form a
			//straight.
			for(int num = i - 1; num >= 0 && couldWork && !found; num--)
			{
				if(hand[i].getValueIndex() == hand[num].getValueIndex())
					; 	//do nothing, as this means that they are same and
						//we want to check the next value.
					
				//if the values of adjacent cards are different by one, then
				//they are in a straight with each other.
				else if (hand[num + 1].getValueIndex()
						== (hand[num].getValueIndex() + 1))
				{
					numWork++;
					best5Cards[numWork - 1] = hand[num];
				}
				else
					couldWork = false;
				
				if(numWork == 5)	//if we have found 5 cards that are in a
					found = true;	//row then we have found a straight.
				if(5 - numWork > num)	//if there is more cards left to find
					couldWork = false;	//in the straight than there are cards
										//left, then it won't work.
			}
			
			//if it was not found, return best5Cards to null
			if(!found)
				for(int x = 0; x < best5Cards.length; x++)
					best5Cards[x] = null;
		}
		
		return found;
	}
	
	//--------------------------------------------------------------------------
	// Determines if the hand has Three of a Kind
	//--------------------------------------------------------------------------
	private boolean threeOfAKind ()
	{
		boolean found = false;
		
		if (findSameValueCards (3, 1, 1))
			found = true;
		
		return found;
	}
	
	//--------------------------------------------------------------------------
	// Determines if the hand has Two Pairs
	//--------------------------------------------------------------------------
	private boolean twoPairs ()
	{
		boolean found = false;
		
		if(findSameValueCards(2, 1, 2) && findSameValueCards(2, 2, 2))
			found = true;
		
		return found;
	}
	
	//--------------------------------------------------------------------------
	// Determines if the hand has One Pair
	//--------------------------------------------------------------------------
	private boolean onePair ()
	{
		boolean found = false;
		
		if (findSameValueCards (2, 1, 1))
			found = true;
		
		return found;
	}
	
	//--------------------------------------------------------------------------
	// Returns true if the specified number of cards of the same value are found
	// Adds those cards to the 5 best cards.
	// Second int says how many times will a method has called this and
	// third int says the maximum number of times it will call it (for example,
	// fullHouse() looks for a three-of-a-kind and then a pair. So for the
	// first time it is called, it will give (3, 1, 2) as the parameters and
	// the second times it will give (2, 2, 2).
	//--------------------------------------------------------------------------
	private boolean findSameValueCards (int numCards, int howManyTimes,
														int maxNumTimes)
	{
		boolean found = false;
		int highestValue = -1;
		
		//for when checking for the first of two groupings
		if(howManyTimes == 1)
		{
			//Begins with the number of aces in the hand
			for (int value = handValues.length-1; value >= 0 && !found; value--)
			{
				//Are there numCards of this value?
				if (handValues[value] == numCards)
				{
					found = true;
					highestValue = value;
				}	
			}
		}
		//when checking for the second of two groupings
		else if(howManyTimes == 2)
		{
			//Begins with the number of aces in the hand
			for (int value = handValues.length-1; value >= 0 && !found; value--)
			{
				//Are there numCards of this value?
				//Checks to make sure that this has not been used yet by the
				//first time through.
				//Checks for if it is at least as high as the number of cards
				//one is looking for, as a full house can be made out of two
				//groups of three by acting as if the group of three were
				//a group of two.
				if (handValues[value] >= numCards && value != highestGroup)
				{
					found = true;
					highestValue = value;
				}	
			}
		}
		
		if (found)
		{
			//Searches the hand for cards of the highest value
			for (int index = hand.length - 1; index >= 0; index--)
			{
				//Adds numCards of this value to best5Cards
				//checks to make sure there is not an out of bounds exception.
				if (hand[index].getValueIndex() == highestValue
							&& count < best5Cards.length)
				{
					best5Cards[count] = hand[index];
					count++;
				}	
			}
		}
		
		//if this is the first time, places it in highestGroup.
		if(howManyTimes == 1)
			highestGroup = highestValue;
		//if this is the second time, places it in lowestGroup.
		else if(howManyTimes == 2)
			lowestGroup = highestValue;
		//will not need any other times, as cannot be more than two groupings
		//in one best5Cards.
		
		//if this is the last time to go through this and there are empty
		//spaces, then fill it up.
		if(found && count < best5Cards.length && howManyTimes >= maxNumTimes)
			for(int i = hand.length-1; i>=0 && count<best5Cards.length; i--)
				if(!(hand[i].getValueIndex() == highestGroup ||
					hand[i].getValueIndex() == lowestGroup))
				{
					best5Cards[count] = hand[i];
					count++;
				}
		
		//if not found or the last time it will be called, returns count to 0
		if(!found || howManyTimes >= maxNumTimes)
			count = 0;
		
		return found;
	}
	
	//--------------------------------------------------------------------------
	// If there is nothing in the hand, then best5Cards gets the highest
	// cards in the hand.
	//--------------------------------------------------------------------------
	private void highestCards()
	{
		int num = 0;
		for(int i = hand.length - 1; i >= 2 && num < best5Cards.length; i--)
		{
			best5Cards[num] = hand[i];
			num++;
		}
	}
	
	//--------------------------------------------------------------------------
	// Checks if a given set of 5 cards is flush.
	//--------------------------------------------------------------------------
	private boolean isFlush(Card[] cards)
	{
		boolean flush = false;
		
		if(cards.length != 5)
			System.out.println("Wrong size card set.");
		else if (cards[0].getSuitIndex() == cards[1].getSuitIndex() &&
					cards[1].getSuitIndex() == cards[2].getSuitIndex() &&
					cards[2].getSuitIndex() == cards[3].getSuitIndex())
				flush = true;
		
		return flush;
	}
	//--------------------------------------------------------------------------
	// Checks if a given set of 5 cards is a straight.
	//--------------------------------------------------------------------------
	private boolean isStraight(Card[] cards)
	{
		boolean straight = false;
		
		if(cards.length != 5)
			System.out.println("Wrong size card set.");
		else if(!(cards[0] == null || cards[1] == null || cards[2] == null ||
						cards[3] == null || cards[4] == null))
		{
			if(cards[0].getValueIndex() == cards[1].getValueIndex()+1 &&
					cards[1].getValueIndex() == cards[2].getValueIndex()+1 &&
					cards[2].getValueIndex() == cards[3].getValueIndex()+1 &&
					cards[3].getValueIndex() == cards[4].getValueIndex()+1)
				straight = true;
			else if(cards[0].getValueIndex() == cards[1].getValueIndex()-1 &&
					cards[1].getValueIndex() == cards[2].getValueIndex()-1 &&
					cards[2].getValueIndex() == cards[3].getValueIndex()-1 &&
					cards[3].getValueIndex() == cards[4].getValueIndex()-1)
				straight = true;
		}
		return straight;
	}
}