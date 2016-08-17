package edu.cs151.trigger.entities.projectile.bullet;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * This is the player's secondary firing mode.
 * @author Philip Vaca
 *
 */
public class PlayerBasicLaser extends BasicBullet
{
	private static BufferedImage laser1;
	private int laserPower;
	
	/**
	 * The default constructor. 
	 * @param xPosition The x position of where to draw the laser
	 * @param yPosition The y position of where to draw the laser
	 * @param path The direction of the laser
	 * @param bulletSpeed The speed of the laser
	 * @param bulletPower The damage the laser does upon collision
	 */
	public PlayerBasicLaser(double xPosition, double yPosition, int path,
			double bulletSpeed, int bulletPower){

		super(xPosition, yPosition, path, bulletSpeed);
		this.laserPower = 1;
		try{
			laser1 = ImageIO.read(this.getClass().getResourceAsStream("/res/Bullets/laser2.png"));
		}
		catch (IOException e)
		{
			laser1 = new BufferedImage(5, 5, BufferedImage.TYPE_3BYTE_BGR);
		}
	}
	/**
	 * Draws the laser
	 */
	public void draw(Graphics g) {
		if(hasCollided()){
			BufferedImage bulletCollision = getBulletCollision();
			g.drawImage(bulletCollision , (int)x-(bulletCollision.getWidth()/2), (int)y-(bulletCollision.getWidth()/2), null);
			remove();
		}
		else{
			if(laserPower==1)
				g.drawImage(laser1, (int)x-(laser1.getWidth()/2), (int)y-(laser1.getWidth()/2) - 60, null);
			
		}
	}
	/**
	 * Gets the damage from the laser
	 * @return 1 the amount of damage
	 */
	public int getDamage()
	{
		return 1;
	}
}