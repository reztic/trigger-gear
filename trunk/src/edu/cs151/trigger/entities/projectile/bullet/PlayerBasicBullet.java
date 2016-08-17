package edu.cs151.trigger.entities.projectile.bullet;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * The basic bullet fired by a player.
 * @author Tan Nguyen
 *
 */
public class PlayerBasicBullet extends BasicBullet {
	
	private static BufferedImage power1;
	private static BufferedImage power2;
	private static BufferedImage power3;
	private static BufferedImage power4;
	private static BufferedImage power5;

	
	private int bulletPower;

	/**
	 * The x and y coordinates represents the coordinate for the center of the bullet. This makes
	 * it easier to place bullets.
	 * @param xPosition The center x position of the bullet
	 * @param yPosition The center y position of the bullet
	 * @param path The type of path this bullet should take. 0 allows you to set your own path
	 * with the setPath() method.
	 * @param bulletSpeed The magnitude speed of the bullet.
	 * @param type There are 3 types, the basic, stronger, and strongest bullets.  The basic does 1 damage while strong does 2
	 * and strongest does 2 damage. Passing 1 gives you basic, 2 gives you strong, 3 gives you strongest
	 */
	public PlayerBasicBullet(double xPosition, double yPosition, int path,
			double bulletSpeed, int bulletPower){
		
		super(xPosition, yPosition, path, bulletSpeed);
		this.bulletPower = bulletPower;
		if(power1 == null || power2 == null || power3 == null || power4 == null || power5 == null){
			try 
			{
				
				power1 = ImageIO.read(this.getClass().getResourceAsStream("/res/Bullets/bullet.png"));
				power2 = ImageIO.read(this.getClass().getResourceAsStream("/res/Bullets/upgradedbullet.png"));
				power3 = ImageIO.read(this.getClass().getResourceAsStream("/res/Bullets/upgradedbullet2.png"));
				power4 = ImageIO.read(this.getClass().getResourceAsStream("/res/Bullets/upgradedbulletleft.png"));
				power5 = ImageIO.read(this.getClass().getResourceAsStream("/res/Bullets/upgradedbulletright.png"));

			} catch (IOException e) {
				//Invisible bullet
				power1 = new BufferedImage(5, 5, BufferedImage.TYPE_3BYTE_BGR);
				power2 = new BufferedImage(5, 5, BufferedImage.TYPE_3BYTE_BGR);
				power3 = new BufferedImage(5, 5, BufferedImage.TYPE_3BYTE_BGR);
				power4 = new BufferedImage(5, 5, BufferedImage.TYPE_3BYTE_BGR);
				power5 = new BufferedImage(5, 5, BufferedImage.TYPE_3BYTE_BGR);

			}
		}
	}

	@Override
	public void draw(Graphics g) {
		if(hasCollided()){
			BufferedImage bulletCollision = getBulletCollision();
			g.drawImage(bulletCollision , (int)x-(bulletCollision.getWidth()/2), (int)y-(bulletCollision.getWidth()/2), null);
			remove();
		}
		else{
			if(bulletPower==1)
				g.drawImage(power1 , (int)x-(power1.getWidth()/2), (int)y-(power1.getWidth()/2), null);
			else if(bulletPower==2)
				g.drawImage(power2 , (int)x-(power2.getWidth()/2), (int)y-(power2.getWidth()/2), null);
			else if(bulletPower==3)
				g.drawImage(power3 , (int)x-(power3.getWidth()/2), (int)y-(power3.getWidth()/2), null);
			else if(bulletPower==4)
				g.drawImage(power4 , (int)x-(power3.getWidth()/2), (int)y-(power3.getWidth()/2), null);
			else if(bulletPower==5)
				g.drawImage(power5 , (int)x-(power3.getWidth()/2), (int)y-(power3.getWidth()/2), null);
		}
	}
	
	@Override
	public int getDamage(){
		if(bulletPower==3||bulletPower==2 || bulletPower==4 || bulletPower ==5){
			return 2;
		}
		else 
			return 1;
	}


}
