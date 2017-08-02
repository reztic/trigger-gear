package edu.cs151.trigger.entities.enemies;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import edu.cs151.trigger.entities.player.PlayerShip;
import edu.cs151.trigger.entities.projectile.PowerUp;
import edu.cs151.trigger.entities.projectile.bullet.BasicBullet;
import edu.cs151.trigger.entities.projectile.bullet.EnemyBasicBullet;
import edu.cs151.trigger.util.Path;
import edu.cs151.trigger.util.SoundPlayer;
import edu.cs151.trigger.util.Path.PathType;

/**
 * The basic enemies in the game.  The term popcorn refers to enemies that have low hitpoints and are not
 * considered a big threat by the player.
 * @author Tan Nguyen
 *
 */
public class Popcorn extends Enemy{

	private double x;
	private double y;
	
	private int xOffset;
	private int yOffset;
	
	private int spawnTime;
	private int timeStamp;
	private BufferedImage img;
	private EnemyType type;
	private PathType pathType;
	private Random rand;
	private boolean hasPowerUp;

	private static BufferedImage popcorn1Image;
	private static BufferedImage popcorn2Image;
	private static BufferedImage popcorn3Image;
	
	private static ArrayList<BufferedImage> explosionFrames = new ArrayList<BufferedImage>(8);
	private int currentExplosionFrame;
	
	private boolean showExplosion;

	private static final SoundPlayer soundPlayer = new SoundPlayer();

	/**
	 * There are 3 types of POPCORN in each level. As of now there are 2 set of enemies.
	 * @author Tan Nguyen
	 *
	 */
	public static enum EnemyType {
		POPCORN1, POPCORN2, POPCORN3
	}
	
	/**
	 * Constructs a popcorn Entity of different paths and difficulty
	 * @param xPosition The x position of where the popcorn spawn
	 * @param yPosition The y position of where the popcorn spawn
	 * @param enemyType The type of enemy to spawn
	 * @param pathType The path the enemy should take
	 * @param hasPowerUp Determines if this enemy carries a power up. It will spawn when the enemy gets destroyed by the player
	 */
	public Popcorn(int xPosition, int yPosition, EnemyType enemyType, PathType pathType, boolean hasPowerUp){
		x = xPosition;
		y = yPosition;
		this.hasPowerUp = hasPowerUp;
		
		//The offset is used for updatePath() when using functions in calculating paths.
		xOffset = xPosition;
		yOffset = yPosition;
		
		showExplosion = false;
		
		hp = 5;
		spawnTime = 0;
		type = enemyType;
		
		rand = new Random();
		
		switch(enemyType){
		case POPCORN1:
			img = popcorn1Image;
			break;
		case POPCORN2:
			img = popcorn2Image;
			break;
		case POPCORN3:
			img = popcorn3Image;
			break;
		default:
			img = popcorn1Image;
			break;
		}
		this.pathType = pathType;
		
	}

	/**
	 * Draws the enemy on screen.
	 * @param g The graphic context to use to draw the enemy.
	 */
	@Override
	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		spawnTime++;
		if(!showExplosion)
			g.drawImage(img, (int)x, (int)y, null);
		else {
			if(currentExplosionFrame < 8){
				g.drawImage(explosionFrames.get(currentExplosionFrame), (int)((x+img.getWidth()/2)-(explosionFrames.get(currentExplosionFrame).getWidth()/2)), 
					(int)((y+img.getHeight()/2)-(explosionFrames.get(currentExplosionFrame).getHeight()/2)), null);
				if(timeStamp==3){
					currentExplosionFrame++;
					timeStamp = 0;
				}
				else
					timeStamp++;
			}
			if(currentExplosionFrame >= 8){
				if(!isTerminated()&&hasPowerUp){
					listener.addProjectile(new PowerUp(x, y));
				}
				setTermination();
			}
		}
	}


	/**
	 * Updates the enemies' path depending on which Path he was set with
	 */
	@Override
	public void updatePath() {

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
	 * When an opposing Entity collides with this ship, this returns the amount of damage
	 * this enemy does to the other Entity.
	 * @return The amount of damage done.
	 */
	@Override
	public int getDamage() {
		return 1;
	}

	/**
	 * Determines when to fire its bullets.
	 */
	@Override
	public void fireProjectile() {
		switch(type){
		case POPCORN1:
			if(spawnTime%40==0 && (rand.nextInt(100) > 30) && y < 420){
				this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2), y+(img.getWidth()/2), BasicBullet.STRAIGHT_PATH_DOWN, 6));
			}
			break;
		case POPCORN2:
			if(spawnTime%40==0 && (rand.nextInt(100) > 30) && y < 420){
				this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2), y+(img.getWidth()/2), 0, 3));
			}
			break;
		case POPCORN3:
			if(spawnTime%40==0 && (rand.nextInt(100) > 30) && y < 420){
				this.listener.addProjectile(new EnemyBasicBullet(x+(img.getWidth()/2), y+(img.getWidth()/2), BasicBullet.STRAIGHT_PATH_DOWN, 5));
				EnemyBasicBullet left = new EnemyBasicBullet(x+(img.getWidth()/2), y+(img.getWidth()/2), BasicBullet.NO_PATH, 5);
				EnemyBasicBullet right = new EnemyBasicBullet(x+(img.getWidth()/2), y+(img.getWidth()/2), BasicBullet.NO_PATH, 5);
				left.setPath((int)x+(img.getWidth()/2)-5, (int)y+(img.getWidth()/2)+10);
				right.setPath((int)x+(img.getWidth()/2)+5, (int)y+(img.getWidth()/2)+10);
				this.listener.addProjectile(left);
				this.listener.addProjectile(right);
			}
			break;
		default:
				break;
		}
	}

	/**
	 * Gets the boundary of the popcorn's hitbox.
	 * @return Returns the boundary.
	 */
	@Override
	public Double getBounds() {
		//This way they can't hit the enemy if it is already exploding
		if(showExplosion){
			return new Rectangle2D.Double(-100, -100, 0, 0);
		}
		return new Rectangle2D.Double(x, y, img.getWidth(), img.getHeight());
	}

	/**
	 * When an opposing Entity collides with this enemy, you set the damage it should recieve here
	 * @param The damage this enemy should receive
	 */
	@Override
	public void recieveDamage(int damage)
	{
		hp -= damage;
		if(hp <= 0){
			showExplosion = true;
			currentExplosionFrame = 0;
			timeStamp = 0;
			PlayerShip.getInstance().increaseScore(50);
			soundPlayer.playSound("/res/soundeffects/explosion.wav");
		}
		
	}
	
	/**
	 * Loads the different sets of enemies.
	 * @param loadSet Which set to load.
	 * @throws IOException
	 */
	public static void loadEnemySet(int loadSet) throws IOException{
		popcorn1Image = ImageIO.read(Popcorn.class.getResourceAsStream("/res/enemySet/"+loadSet+"/type1.png"));
	
		popcorn2Image = ImageIO.read(Popcorn.class.getResourceAsStream("/res/enemySet/"+loadSet+"/type2.png"));

		popcorn3Image = ImageIO.read(Popcorn.class.getResourceAsStream("/res/enemySet/"+loadSet+"/type3.png"));
	
		if(explosionFrames.size()==0){
			for(int i = 0; i < 8; i++){
				explosionFrames.add(ImageIO.read(Popcorn.class.getResourceAsStream("/res/explosions/explosion" + Integer.toString(i) + ".png")));
			}
		}
		
	}
	

}
