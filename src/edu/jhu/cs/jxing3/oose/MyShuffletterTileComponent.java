package edu.jhu.cs.jxing3.oose;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;


/**A custom JComponent to represent a single tile in the game Shuffletter 
 * @author Jesse Xing
 *
 */
public class MyShuffletterTileComponent extends JFrame {
	/**
	 * Serial provided to eliminate warning
	 */
	private static final long serialVersionUID = 1L;
	public static final int TILE_SIZE = 50;
	private int x;
	private int y;
	private char c;
	
	/** Draws a yellow tile that is 50 by 50 pixels starting from
	 * position x and y (top-left corner) with a character centered in 
	 * the middle
	 * @param x the x position for the top-left corner 
	 * @param y the y position for the top-left corner
	 * @param c the character to draw a tile
	 */
	public MyShuffletterTileComponent(int x,int y,char c) {
		super();
		this.x = x;
		this.y = y;
		this.c = c;
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(TILE_SIZE, TILE_SIZE);
	}
	
	@Override
	public void paint(Graphics g) {
		
		//draws the tile rectangle
		
		g.setColor(Color.black);
		g.drawRect(this.x, this.y, TILE_SIZE, TILE_SIZE);
		
		g.setColor(Color.yellow);
		g.fillRect(this.x+1, this.y+1, TILE_SIZE-1, TILE_SIZE-1);

		g.setColor(Color.black);
		
		g.setFont(new Font("Arial",Font.BOLD,20));
		
		//draws a character in the middle
		
		 FontMetrics fm = g.getFontMetrics();
		 Rectangle2D textsize = fm.getStringBounds(this.c+"", g);
		 
		 int xOff = (int) (TILE_SIZE - textsize.getWidth()) / 2;
		 int yOff = (int) (TILE_SIZE - textsize.getHeight()) / 2 + fm.getAscent();
		
	     g.drawString(this.c + "",this.x+xOff,this.y+yOff);
	
	}
	
}
