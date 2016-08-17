package edu.cs151.trigger.entities;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

/**
 * All enemies, player, projectiles, and bosses are Entities.  Entities are objects that interact
 * with each other on screen.
 * @author Tan Nguyen
 *
 */
public interface Entity {
	/**
	 * Used to draw the Entity on screen.
	 * @param g The graphics context to be used.
	 */
	public void draw(Graphics g);
	/**
	 * Determines if one entity collides with another.
	 * @param obj The other Entity to detect the collision with.
	 * @return Returns true if the Entity collides with this Entity.
	 */
	public boolean collided(Entity obj);
	/**
	 * This method is mostly used by Entites not controlled by the player.  This updates its path.
	 */
	public void updatePath();
	/**
	 * When an Entity collides with another Entity, this method determines how much damage this
	 * Entity causes to the other Entity.
	 * @return The amount of damage cause upon collision.
	 */
	public int getDamage();
	/**
	 * Determines if the Entity is no longer needed on screen.
	 * @return Returns true if the Entity is no longer needed.
	 */
	public boolean isTerminated();
	/**
	 * Gets the boundary for the hit box of this Entity 
	 * @return The boundary of the hit box
	 */
	public Rectangle2D.Double getBounds();
	/**
	 * This is to set how much damage this Entity receives, for instance when one Entity
	 * collides with another.
	 * @param damage The amount of damage this Entity receives.
	 */
	public void recieveDamage(int damage);
}
