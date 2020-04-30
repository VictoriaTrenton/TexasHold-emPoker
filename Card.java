//******************************************************************************
// 	  Card.java
//									Authors:   Partner & Victoria Trenton
//									Class:	   CS152
//									Assignment:Texas Hold'em Game
//
// Represents a single playing card
//******************************************************************************

import javax.swing.*;
import java.awt.*;

public class Card
{
	private int valueIndex, suitIndex;	//Indicies of array elements
	private ImageIcon faceupImage;		//Shows value and suit
	private ImageIcon facedownImage;	//Hides value and suit
	private ImageIcon image;			//Current image of card
	private boolean faceup;				//Is card faceup or facedown?
	
	private String[] cardValues = {"2", "3", "4", "5", "6", "7", "8", "9",
		"10", "J", "Q", "K", "A"};
	private String[] cardSuits = {"club", "diamond", "heart", "spade"};

	//--------------------------------------------------------------------------
	// Constructor: Sets the card's value, suit, and image
	//--------------------------------------------------------------------------
	public Card (int newValueIndex, int newSuitIndex)
	{
		valueIndex = newValueIndex;
		suitIndex = newSuitIndex;
		
		//Sets the faceup image that corresponds to its value and suit
		String val;					
		char suit;
		
		if (valueIndex <= 8)			//Value is 2-10
			val = cardValues[valueIndex];
		else if (valueIndex == 9)		//Value is Jack
			val = "jack";
		else if (valueIndex == 10)		//Value is Queen
			val = "queen";
		else if (valueIndex == 11)		//Value is King
			val = "king";
		else							//Value is Ace
			val = "ace";			
		suit = cardSuits[suitIndex].charAt (0);
		
		faceupImage = new ImageIcon ("Playing Cards/" + val + suit + ".jpg");
		facedownImage = new ImageIcon ("Playing Cards/back.jpg");
		image = faceupImage;
		faceup = true;
	}
	
	//--------------------------------------------------------------------------
	// Compares this card to a given card
	//--------------------------------------------------------------------------
	public int compareTo (Card otherCard)
	{
		int result;
		
		//Card value is less than other card's
		if (getValueIndex() < otherCard.getValueIndex())
			result = -1;
		//Card value is equal to other card's
		else if (getValueIndex() == otherCard.getValueIndex())
			result = 0;
		//Card value is greater than other card's
		else
			result = 1;
			
		return result;
	}
	
	//--------------------------------------------------------------------------
	// Turns the card over (faceup to facedown or facedown to faceup)
	//--------------------------------------------------------------------------
	public void flip ()
	{
		if (faceup)
		{
			faceup = false;
			image = facedownImage;
		}
		else
		{
			faceup = true;
			image = faceupImage;
		}
	}
	
	//--------------------------------------------------------------------------
	// Draws the card image
	//--------------------------------------------------------------------------
	public void draw (JPanel drawing_panel, Graphics page, int x, int y)
	{
		image.paintIcon (drawing_panel, page, x, y);
	}
	
	//--------------------------------------------------------------------------
	// Returns a boolean (Is the face of the card visible?)
	//--------------------------------------------------------------------------
	public boolean isFaceup ()
	{
		if (faceup)
			return true;
		else
			return false;
	}
	
	//--------------------------------------------------------------------------
	// Returns the index of the card's value
	//--------------------------------------------------------------------------
	public int getValueIndex ()
	{
		return valueIndex;
	}
	
	//--------------------------------------------------------------------------
	// Returns the index of the card's suit
	//--------------------------------------------------------------------------
	public int getSuitIndex ()
	{
		return suitIndex;
	}
	
	//--------------------------------------------------------------------------
	// Returns card information of value and suit
	//--------------------------------------------------------------------------
	public String toString ()
	{
		return cardValues[valueIndex] + "-" + cardSuits[suitIndex];
	}
}
