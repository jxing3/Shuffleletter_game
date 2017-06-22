package edu.jhu.cs.jxing3.oose;

import edu.jhu.cs.oose.fall2014.shuffletter.iface.ShuffletterTile;



/** This is an implementation of the ShuffletterTile interface
 * @author Jesse Xing
 *
 */
public class MyShuffletterTile implements ShuffletterTile {
	
	private boolean wild;
	private char letter;
	
	/** Default constructor for a MyShuffletterTile object
	 * @param letter The character represented by the tile
	 * @param wild True if tile is wild, false otherwise
	 */
	public MyShuffletterTile(char letter, boolean wild) {

		this.wild = wild;
		this.letter = letter;
	}

	@Override
	public char getLetter() {
		return this.letter;
	}



	@Override
	public boolean isWild() {
		return this.wild;
	}

}
