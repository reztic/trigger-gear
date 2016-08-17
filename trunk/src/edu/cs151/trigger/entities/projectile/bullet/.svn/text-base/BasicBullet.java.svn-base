package edu.cs151.trigger.entities.projectile.bullet;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import edu.cs151.trigger.entities.Entity;
/**
 * This is the underlying class for all projectiles used in the game.
 * @author Tan Nguyen
 *
 */
public class BasicBullet implements Entity{
	/*
	 * These static values are used to determine if the bullets go straight up, down, left, or right.
	 */
	public final static int STRAIGHT_PATH_UP = -1;
	public final static int STRAIGHT_PATH_DOWN = -2;
	public final static int STRAIGHT_PATH_LEFT = -3;
	public final static int STRAIGHT_PATH_RIGHT = -4;
	public final static int NO_PATH = 0;
	
	//Stores what kind of path the bullet has.  0 indicates no movement while any other value indicates
	//it is on a path.
	private int path;
	protected double x;
	protected double y;
	
	private double dx;
	private double dy;
	private double magnitude;
	
	protected static BufferedImage[] bulletCollisions;
	
	private boolean hasCollided;
	private boolean remove;
	
	/**
	 * Set the x and y coordinates of where the bullet will spawn.  You can select a pre-define path
	 * from the static variables or if you want to set your own path, set path to NO_PATH.
	 * @param xPosition The starting x position
	 * @param yPosition The starting y position
	 * @param path The path the bullet should take.
	 */
	public BasicBullet(double xPosition, double yPosition, int path, double bulletSpeed){
		x = xPosition;
		y = yPosition;
		magnitude = bulletSpeed;
		
		remove = false;
		
		if(bulletCollisions==null){
			try{
				bulletCollisions = new BufferedImage[3];
				bulletCollisions[0] = ImageIO.read(this.getClass().getResourceAsStream("/res/Bullets/bulletCollision.png"));
				bulletCollisions[1] = ImageIO.read(this.getClass().getResourceAsStream("/res/Bullets/bulletCollision1.png"));
				bulletCollisions[2] = ImageIO.read(this.getClass().getResourceAsStream("/res/Bullets/bulletCollision2.png"));
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		switch(path){
		case STRAIGHT_PATH_DOWN:
			dx=0;
			dy=magnitude;
			this.path = STRAIGHT_PATH_DOWN;
			break;
		case STRAIGHT_PATH_UP:
			dx=0;
			dy=-magnitude;
			this.path = STRAIGHT_PATH_UP;
			break;
		case STRAIGHT_PATH_LEFT:
			dx=-magnitude;
			dy=0;
			this.path = STRAIGHT_PATH_LEFT;
			break;
		case STRAIGHT_PATH_RIGHT:
			dx=magnitude;
			dy=0;
			this.path = STRAIGHT_PATH_RIGHT;
			break;
		default:
			dx=0;
			dy=0;
			this.path = 0;
			break;
		}
	}
	
	/**
	 * Here you can set which direction you want the bullet to fly to.  It will keep going in that direction until
	 * it reaches offscreen.
	 * @param xDestination The x destination of the where the bullet should travel to
	 * @param yDestination The y destination of the where the bullet should travel to
	 */
	public void setPath(int xDestination, int yDestination){
		//Moves straight up or down.
		if(x==xDestination){
			if (yDestination>y){
				dy=magnitude;
				dx=0;
			}
			else if (yDestination<y){
				dy=-magnitude;
				dx=0;
			}
			return;
		}
		if(yDestination>y){
			double theta = Math.atan2(yDestination-y, xDestination-x);
			dy = (magnitude*Math.sin(theta));
			dx = (magnitude*Math.cos(theta));
		}
		else{
			double theta = Math.atan2(xDestination-x, yDestination-y);
			dy = (magnitude*Math.cos(theta));
			dx = (magnitude*Math.sin(theta));
		}
		path = 1;
	}
	
	/**
	 * Determines whether a path has been set. If a path has not been set, it will just stay in place.
	 * @return True if a path is set, false otherwise.
	 */
	public boolean isPathSet(){
		return path!=0;
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.YELLOW);
		g.fillOval((int)x, (int)y, 5, 5);
	}


	@Override
	public boolean collided(Entity obj) {
		return obj.getBounds().intersects(this.getBounds());
	}

	@Override
	public void updatePath() {
		x += dx;
		y += dy;
	}

	@Override
	public int getDamage() {
		return 1;
	}

	@Override
	public boolean isTerminated() {
		return y<-5 || y > 805 || x <-5 || x > 605 || remove;
	}

	@Override
	public Double getBounds() {
		if(hasCollided){
			return new Rectangle2D.Double(-100, 300, 0, 0);
		}
		return new Rectangle2D.Double(x, y, 5, 5);
	}

	@Override
	public void recieveDamage(int damage) {
		hasCollided = true;
	}
	
	/**
	 * Determines if the bullet has collided with another Entity
	 * @return Returns true if it has collided with another Entity.
	 */
	public boolean hasCollided(){
		return hasCollided;
	}
	
	/**
	 * This method will set off the isTerminated() method.
	 */
	public void remove(){
		remove = true;
	}
	
	/**
	 * Randomly gets a bullet collision frame.
	 * @return An image of a bullet collision frame.
	 */
	public BufferedImage getBulletCollision(){
		return bulletCollisions[(int)System.currentTimeMillis()%3];
	}
	
}
