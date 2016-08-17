package edu.cs151.trigger.entities.projectile.bullet;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * The basic bullet an Enemy entity fires.
 * @author Tan Nguyen
 *
 */
public class EnemyBasicBullet extends BasicBullet {
	
	private static BufferedImage img; 

	/**
	 * The x and y coordinates represents the coordinate for the center of the bullet. This makes
	 * it easier to place bullets.
	 * @param xPosition The center x position of the bullet
	 * @param yPosition The center y position of the bullet
	 * @param path The type of path this bullet should take. 0 allows you to set your own path
	 * with the setPath() method.
	 * @param bulletSpeed The magnitude speed of the bullet.
	 */
	public EnemyBasicBullet(double xPosition, double yPosition, int path,
			double bulletSpeed){
		
		super(xPosition, yPosition, path, bulletSpeed);
		if(img == null){
			try {
				img = ImageIO.read(this.getClass().getResourceAsStream("/res/Bullets/enemybullet.png"));
				
			} catch (IOException e) {
				//Invisible bullet
				img = new BufferedImage(5, 5, BufferedImage.TYPE_3BYTE_BGR);
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
		else
			g.drawImage(img , (int)x-(img.getWidth()/2), (int)y-(img.getWidth()/2), null);
		
	}


}
