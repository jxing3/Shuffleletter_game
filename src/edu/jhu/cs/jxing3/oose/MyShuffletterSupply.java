package edu.jhu.cs.jxing3.oose;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;

import edu.jhu.cs.oose.fall2014.shuffletter.iface.NoOpShuffletterModelListener;
import edu.jhu.cs.oose.fall2014.shuffletter.iface.ShuffletterIllegalMoveEvent;
import edu.jhu.cs.oose.fall2014.shuffletter.iface.ShuffletterModel;
import edu.jhu.cs.oose.fall2014.shuffletter.iface.ShuffletterTile;
import edu.jhu.cs.oose.fall2014.shuffletter.iface.ShuffletterTileMovedEvent;
import edu.jhu.cs.oose.fall2014.shuffletter.iface.ShuffletterTilePlayedEvent;


/** A custom JComponent to represent the supply in the game Shuffletter 
 * @author Jesse Xing
 *
 */
public class MyShuffletterSupply extends JComponent{
	
	/**
	 * Serial provided to eliminate warning
	 */
	private static final long serialVersionUID = 1L;
	public static final int SUPPLY_LENGTH = 700;
	public static final int SUPPLY_WIDTH = 100;
	public static final int rows = 14;
	public static final int columns = 2; 


	private ShuffletterModel model;
	private MyShuffletterBoard board;
	
	/** Default constructor for a MyShuffletterSupply object
	 * Draws a grid and tiles in the supply(from model)
	 * Registers clicks and hands tiles to a MyShuffletterBoard component
	 * @param model a ShuffletterModel implementation
	 * @param b a MyShuffletterBoard component - needed to pass tiles
	 */
	public MyShuffletterSupply(ShuffletterModel model,MyShuffletterBoard b) {
		super();
		this.board = b;
		
		this.model = model;
		
		//repaints supply if a tile has been played or if the round has ended
		this.model.addListener(new NoOpShuffletterModelListener() {
			
			@Override
			public void tilePlayed(ShuffletterTilePlayedEvent arg0) {
				repaint();
			}
			
			@Override
			public void tileMoved(ShuffletterTileMovedEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void roundEnded() {
				repaint();
			}
			
			@Override
			public void illegalMoveMade(ShuffletterIllegalMoveEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void gameEnded() {
				// TODO Auto-generated method stub
				
			}
		});
		
		/*Registers a mouse click and hands tile to board if 
		there exists one at the clicked location*/
		this.addMouseListener(new MouseAdapter() {	
			
			@Override
			public void mouseClicked(MouseEvent e) {
				
				if(e.getX()>MyShuffletterSupply.SUPPLY_WIDTH||e.getY()>MyShuffletterSupply.SUPPLY_LENGTH) {
					return;
				}
			
				int x = e.getX()/MyShuffletterTileComponent.TILE_SIZE;
				int y = e.getY()/MyShuffletterTileComponent.TILE_SIZE;
				
				
				int index = y +rows*x;
				
				if(index<MyShuffletterSupply.this.model.getSupplyContents().size()){
				
					board.handTile(MyShuffletterSupply.this.model.getSupplyContents().get(index));
				}
			}
		});
		
	}
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(SUPPLY_WIDTH, SUPPLY_LENGTH);
	}
	
	@Override
	public void paint(Graphics g) {
		
		//draws the grid for the supply
		g.setColor(Color.black);
		g.drawLine(SUPPLY_WIDTH/columns, 0, SUPPLY_WIDTH/columns, SUPPLY_LENGTH);
		for (int i= 0; i<=SUPPLY_LENGTH; i+=SUPPLY_LENGTH/rows) {
			g.drawLine(0,i, SUPPLY_WIDTH, i);
		}
		
		int x =0;
		int y =0;
		
		//draws the tiles in the supply from the model
		for(ShuffletterTile t : this.model.getSupplyContents()){
			
			if(t.isWild()) {
				MyShuffletterTileComponent tile = new MyShuffletterTileComponent(x, y,' ');
				tile.paint(g);
				
			} else {
			
				MyShuffletterTileComponent tile = new MyShuffletterTileComponent(x, y, t.getLetter());
				tile.paint(g);
			}
				
			y +=MyShuffletterTileComponent.TILE_SIZE;
				
			if(y==SUPPLY_LENGTH) {
				y=0;
				x += MyShuffletterTileComponent.TILE_SIZE;
							
			}
		}
				
	}
}
