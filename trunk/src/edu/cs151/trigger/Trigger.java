package edu.cs151.trigger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import javax.swing.Timer;



/**
 * This is the JFrame which the game resides.  This class contains the main().
 * @author Chris Wong
 *
 */
public class Trigger extends JFrame{
	
	private static int UPDATE_RATE = 10;
	// milliseconds
	GameManager content;
	boolean gameState = false;
		
	/**
	 * Initializes the game and runs it.
	 */
	public Trigger()
	{
		content = new GameManager();
		this.add(content);
		init();

		Timer t = new Timer(UPDATE_RATE, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				content.gameLoop();
			}
        });
        t.start();
        
	}
	
	/**
	 * Creates the size of the JFrame.
	 */
	public void init(){
		this.setSize(600,800);
		this.setResizable(false);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("Trigger Gear");
	}
	

	/**
	 * Runs the game.
	 * @param args No arguments are needed.
	 */
	public static void main(String[]args)
	{
		new Trigger();
	}
}
