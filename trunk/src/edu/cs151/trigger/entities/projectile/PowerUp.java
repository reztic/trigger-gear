package edu.cs151.trigger.entities.projectile;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import edu.cs151.trigger.entities.Entity;

/**
 * Some Entities spawn power ups when destroyed.  This determines its path and draws the power up
 * on screen.
 * @author Tan Nguyen
 *
 */
public class PowerUp implements Entity {

	private static BufferedImage[] frames;
	private double x;
	private double y;
	private double dx;
	private double dy;
	private boolean isTerminated;
	private int bounces;
	private int spawnTimer;
	
	/**
	 * Constructor that determines where the powerup will spawn
	 * @param x The x position
	 * @param y The y position
	 */
	public PowerUp(double x, double y){
		if(frames==null){
			frames = new BufferedImage[3];
			try {
				frames[0] = ImageIO.read(this.getClass().getResourceAsStream("/res/Powerup/powerup1.png"));
				frames[1] = ImageIO.read(this.getClass().getResourceAsStream("/res/Powerup/powerup2.png"));
				frames[2] = ImageIO.read(this.getClass().getResourceAsStream("/res/Powerup/powerup3.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.x = x;
		this.y = y;
		isTerminated = false;
		bounces = 0;
		spawnTimer=0;
		
		//Randomize in which direction the powerup will go when spawned
		Random rand = new Random();
		switch(rand.nextInt(4)){
		case 0:
			dx = 3;
			dy = 3;
			break;
		case 1:
			dx = 3;
			dy = -3;
			break;
		case 2:
			dx = -3;
			dy = 3;
			break;
		case 3:
			dx = -3;
			dy = -3;
			break;
		default:
			dx = 3;
			dy = 3;
			break;
		}
	}

	@Override
	public void draw(Graphics g) {
		int showFrame = spawnTimer%21;
		if(showFrame < 7){
			showFrame = 0;
		}
		else if(showFrame < 14){
			showFrame = 1;
		}
		else{
			showFrame = 2;
		}
		g.drawImage(frames[showFrame], (int)x, (int)y, null);
		spawnTimer++;
	}

	@Override
	public boolean collided(Entity obj) {
		return getBounds().intersects(obj.getBounds());
	}

	@Override
	public void updatePath() {
		x += dx;
		y += dy;
		if(bounces<4){
			if(y < 0 || y+frames[0].getHeight() > 800){
				dy = -dy;
				bounces++;
			}
			if(x < -10 || x+frames[0].getWidth() > 600){
				dx = -dx;
				bounces++;
			}
		}
		else{
			if(y < -10 || y > 800 || x < 0 || x > 600){
				isTerminated = true;
			}
		}
	}

	@Override
	public int getDamage() {
		return 0;
	}

	@Override
	public boolean isTerminated() {
		// TODO Auto-generated method stub
		return isTerminated;
	}

	@Override
	public Double getBounds() {
		return new Rectangle2D.Double(x, y, frames[0].getWidth(), frames[0].getHeight());
	}

	@Override
	public void recieveDamage(int damage) {
		isTerminated = true;
	}

}
