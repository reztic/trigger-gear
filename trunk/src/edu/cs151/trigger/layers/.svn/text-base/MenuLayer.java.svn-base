package edu.cs151.trigger.layers;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;

import edu.cs151.trigger.util.MusicPlayer;

/**
 * This is an menu item used by the MenuLayer
 * @author Chris Wong
 *
 */
final class MenuItem{
	int x;
	int y;
	int selected; // 1 is selected
	String menu;
	FontMetrics fm;	
	Font menuFont;
	
	/**
	 * Creates a menu item for the menu screen.
	 * @param y The y position of where to place the menu item.
	 * @param menu The name of the menu item.
	 * @param selected Determines if this item is currently selected.
	 */
	public MenuItem(int y, String menu, int selected)
	{
		this.x = x;
		this.y = y;
		this.selected = selected;
		this.menu = menu;
		try
		{
			menuFont = Font.createFont(Font.TRUETYPE_FONT, this.getClass().getResourceAsStream("/res/Fonts/clean.ttf"));
		} catch (FontFormatException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}

	}
	
	/**
	 * Draws the menu item on screen.
	 * @param g The graphic context to use.
	 */
	public void draw(Graphics g){
		
		//Generate menu
		g.setFont(menuFont.deriveFont(26f));
		fm = g.getFontMetrics();		
		int x = (570 - fm.stringWidth(menu)) / 2;
		g.setColor(Color.WHITE);
		if(selected == 1){
			g.fillRect(x - 25, y, 15, 15);
		}
		
		g.drawString(menu, x, y + 15);
		if(selected == 1){
			g.fillRect(x + fm.stringWidth(menu) + 10, y, 15, 15);
		}
	}
	/**
	 * Set this menuitem as being currently selected
	 */
	public void setSelected(){
		selected = 1;
	}
	/**
	 * Set this menuitem as not being currently selected.
	 */
	public void removeSelected(){
		selected = 0;

	}
}

/**
 * This is displayed at the beginning of the game. Consist of the title screen and various menu options
 * @author Chris Wong
 *
 */
public class MenuLayer
{
	MenuItem[] menus = new MenuItem[3];
	MenuItem title;
	Font titleFont;
	
	int currentIndex;
	private MusicPlayer mp;
	/**
	 * Constructs the menu setting "Start" as the currently selected menu item.
	 */
	public MenuLayer()
	{
		mp = new MusicPlayer("/res/Music/Intro.mid");
		mp.start();
		try {
			titleFont = Font.createFont(Font.TRUETYPE_FONT, this.getClass().getResourceAsStream("/res/Fonts/batmfa.ttf"));
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		menus[0] = new MenuItem(500,"Start", 1);
		menus[1] = new MenuItem(540,"About", 0);
		menus[2] = new MenuItem(580,"Quit", 0);
		title = new MenuItem(100, "Trigger Gear", 0);
		currentIndex = 0;
	}
	
	/**
	 * Changes the selected menu item when the player pushes down.
	 */
	public void moveDown(){
		if(currentIndex < menus.length - 1){
			menus[currentIndex].removeSelected();
			currentIndex++;
			menus[currentIndex].setSelected();

		}
	}
	
	/**
	 * Changes the selected menu item when the player pushes up.
	 */
	public void moveUp(){
		if(currentIndex > 0){
			menus[currentIndex].removeSelected();
			
			currentIndex--;
			menus[currentIndex].setSelected();

		}
	}
	
	/**
	 * Gets the index of the currently selected menu item.
	 * @return The index of the selected menu item.
	 */
	public int getCurrentIndex(){
		return currentIndex;
	}
	
	/**
	 * Draws the menu with all the menu items.
	 * @param g The graphic context to use.
	 */
	public void draw(Graphics g){
		for(MenuItem m:menus){
			m.draw(g);
		}
		
		g.setFont(titleFont.deriveFont(48f));
		FontMetrics fm = g.getFontMetrics();		
		int x = (570 - fm.stringWidth("Trigger Gear")) / 2;
		g.setColor(Color.WHITE);
		
		
		g.drawString("Trigger Gear", x, 175);
	}

	/**
	 * Stops the menu music.
	 */
	public void stopMusic()
	{
		mp.stop();
		
	}
}
