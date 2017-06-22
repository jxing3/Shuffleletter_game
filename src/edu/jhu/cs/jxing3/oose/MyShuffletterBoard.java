package edu.jhu.cs.jxing3.oose;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;

import edu.jhu.cs.oose.fall2014.shuffletter.iface.NoOpShuffletterModelListener;
import edu.jhu.cs.oose.fall2014.shuffletter.iface.Position;
import edu.jhu.cs.oose.fall2014.shuffletter.iface.ShuffletterIllegalMoveEvent;
import edu.jhu.cs.oose.fall2014.shuffletter.iface.ShuffletterModel;
import edu.jhu.cs.oose.fall2014.shuffletter.iface.ShuffletterTile;
import edu.jhu.cs.oose.fall2014.shuffletter.iface.ShuffletterTileMovedEvent;
import edu.jhu.cs.oose.fall2014.shuffletter.iface.ShuffletterTilePlayedEvent;


/** A custom JComponent for the board of the game Shuffletter
 * @author Jesse Xing
 *
 */
public class MyShuffletterBoard extends JComponent{
	
	/**
	 * Serial provided to eliminate warning
	 */
	private static final long serialVersionUID = 1L;
	
	private static final int INIT_BOARD_SIZE = 800;
	private static final int INIT_ROW_COLUMN = 16;
	private static final char WILD_CHAR= ' ';
	private int boardWidth;
	private int boardHeight;


	private ShuffletterModel model;
	
	private int rows;
	private int columns;
	private int offsetX;
	private int offsetY;
	
	private ShuffletterTile supplyLetter;
	private Position boardLetter;
	
	/**Default constructor for a MyShuffletterBoard object
	 * Draws a grid to represent the board 
	 * Also draws tiles on the board from the model
	 * Registers clicks as playing a tile from supply or moving a tile on the board
	 * Expands when tile is placed in a space near the edge
	 * @param model a ShuffletterModel implementation
	 */
	public MyShuffletterBoard(ShuffletterModel model) {
		super();
		this.supplyLetter = null;
		this.boardLetter = null;
		this.boardHeight = INIT_BOARD_SIZE;
		this.boardWidth = INIT_BOARD_SIZE;
		this.rows = INIT_ROW_COLUMN;
		this.columns = INIT_ROW_COLUMN;
		this.offsetX = 0;
		this.offsetY = 0;
		
		this.model = model;
		
		//Repaints board when tile is played or moved
		this.model.addListener(new NoOpShuffletterModelListener() {
			
			@Override
			public void tilePlayed(ShuffletterTilePlayedEvent arg0) {
				revalidate();
				repaint();
			}
			
			@Override
			public void tileMoved(ShuffletterTileMovedEvent arg0) {
				revalidate();
				repaint();
			}
			
			@Override
			public void roundEnded() {
				// TODO Auto-generated method stub
				
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
		
		// Processes a Tile when mouse is clicked within board, ignores inputs otherwise
		this.addMouseListener(new MouseAdapter() {	
			
			@Override
			public void mouseClicked(MouseEvent e) {
				
				if(e.getX()>MyShuffletterBoard.this.boardWidth||e.getY()>MyShuffletterBoard.this.boardHeight) {
					return;
				}
				
				int x = e.getX()/MyShuffletterTileComponent.TILE_SIZE;
				int y = e.getY()/MyShuffletterTileComponent.TILE_SIZE;
				procLetter(x, y);
													
			}
		});
	}
	
	/**Processes a click at tile position x,y
	 * If there is a supplyLetter program tries to play the tile on the board
	 * Else if there is not a boardLetter, program tries to add a boardLetter from that position
	 * Otherwise if there is a boardLetter, program tries to move boardLetter into new position
	 * The board expands if there are tiles placed near the edge of the board
	 * @param x the x position of the tile
	 * @param y the y position of the tile
	 */
	private void procLetter(int x, int y) {
		if(supplyLetter!=null) {
			MyShuffletterBoard.this.model.play(supplyLetter, new Position(x-this.offsetX,-(y-this.offsetY)));
			supplyLetter = null;
		} else if(boardLetter == null) {
			if(MyShuffletterBoard.this.model.getTile(new Position(x-this.offsetX,-(y-this.offsetY)))!=null) {
				boardLetter = new Position(x-this.offsetX,-(y-this.offsetY));
			}
		} else {
			MyShuffletterBoard.this.model.move(boardLetter, new Position(x-this.offsetX,-(y-this.offsetY)));
			boardLetter = null;
		}
		
		//code to expand board if there exists a tile at the edge
		
		for(Position p: MyShuffletterBoard.this.model.getTilePositions()) {
			if(p.getX()+this.offsetX > (boardWidth/MyShuffletterTileComponent.TILE_SIZE -2)) {
				boardWidth+=MyShuffletterTileComponent.TILE_SIZE;
				columns++;
			}
			if(-p.getY()+this.offsetY > (boardHeight/MyShuffletterTileComponent.TILE_SIZE -2)) {
				boardHeight+=MyShuffletterTileComponent.TILE_SIZE;
				rows++;
			}
			if(p.getX()+this.offsetX <1) {
				boardWidth+=MyShuffletterTileComponent.TILE_SIZE;
				columns++;
				this.offsetX++;
			}
			if(-p.getY()+this.offsetY <1) {
				boardHeight+=MyShuffletterTileComponent.TILE_SIZE;
				rows++;
				this.offsetY++;
			}
		}
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(boardWidth, boardHeight);
	}
	
	@Override
	public void paint(Graphics g) {
		
		//draws the board grid
		g.setColor(Color.white);
		g.fillRect(0, 0, boardWidth, boardHeight);

		g.setColor(Color.black);
		
		
		for (int i= 0; i<=boardWidth; i+=boardWidth/columns) {
			g.drawLine(i,0, i,boardHeight);
		}
		for (int i= 0; i<=boardHeight; i+=boardHeight/rows) {
			g.drawLine(0,i, boardWidth, i);
		}
		
		//draws the tiles onto the board (obtained from model)
		for(Position p: this.model.getTilePositions()) {
			
			
			int x = (p.getX()+this.offsetX)*MyShuffletterTileComponent.TILE_SIZE;
			
			int y = (-p.getY()+this.offsetY)*MyShuffletterTileComponent.TILE_SIZE;
			
			if(this.model.getTile(p).isWild()) {
				MyShuffletterTileComponent t = new MyShuffletterTileComponent(x, y,WILD_CHAR);
				t.paint(g);	

			} else {
				MyShuffletterTileComponent t = new MyShuffletterTileComponent(x, y,this.model.getTile(p).getLetter());
				t.paint(g);	
			}
		}
	}
	
	/** Sets supplyLetter to the ShuffletterTile handed to it by the supply
	 * @param t The ShuffletterTile handed by the Supply component
	 */
	public void handTile(ShuffletterTile t) {
		this.supplyLetter = t;
				
	}
	


}
