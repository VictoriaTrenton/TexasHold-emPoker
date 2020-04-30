//******************************************************************************
// 	  TexasHolemDemo.java
//									Authors:   Partner &
//											   Vicki Trenton
//									Class:	   CS152
//									Assignment:Texas Hold'em Game
//
// A driver that simulates the Texas Hold'em poker card game
//******************************************************************************

import javax.swing.JFrame;

public class TexasHoldemDemo
{
	//--------------------------------------------------------------------------
	// Displays the TexasHolemPanel
	//--------------------------------------------------------------------------
	public static void main (String[] args)
	{
		JFrame frame = new JFrame ("Texas Hold'em");
		frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		
		frame.getContentPane().add (new TexasHoldemPanel());
		frame.pack();
		frame.setVisible (true);
	}
}
