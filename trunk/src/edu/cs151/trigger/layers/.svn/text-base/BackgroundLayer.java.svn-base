package edu.cs151.trigger.layers;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * Draws the stars used in the background and scrolls it continuously
 * @author Tan Nguyen
 *
 */
public class BackgroundLayer{

	private BufferedImage background;
	private int yOffset = 0;
	private final int SCROLL_RATE;
	
	/**
	 * This is used to draw the background for the game
	 * @param scrollRate How fast the background scrolls.
	 * @param setTransparency Determine if the background should be transparent.  Use to layer several
	 * background layers on top of each other.
	 */
	public BackgroundLayer(int scrollRate, boolean setTransparency){
		
		SCROLL_RATE = scrollRate;
		
		if(setTransparency){
			background = new BufferedImage(600, 800, BufferedImage.TYPE_4BYTE_ABGR);
		}
		else{
			background = new BufferedImage(600, 800, BufferedImage.TYPE_USHORT_555_RGB);
		}
		Graphics g = background.getGraphics();
		g.setColor(Color.WHITE);
		int count = 0;
		while(count <100){
			Random rand = new Random();
			int size = rand.nextInt(5);
			g.fillOval(rand.nextInt(600), rand.nextInt(800), size, size);
			count++;
		}

	}
	
	/**
	 * Draws and stitches the background images so it will scroll smoothly
	 * @param g The graphics context to be used
	 */
	public void draw(Graphics g){
		yOffset += SCROLL_RATE;
		g.drawImage(background, 0, yOffset, null);
		g.drawImage(background, 0, yOffset-background.getHeight(), null);
		if(yOffset>=background.getHeight()){
			yOffset = 0;
		}
		
	}
}