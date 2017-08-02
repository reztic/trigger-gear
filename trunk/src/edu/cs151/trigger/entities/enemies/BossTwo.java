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
 * Second level boss
 * @author Philip Vaca
 *
 */
public class BossTwo extends Enemy
{
	private double x;
	private double y;
	
	private int xOffset;
	private int yOffset;
	
	private int spawnTime;
	private int timeStamp;
	private BufferedImage img;
	private BossTwoType type;
	private PathType pathType;
	private Random rand;
	private int burstTime = 0;
	private int burstTime2 = 0;
	//Might be less efficient this way.
	private static ArrayList<BufferedImage> explosionFrames = new ArrayList<BufferedImage>(24);
	private int currentExplosionFrame;
	
	private boolean showExplosion;
	private static BufferedImage boss2Image;

	private static final SoundPlayer soundPlayer = new SoundPlayer();

	public static enum BossTwoType 
	{
		BOSS2
	}
	/**
	 * Constructs the level 2 boss
	 * @param xPosition the x posiition of the boss
	 * @param yPosition the y position of the boss
	 * @param bossType the type of boss
	 * @param pathType the pathing for the boss
	 */
	public BossTwo(int xPosition, int yPosition, BossTwoType bossType, PathType pathType)
	{

		try 
		{
			boss2Image = ImageIO.read(this.getClass().getResourceAsStream("/res/BossSprites/redboss2.png"));

				for(int i = 0; i < 25; i++)
				{
					explosionFrames.add(ImageIO.read(this.getClass().getResourceAsStream("/res/explosions/RedBossExplosion/boss2explosion" + Integer.toString(i) + ".png")));
				}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		img = boss2Image;
		x = xPosition;
		y = yPosition;
		
		//The offset is used for updatePath() when using functions in calculating paths.
		xOffset = xPosition;
		yOffset = yPosition;
		
		showExplosion = false;
		
		hp = 1300;
		spawnTime = 0;
		type = bossType;
		
		
		this.pathType = pathType;
	}
	/**
	 * Draws the boss and explosion animation
	 */
	public void draw(Graphics g)
	{
		double percent = ((double)hp / 1300);
		g.setColor(Color.RED);
		g.fillRect(10, 10, (int) (percent * 570), 5);
		spawnTime++;
		if(!showExplosion)
			g.drawImage(img, (int)x, (int)y, null);
		else {
			if(currentExplosionFrame < 24){
				g.drawImage(explosionFrames.get(currentExplosionFrame), (int)((x+img.getWidth()/2)-(explosionFrames.get(currentExplosionFrame).getWidth()/2)), 
					(int)((y+img.getHeight()/2)-(explosionFrames.get(currentExplosionFrame).getHeight()/2)), null);
				if(timeStamp==4){
					currentExplosionFrame++;
					timeStamp = 0;
				}
				else
					timeStamp++;
			}
			if(currentExplosionFrame >= 24){
				setTermination();
			}
		}
		
	}
	/**
	 * Updates the path of the boss
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
	 * Gets the damage from the boss
	 */
	public int getDamage() 
	{
		return 1;
	}
	/**
	 * Returns the bounds of the boss
	 * @return the bounds of the boss
	 */
	public Double getBounds()
	{
		if(showExplosion){
			return new Rectangle2D.Double(-100, -100, 0, 0);
		}
		return new Rectangle2D.Double(x, y, img.getWidth(), img.getHeight());
	}
	/**
	 * Gets the damage taken to the boss
	 * @param damage the damage to the boss
	 */
	public void recieveDamage(int damage) 
	{
		hp -= damage;
		if(hp <= 0)
		{
			showExplosion = true;
			PlayerShip.getInstance().increaseScore(500);
			soundPlayer.playSound("/res/soundeffects/bossexplosion.wav");
			currentExplosionFrame = 0;
			timeStamp = 0;
		}
	}
	/**
	 * The shooting pattern for the boss
	 */
	public void fireProjectile() 
	{
		burstTime++;
		burstTime2++;
		if(hp <= 140 && spawnTime%5 == 0)
		{
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+5, y+(img.getWidth()/2+50), 0, 1));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-5, y+(img.getWidth()/2+50), 0, 1));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+10, y+(img.getWidth()/2+50), 0, 1));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-10, y+(img.getWidth()/2+50), 0, 1));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+15, y+(img.getWidth()/2+50), 0, 1));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-15, y+(img.getWidth()/2+50), 0, 1));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+20, y+(img.getWidth()/2+50), 0, 1));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-20, y+(img.getWidth()/2+50), 0, 1));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2), y+(img.getWidth()/2+50), 0, 1));

			
		}
		if(hp <= 300 && spawnTime%30 == 0)
		{
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+120, y+(img.getWidth()/2-150), 0, 2));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+130, y+(img.getWidth()/2-160), 0, 2));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+110, y+(img.getWidth()/2-160), 0, 2));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+140, y+(img.getWidth()/2-170), 0, 2));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+100, y+(img.getWidth()/2-170), 0, 2));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+150, y+(img.getWidth()/2-180), 0, 2));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+90, y+(img.getWidth()/2-180), 0, 2));
		}
		if(hp <= 450 && spawnTime%2 == 0)
		{
			if(burstTime2 <= 70)
			{
				this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+120, y+(img.getWidth()/2-150), 0, 1));
				this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+120, y+(img.getWidth()/2-150), 0, 1));
				this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+120, y+(img.getWidth()/2-150), 0, 1));
				this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+120, y+(img.getWidth()/2-150), 0, 1));
				this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+120, y+(img.getWidth()/2-150), 0, 1));
				this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+120, y+(img.getWidth()/2-150), 0, 1));
				this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+120, y+(img.getWidth()/2-150), 0, 1));
				this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+120, y+(img.getWidth()/2-150), 0, 1));
				this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+120, y+(img.getWidth()/2-150), 0, 1));
				this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+120, y+(img.getWidth()/2-150), 0, 1));
				this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+120, y+(img.getWidth()/2-150), 0, 1));
				
				this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+130, y+(img.getWidth()/2-150), 0, 1));
				this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+130, y+(img.getWidth()/2-150), 0, 1));
				this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+130, y+(img.getWidth()/2-150), 0, 1));
				this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+130, y+(img.getWidth()/2-150), 0, 1));
				this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+130, y+(img.getWidth()/2-150), 0, 1));
				this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+130, y+(img.getWidth()/2-150), 0, 1));
				this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+130, y+(img.getWidth()/2-150), 0, 1));
				this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+130, y+(img.getWidth()/2-150), 0, 1));
				this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+130, y+(img.getWidth()/2-150), 0, 1));
				this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+130, y+(img.getWidth()/2-150), 0, 1));
				this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+130, y+(img.getWidth()/2-150), 0, 1));
				
				
				
			}
			else if(burstTime2 > 130)
			{
				burstTime2 = 0;
			}
			
		}

		if(hp <= 550 && spawnTime%2 == 0)
		{
			if(burstTime <= 80)
			{
				this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-120, y+(img.getWidth()/2-150), 0, 2));
				this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-120, y+(img.getWidth()/2-150), 0, 2));
				this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-120, y+(img.getWidth()/2-150), 0, 2));
				this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-120, y+(img.getWidth()/2-150), 0, 2));
				this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-120, y+(img.getWidth()/2-150), 0, 2));
				this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-120, y+(img.getWidth()/2-150), 0, 2));
				this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-120, y+(img.getWidth()/2-150), 0, 2));
				this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-120, y+(img.getWidth()/2-150), 0, 2));
				
				this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-120, y+(img.getWidth()/2-160), 0, 2));
				this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-120, y+(img.getWidth()/2-160), 0, 2));
				this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-120, y+(img.getWidth()/2-160), 0, 2));
				this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-120, y+(img.getWidth()/2-160), 0, 2));
				this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-120, y+(img.getWidth()/2-160), 0, 2));
				this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-120, y+(img.getWidth()/2-160), 0, 2));
				this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-120, y+(img.getWidth()/2-160), 0, 2));
				this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-120, y+(img.getWidth()/2-160), 0, 2));
				
				
				this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-120, y+(img.getWidth()/2-170), 0, 2));
				this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-120, y+(img.getWidth()/2-170), 0, 2));
				this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-120, y+(img.getWidth()/2-170), 0, 2));
				this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-120, y+(img.getWidth()/2-170), 0, 2));
				this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-120, y+(img.getWidth()/2-170), 0, 2));
				this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-120, y+(img.getWidth()/2-170), 0, 2));
				this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-120, y+(img.getWidth()/2-170), 0, 2));
				this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-120, y+(img.getWidth()/2-170), 0, 2));
				
				this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-120, y+(img.getWidth()/2-180), 0, 2));
				this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-120, y+(img.getWidth()/2-180), 0, 2));
				this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-120, y+(img.getWidth()/2-180), 0, 2));
				this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-120, y+(img.getWidth()/2-180), 0, 2));
				this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-120, y+(img.getWidth()/2-180), 0, 2));
				this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-120, y+(img.getWidth()/2-180), 0, 2));
				this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-120, y+(img.getWidth()/2-180), 0, 2));
				this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-120, y+(img.getWidth()/2-180), 0, 2));
			}
			else if(burstTime > 170)
			{
				burstTime = 0;
			}
			
		}
		else if(hp <= 750 && spawnTime%40 == 0)
		{
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-120, y+(img.getWidth()/2-150), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-130, y+(img.getWidth()/2-150), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-140, y+(img.getWidth()/2-150), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-150, y+(img.getWidth()/2-150), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-160, y+(img.getWidth()/2-150), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-170, y+(img.getWidth()/2-150), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-180, y+(img.getWidth()/2-150), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-190, y+(img.getWidth()/2-150), 0, 4));
			
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-120, y+(img.getWidth()/2-160), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-130, y+(img.getWidth()/2-160), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-140, y+(img.getWidth()/2-160), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-150, y+(img.getWidth()/2-160), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-160, y+(img.getWidth()/2-160), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-170, y+(img.getWidth()/2-160), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-180, y+(img.getWidth()/2-160), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-190, y+(img.getWidth()/2-160), 0, 4));
			
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-120, y+(img.getWidth()/2-170), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-130, y+(img.getWidth()/2-170), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-140, y+(img.getWidth()/2-170), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-150, y+(img.getWidth()/2-170), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-160, y+(img.getWidth()/2-170), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-170, y+(img.getWidth()/2-170), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-180, y+(img.getWidth()/2-170), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-190, y+(img.getWidth()/2-170), 0, 4));

			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-120, y+(img.getWidth()/2-180), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-130, y+(img.getWidth()/2-180), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-140, y+(img.getWidth()/2-180), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-150, y+(img.getWidth()/2-180), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-160, y+(img.getWidth()/2-180), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-170, y+(img.getWidth()/2-180), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-180, y+(img.getWidth()/2-180), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-190, y+(img.getWidth()/2-180), 0, 4));
			

			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+120, y+(img.getWidth()/2-150), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+130, y+(img.getWidth()/2-150), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+140, y+(img.getWidth()/2-150), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+150, y+(img.getWidth()/2-150), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+160, y+(img.getWidth()/2-150), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+170, y+(img.getWidth()/2-150), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+180, y+(img.getWidth()/2-150), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+190, y+(img.getWidth()/2-150), 0, 4));
			
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+120, y+(img.getWidth()/2-160), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+130, y+(img.getWidth()/2-160), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+140, y+(img.getWidth()/2-160), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+150, y+(img.getWidth()/2-160), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+160, y+(img.getWidth()/2-160), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+170, y+(img.getWidth()/2-160), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+180, y+(img.getWidth()/2-160), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+190, y+(img.getWidth()/2-160), 0, 4));
			
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+120, y+(img.getWidth()/2-170), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+130, y+(img.getWidth()/2-170), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+140, y+(img.getWidth()/2-170), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+150, y+(img.getWidth()/2-170), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+160, y+(img.getWidth()/2-170), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+170, y+(img.getWidth()/2-170), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+180, y+(img.getWidth()/2-170), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+190, y+(img.getWidth()/2-170), 0, 4));
			
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+120, y+(img.getWidth()/2-180), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+130, y+(img.getWidth()/2-180), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+140, y+(img.getWidth()/2-180), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+150, y+(img.getWidth()/2-180), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+160, y+(img.getWidth()/2-180), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+170, y+(img.getWidth()/2-180), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+180, y+(img.getWidth()/2-180), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+190, y+(img.getWidth()/2-180), 0, 4));
			
			
		}
		if(hp <= 1050 && spawnTime%35 == 0 && hp >= 250)
		{
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-100, y+(img.getWidth()/2-80), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-110, y+(img.getWidth()/2-80), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-120, y+(img.getWidth()/2-80), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-130, y+(img.getWidth()/2-80), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-140, y+(img.getWidth()/2-80), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-150, y+(img.getWidth()/2-80), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-160, y+(img.getWidth()/2-80), 0, 4));
			
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-100, y+(img.getWidth()/2-90), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-110, y+(img.getWidth()/2-90), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-120, y+(img.getWidth()/2-90), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-130, y+(img.getWidth()/2-90), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-140, y+(img.getWidth()/2-90), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-150, y+(img.getWidth()/2-90), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-160, y+(img.getWidth()/2-90), 0, 4));
			
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+100, y+(img.getWidth()/2-80), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+110, y+(img.getWidth()/2-80), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+120, y+(img.getWidth()/2-80), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+130, y+(img.getWidth()/2-80), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+140, y+(img.getWidth()/2-80), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+150, y+(img.getWidth()/2-80), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+160, y+(img.getWidth()/2-80), 0, 4));
			
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+100, y+(img.getWidth()/2-90), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+110, y+(img.getWidth()/2-90), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+120, y+(img.getWidth()/2-90), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+130, y+(img.getWidth()/2-90), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+140, y+(img.getWidth()/2-90), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+150, y+(img.getWidth()/2-90), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+160, y+(img.getWidth()/2-90), 0, 4));
		}
		if(hp <= 1200 && spawnTime%40 == 0)
		{
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-60, y+(img.getWidth()/2-30), 0, 5));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+60, y+(img.getWidth()/2-30), 0, 5));
		}
		if(hp <= 1250&& spawnTime%40 == 0 && hp >= 300)
		{
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-120, y+(img.getWidth()/2-150), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-130, y+(img.getWidth()/2-160), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-110, y+(img.getWidth()/2-160), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-140, y+(img.getWidth()/2-170), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-100, y+(img.getWidth()/2-170), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-150, y+(img.getWidth()/2-180), 0, 4));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-90, y+(img.getWidth()/2-180), 0, 4));
			
			
			
		}
		if(hp <= 1300 && spawnTime%60 == 0 && hp > 800)
		{
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-60, y+(img.getWidth()/2-30), BasicBullet.STRAIGHT_PATH_DOWN, 3));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)-60, y+(img.getWidth()/2-30), BasicBullet.STRAIGHT_PATH_DOWN, 4));
			
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+60, y+(img.getWidth()/2-30), BasicBullet.STRAIGHT_PATH_DOWN, 3));
			this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2)+60, y+(img.getWidth()/2-30), BasicBullet.STRAIGHT_PATH_DOWN, 4));
			
			
		}
		
	}
}
