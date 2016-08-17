package edu.cs151.trigger.layers;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JPanel;

import edu.cs151.trigger.entities.Entity;
import edu.cs151.trigger.entities.player.PlayerShip;
/**
 * This is an UI layer for displaying information about the ship and score.
 * @author Chris Wong
 *
 */
public class UILayer
{
	private PlayerShip ps;
	private Font liveFont;
	private FontMetrics fm;
	private String message;
	private int state;
	private float alpha;
	private int alphaMessage = 255;
	public final static int FADE_IN = 1;
	public final static int FADE_OUT = 2;
	public final static int BLANK_SCREEN = 3;
	public final static int NORMAL = 0;
/**
 * Constructor for the UIlayer
 */
	public UILayer()
	{
		///this.setFocusable(true); 
		//this.setFocusTraversalKeysEnabled(false);
		//this.addKeyListener(this);
		this.ps = PlayerShip.getInstance();
		try
		{
			liveFont = Font.createFont(Font.TRUETYPE_FONT, this.getClass().getResourceAsStream("/res/Fonts/batmfa.ttf"));
		} catch (FontFormatException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		state = 0;

	}
	/**
	 * Draws the UI on screen
	 * @param g Graphics
	 */
	public void draw(Graphics g)
	{
		
	    Graphics2D g2 = (Graphics2D) g;

		g2.setFont(liveFont.deriveFont(16f));
		g2.setColor(new Color(255,255,255));
		g2.drawString("Lives : " + ps.getLives(), 500, 760);
		
		g2.setFont(liveFont.deriveFont(16f));
		g2.setColor(new Color(255,255,255));
		g2.drawString("Score : " + ps.getScore(), 10, 760);
		
		//Draw onscreen messages
		//System.out.println(message);

		if(message != null && alphaMessage > 0 && state == NORMAL){
			g2.setFont(liveFont.deriveFont(32f));
			FontMetrics fm = g2.getFontMetrics();
			int x = (550 - fm.stringWidth(message)) / 2;
			g2.setColor(new Color(255,255,255,alphaMessage));
			g2.drawString(message, x , 260);
			alphaMessage -= 1;
		}
		
		//Fade out
		if(state==FADE_OUT){

			g.setColor(new Color(0, 0, 0, alpha));
			g.fillRect(0, 0, 600, 800);
			alpha += .005;
			if(alpha>=1){
				state = BLANK_SCREEN;
			}
			return;
		}
		//Fade in
		if(state==FADE_IN){
			g.setColor(new Color(0, 0, 0, alpha));
			g.fillRect(0, 0, 600, 800);
			alpha -= .005;
			if(alpha<=0){
				state = NORMAL;
			}
			return;
		}
		if(state == BLANK_SCREEN){
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, 600, 800);
		}
		

	}
	
	/**
	 * Trigger a Fade in effect.
	 */
	public void drawFadeIn(){
		state = FADE_IN;
		alpha = 1;
	}
	/**
	 * Sets the current message to display/
	 */
	public void setMessage(String Message){
		message = Message;
		alphaMessage = 255;
	}
	/**
	 * Returns the alpha setting
	 * @return the alpha setting
	 */
	public int alphaMessage(){
		return alphaMessage;
	}
	/**
	 * Trigger a Fade out effect.
	 */
	public void drawFadeOut(){
		state = FADE_OUT;
		alpha = 0;
	}
	
	
	/**
	 * Returns the state the UILayer is in.
	 * @return Returns the value of either FADE_IN, FADE_OUT, BLANK_SCREEN, or NORMAL.
	 */
	public int getState(){
		return state;
	}
}

