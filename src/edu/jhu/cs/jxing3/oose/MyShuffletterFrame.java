package edu.jhu.cs.jxing3.oose;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.jhu.cs.oose.fall2014.shuffletter.iface.NoOpShuffletterModelListener;
import edu.jhu.cs.oose.fall2014.shuffletter.iface.ShuffletterIllegalMoveEvent;
import edu.jhu.cs.oose.fall2014.shuffletter.iface.ShuffletterModel;
import edu.jhu.cs.oose.fall2014.shuffletter.iface.ShuffletterTileMovedEvent;
import edu.jhu.cs.oose.fall2014.shuffletter.iface.ShuffletterTilePlayedEvent;


/** Creates the basic GUI frame for the Shuffletter game
 * using custom JComponents for the board and supply
 * and standard JComponents for the label and EndGame buttons
 * @author Jesse Xing
 *
 */


public class MyShuffletterFrame extends JFrame {
	
	/**
	 * Serial provided to eliminate warning
	 */
	private static final long serialVersionUID = 1L;
	
	private ShuffletterModel model;
	private MyShuffletterBoard board;

	
	/** Initializes a GUI frame with a custom board and supply in a JScrollPane for tiles
	 * Utilizes BorderLayout as the main Layout manager
	 * Registers button clicks and calls endRound() in the model
	 * Changes status message based on model outputs
	 * @param model The ShuffletterModel initialized and handed by a main program
	 */
	MyShuffletterFrame(final ShuffletterModel model) {
		this.model = model;
		
		this.board = new MyShuffletterBoard(this.model);
		MyShuffletterSupply supply = new MyShuffletterSupply(this.model, board);

		
		JButton button = new JButton("End Round");
		final JLabel label = new JLabel();
		JScrollPane scrollPaneBoard= new JScrollPane(board);
		JScrollPane scrollPaneSupply = new JScrollPane(supply);

		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BorderLayout());
		bottomPanel.add(label, BorderLayout.CENTER);
		bottomPanel.add(button, BorderLayout.EAST);
		
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(scrollPaneBoard, BorderLayout.CENTER);
		this.getContentPane().add(scrollPaneSupply, BorderLayout.EAST);
		this.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
		
		label.setText(model.getBagCount()+" tiles left in the bag.");
		
		//Creates an action listener so button will call endRound() when pressed
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MyShuffletterFrame.this.model.endRound();
			}
		});
		
		//Changes message status based on calls by model to listeners
		this.model.addListener(new NoOpShuffletterModelListener() {
			
			@Override
			public void tilePlayed(ShuffletterTilePlayedEvent arg0) {
				label.setForeground(Color.BLACK);
				label.setText(model.getBagCount()+" tiles left in the bag.");
			}
			
			@Override
			public void tileMoved(ShuffletterTileMovedEvent arg0) {
				label.setForeground(Color.BLACK);
				label.setText(model.getBagCount()+" tiles left in the bag.");
			}
			
			@Override
			public void roundEnded() {
				label.setForeground(Color.BLACK);
				label.setText(model.getBagCount()+" tiles left in the bag.");
				
			}
			
			@Override
			public void illegalMoveMade(ShuffletterIllegalMoveEvent arg0) {
				label.setForeground(Color.RED);
				label.setText(arg0.getMessage());
			}
			
			@Override
			public void gameEnded() {
				label.setForeground(Color.BLACK);
				label.setText("Game over!");
			}
		});
		this.pack();
		
	}

}
