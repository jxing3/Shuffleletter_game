package edu.jhu.cs.jxing3.oose;

import java.io.IOException;

import javax.swing.JFrame;

import edu.jhu.cs.oose.fall2014.shuffletter.iface.ShuffletterModel;


/** Main program that runs the Shuffletter GUI
 * @author Jesse Xing
 *
 */
public class MyShuffletterGUI {

	public static void main(String[] args) throws IOException {
        ShuffletterModel model = new MyShuffletterModel();    
        MyShuffletterFrame gui = new MyShuffletterFrame(model);
        gui.setLocationRelativeTo(null);
		gui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
       // gui.setResizable(false);
        gui.setVisible(true);
	}
	
}
