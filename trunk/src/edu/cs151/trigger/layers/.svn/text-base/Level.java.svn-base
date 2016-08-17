package edu.cs151.trigger.layers;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;


import edu.cs151.trigger.entities.Entity;

/**
 * This draws all the active Entities on screen.
 * @author Chris Wong
 *
 */
public class Level
{

	/**
	 * Initializes all the values.
	 * @param friends An ArrayList of all the friendly entities in the game.
	 * @param enemy An ArrayList of all the hostile entities in the game.
	 */
	public Level(ArrayList<Entity> friends, ArrayList<Entity> enemy)
	{
		onScreenEnemyEntites = friends;
		onScreenFriendlyEntites = enemy;

	}
	
	/**
	 * Draws the Entities on the screen.
	 * @param g The graphic context to use.
	 */
	public void draw(Graphics g)
	{
		Iterator<Entity> i = onScreenEnemyEntites.iterator();
		Iterator<Entity> h = onScreenFriendlyEntites.iterator();
		while(h.hasNext()){
			Entity f = (Entity) h.next();
			
			
			if(f.isTerminated()){
				h.remove();
			}
			f.draw(g);
			f.updatePath();
		}
		while (i.hasNext())
		{
			Entity e = (Entity) i.next();
		
			
			if (e.isTerminated())
			{
				i.remove();
			}
			
			e.draw(g);
			e.updatePath();
			
			
		}
	}
	


	private ArrayList<Entity> onScreenEnemyEntites;
	private ArrayList<Entity> onScreenFriendlyEntites;


}
