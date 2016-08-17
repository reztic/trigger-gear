package edu.cs151.trigger.layers;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;

/**
 * This shows the credits and instructions for the game
 * @author Chris Wong
 *
 */
public class AboutLayer
{
	private Font titleFont;
	private FontMetrics fm;	

	/**
	 * Basic constructor for the AboutLayer
	 */
	public AboutLayer(){
		try
		{
			titleFont = Font.createFont(Font.TRUETYPE_FONT, this.getClass().getResourceAsStream("/res/Fonts/batmfa.ttf"));
		} catch (FontFormatException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}

	}
	/**
	 * Draws the information on the screen.
	 * @param g The graphics context to use.
	 */
	public void draw(Graphics g){
		g.setFont(titleFont.deriveFont(36f));
		fm = g.getFontMetrics();		
		int x = (570 - fm.stringWidth("About Trigger Gear")) / 2;
		g.setColor(Color.WHITE);
		g.drawString("About Trigger Gear", x, 200);
		
		g.setFont(titleFont.deriveFont(20f));
		fm = g.getFontMetrics();		
		x = (570 - fm.stringWidth("Created by:")) / 2;
		g.drawString("Created by:", x, 250);
		
		g.setFont(titleFont.deriveFont(16f));
		fm = g.getFontMetrics();	
		
		x = (570 - fm.stringWidth("Tan Nguyen")) / 2;
		g.drawString("Tan Nguyen", x, 300);
		
		
		x = (570 - fm.stringWidth("Philip Vaca")) / 2;
		g.drawString("Philip Vaca", x, 325);
		
		
		x = (570 - fm.stringWidth("Christopher Wong")) / 2;
		g.drawString("Christopher Wong", x, 350);
		
		x = (570 - fm.stringWidth("Arrow Keys: Move Ship")) / 2;
		g.drawString("Arrow Keys: Move Ship", x, 420);
		
		x = (570 - fm.stringWidth("Z: Fire Bullet")) / 2;
		g.drawString("Z: Fire Bullet", x, 440);
		
		x = (570 - fm.stringWidth("X: Fire Lazer")) / 2;
		g.drawString("X: Fire Lazer", x, 460);
		
		x = (570 - fm.stringWidth("Shift: Slow Ship Movement")) / 2;
		g.drawString("Shift: Slow Ship Movement", x, 480);
		
		x = (570 - fm.stringWidth("Press Enter to go back.")) / 2;
		g.drawString("Press Enter to go back.", x, 650);

	}
}
