package edu.cs151.trigger.entities.enemies;

import edu.cs151.trigger.entities.Entity;
import edu.cs151.trigger.util.ProjectileListener;

/**
 * This is the abstract class used by all enemy types ranging from basic enemies to bosses.
 * @author Tan Nguyen
 *
 */
public abstract class Enemy implements Entity{
	/**
	 * This method can be called to check and see if an enemy flies off screen or gets destroyed.
	 * @return Returns true if enemy should no longer exist.
	 */
	public boolean isTerminated(){
		return isTerminated;
	}
	
	/**
	 * This method should be called to flag when an enemy flies off screen or gets killed.
	 */
	public void setTermination(){
		isTerminated = true;
	}
	
	/**
	 * Sets the ProjectileListener so when the enemy fires a projectile, the listener can be informed.
	 * @param pListener The ProjectileListner
	 */
	public void setProjectileListener(ProjectileListener pListener){
		listener = pListener;
	}
	
	/**
	 * Gets called when enemy fires their projectile.
	 */
	public abstract void fireProjectile();
	
	@Override
	public boolean collided(Entity obj) {
		return obj.getBounds().intersects(this.getBounds());
	}
	
	private boolean isTerminated = false;
	protected ProjectileListener listener;
	protected int hp;
}
