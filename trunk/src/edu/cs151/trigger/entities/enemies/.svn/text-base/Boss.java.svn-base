package edu.cs151.trigger.entities.enemies;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import edu.cs151.trigger.entities.enemies.Popcorn.EnemyType;
import edu.cs151.trigger.entities.player.PlayerShip;
import edu.cs151.trigger.entities.projectile.bullet.BasicBullet;
import edu.cs151.trigger.entities.projectile.bullet.EnemyBasicBullet;
import edu.cs151.trigger.util.Path;
import edu.cs151.trigger.util.SoundPlayer;
import edu.cs151.trigger.util.Path.PathType;


/**
 * Boss class generates the attack patterns for the boss on level 1. It also stores the explosion frames.
 * @author Philip Vaca
 *
 */
public class Boss extends Enemy
{
	private double x;
	private double y;
	
	private int xOffset;
	private int yOffset;
	
	private int spawnTime;
	private int timeStamp;
	private BufferedImage img;
	private BossType type;
	private PathType pathType;
	private Random rand;
	
	//Might be less efficient this way.
	private static ArrayList<BufferedImage> explosionFrames = new ArrayList<BufferedImage>(18);
	private int currentExplosionFrame;
	
	private boolean showExplosion;
	private static BufferedImage boss1Image;

	
	public static enum BossType {
		BOSS1
	}
	/**
	 * Constructor for creating a boss
	 * @param xPosition the starting x position for the boss
	 * @param yPosition the starting y position for the boss
	 * @param bossType the type of boss
	 * @param pathType the pathing for the boss
	 */
	public Boss(int xPosition, int yPosition, BossType bossType, PathType pathType)
	{

		try 
		{
			boss1Image = ImageIO.read(this.getClass().getResourceAsStream("/res/BossSprites/miniboss.png"));

				for(int i = 0; i < 18; i++)
				{
					explosionFrames.add(ImageIO.read(this.getClass().getResourceAsStream("/res/explosions/Boss2Explosion/boss2explosion" + Integer.toString(i) + ".png")));
				}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		x = xPosition;
		y = yPosition;
		
		//The offset is used for updatePath() when using functions in calculating paths.
		xOffset = xPosition;
		yOffset = yPosition;
		
		showExplosion = false;
		img = boss1Image;
		hp = 1000;
		spawnTime = 0;
		type = bossType;
		
		
		this.pathType = pathType;
	}
	/**
	 * Draws the boss, draws the explosion frames when terminated.
	 */
	public void draw(Graphics g)
	{
		double percent = ((double)hp / 1000);
		g.setColor(Color.RED);
		g.fillRect(10, 10, (int) (percent * 570), 5);
		spawnTime++;
		if(!showExplosion)
			g.drawImage(img, (int)x, (int)y, null);
		else {
			if(currentExplosionFrame < 18){
				g.drawImage(explosionFrames.get(currentExplosionFrame), (int)((x+img.getWidth()/2)-(explosionFrames.get(currentExplosionFrame).getWidth()/2)), 
					(int)((y+img.getHeight()/2)-(explosionFrames.get(currentExplosionFrame).getHeight()/2)), null);
				if(timeStamp==7){
					currentExplosionFrame++;
					timeStamp = 0;
				}
				else
					timeStamp++;
			}
			if(currentExplosionFrame >= 18){
				setTermination();
			}
		}
		
	}
	/**
	 * Updates the path for the boss.
	 */
	public void updatePath() 
	{
		double previousX = x;

		if(!showExplosion){
			y = Path.getNextY(x, y, pathType, xOffset, yOffset, spawnTime, this);
			x = Path.getNextX(x, y, pathType, xOffset, yOffset, spawnTime, this);
		
			//5 is the tolerance level of when to turn
			if(x-previousX > 5){
				//Set it to draw the right bank
			}
			if(x-previousX < 5){
				//Set it to draw the left bank
			}
			else{
				//Set it to draw the normal position
			}
			
			fireProjectile();
			
		}
		
	}
	/**
	 * Returns the damage from the boss.
	 */
	public int getDamage() 
	{
		return 1;
	}
	/**
	 * Gets the rectangle around the boss.
	 */
	public Double getBounds()
	{
		if(showExplosion){
			return new Rectangle2D.Double(-100, -100, 0, 0);
		}
		return new Rectangle2D.Double(x, y, img.getWidth(), img.getHeight());
	}
	/**
	 * Gets the damage taken to the boss.
	 * @param damage the damage to the boss
	 */
	public void recieveDamage(int damage) 
	{
		hp -= damage;
		if(hp <= 0)
		{
			showExplosion = true;
			PlayerShip.getInstance().increaseScore(500);

			new Thread(new SoundPlayer("/res/soundeffects/bossexplosion.wav")).start();
			currentExplosionFrame = 0;
			timeStamp = 0;
		}
	}
	/**
	 * Creates the firing patterns for the boss
	 */
	public void fireProjectile() 
	{
		
		if(hp <= 350 && spawnTime%4 == 0)
		{
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-170, y+(img.getWidth()/2-135), BasicBullet.STRAIGHT_PATH_DOWN, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-160, y+(img.getWidth()/2-135), BasicBullet.STRAIGHT_PATH_DOWN, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-150, y+(img.getWidth()/2-135), BasicBullet.STRAIGHT_PATH_DOWN, 4));

			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+170, y+(img.getWidth()/2-135), BasicBullet.STRAIGHT_PATH_DOWN, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+160, y+(img.getWidth()/2-135), BasicBullet.STRAIGHT_PATH_DOWN, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+150, y+(img.getWidth()/2-135), BasicBullet.STRAIGHT_PATH_DOWN, 4));

			
		}
		if(hp <= 400 && spawnTime%20 == 0)
		{
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-15, y+(img.getWidth()/2-135), 0, 5));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-10, y+(img.getWidth()/2-135), 0, 5));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-20, y+(img.getWidth()/2-135), 0, 5));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-15, y+(img.getWidth()/2-145), 0, 5));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-15, y+(img.getWidth()/2-125), 0, 5));
			
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+15, y+(img.getWidth()/2-135), 0, 5));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+10, y+(img.getWidth()/2-135), 0, 5));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+20, y+(img.getWidth()/2-135), 0, 5));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+15, y+(img.getWidth()/2-145), 0, 5));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+15, y+(img.getWidth()/2-125), 0, 5));
			
			
		}
		else if(hp <= 450 && spawnTime%45 == 0)
		{
			
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2+60), y+(img.getWidth()/2-120), 0, 3));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+45, y+(img.getWidth()/2-120), 0, 3));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2+30), y+(img.getWidth()/2-120), 0, 3));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+15, y+(img.getWidth()/2-120), 0, 3));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2+60), y+(img.getWidth()/2-135), 0, 3));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+45, y+(img.getWidth()/2-135), 0, 3));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2+30), y+(img.getWidth()/2-135), 0, 3));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+15, y+(img.getWidth()/2-135), 0, 3));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2+60), y+(img.getWidth()/2-150), 0, 3));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+45, y+(img.getWidth()/2-150), 0, 3));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2+30), y+(img.getWidth()/2-150), 0, 3));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+15, y+(img.getWidth()/2-150), 0, 3));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2+60), y+(img.getWidth()/2-165), 0, 3));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+45, y+(img.getWidth()/2-165), 0, 3));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2+30), y+(img.getWidth()/2-165), 0, 3));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+15, y+(img.getWidth()/2-165), 0, 3));
			
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2-60), y+(img.getWidth()/2-120), 0, 3));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-45, y+(img.getWidth()/2-120), 0, 3));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2-30), y+(img.getWidth()/2-120), 0, 3));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-15, y+(img.getWidth()/2-120), 0, 3));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2-60), y+(img.getWidth()/2-135), 0, 3));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-45, y+(img.getWidth()/2-135), 0, 3));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2-30), y+(img.getWidth()/2-135), 0, 3));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-15, y+(img.getWidth()/2-135), 0, 3));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2-60), y+(img.getWidth()/2-150), 0, 3));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-45, y+(img.getWidth()/2-150), 0, 3));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2-30), y+(img.getWidth()/2-150), 0, 3));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-15, y+(img.getWidth()/2-150), 0, 3));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2-60), y+(img.getWidth()/2-165), 0, 3));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-45, y+(img.getWidth()/2-165), 0, 3));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2-30), y+(img.getWidth()/2-165), 0, 3));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-15, y+(img.getWidth()/2-165), 0, 3));
		}
		else if(hp <= 550 && spawnTime %30 == 0 && hp >= 400)
		{
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-15, y+(img.getWidth()/2-135), 0, 1));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-10, y+(img.getWidth()/2-135), 0, 1));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-20, y+(img.getWidth()/2-135), 0, 1));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-15, y+(img.getWidth()/2-145), 0, 1));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-15, y+(img.getWidth()/2-125), 0, 1));
			
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+15, y+(img.getWidth()/2-135), 0, 1));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+10, y+(img.getWidth()/2-135), 0, 1));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+20, y+(img.getWidth()/2-135), 0, 1));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+15, y+(img.getWidth()/2-145), 0, 1));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+15, y+(img.getWidth()/2-125), 0, 1));
		}
		else if(hp <= 600 && spawnTime%40 == 0)
		{
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2+60), y+(img.getWidth()/2-120), 0, 3));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+45, y+(img.getWidth()/2-120), 0, 3));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2+30), y+(img.getWidth()/2-120), 0, 3));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+15, y+(img.getWidth()/2-120), 0, 3));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2+60), y+(img.getWidth()/2-135), 0, 3));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+45, y+(img.getWidth()/2-135), 0, 3));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2+30), y+(img.getWidth()/2-135), 0, 3));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+15, y+(img.getWidth()/2-135), 0, 3));
			
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2-60), y+(img.getWidth()/2-120), 0, 3));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-45, y+(img.getWidth()/2-120), 0, 3));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2-30), y+(img.getWidth()/2-120), 0, 3));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-15, y+(img.getWidth()/2-120), 0, 3));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2-60), y+(img.getWidth()/2-135), 0, 3));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-45, y+(img.getWidth()/2-135), 0, 3));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2-30), y+(img.getWidth()/2-135), 0, 3));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-15, y+(img.getWidth()/2-135), 0, 3));
			
			
		}

		if(hp <= 900 && spawnTime%40 == 0 && hp >= 700)
		{
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-15, y+(img.getWidth()/2-135), 0, 1));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-10, y+(img.getWidth()/2-135), 0, 1));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-20, y+(img.getWidth()/2-135), 0, 1));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-15, y+(img.getWidth()/2-145), 0, 1));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-15, y+(img.getWidth()/2-125), 0, 1));
			
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+15, y+(img.getWidth()/2-135), 0, 1));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+10, y+(img.getWidth()/2-135), 0, 1));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+20, y+(img.getWidth()/2-135), 0, 1));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+15, y+(img.getWidth()/2-145), 0, 1));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+15, y+(img.getWidth()/2-125), 0, 1));
		}
		if(hp <= 950 && spawnTime%20 == 0 && hp >= 780)
		{
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-15, y+(img.getWidth()/2-135), 0, 3));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-15, y+(img.getWidth()/2-135), 0, 2));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-15, y+(img.getWidth()/2-135), 0, 1));
		}
		if(hp <= 1000 && spawnTime%40 == 0 && hp >= 400)
		{
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2+60), y+(img.getWidth()/2-100), 0, 7));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-60, y+(img.getWidth()/2-100), 0, 7));
			

		}
		
	}
}
