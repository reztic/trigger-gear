package edu.cs151.trigger.entities.player;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import edu.cs151.trigger.entities.Entity;
import edu.cs151.trigger.entities.projectile.PowerUp;
import edu.cs151.trigger.entities.projectile.bullet.BasicBullet;
import edu.cs151.trigger.entities.projectile.bullet.PlayerBasicBullet;
import edu.cs151.trigger.entities.projectile.bullet.PlayerBasicLaser;
import edu.cs151.trigger.util.ProjectileListener;
import edu.cs151.trigger.util.SoundPlayer;

/**
 * This is the Entity the player controls in the game. It is a Singleton class and in order to
 * get an instantiation of this object, you must use getInstance().
 * @author Chris Wong
 *
 */
public class PlayerShip implements Entity{

	//Deals with the drawing of the player on screen
	private BufferedImage img;
	private int x;
	private int y;
	private int shipWidth;
	private int shipHeight;

	//Used when a player just died and he is spawning back in.
	private boolean deathSequence;
	private int deathTimer;
	
	//This is used during level transitions
	private boolean disableControl;
	
	//Basic player information
	private int score;
	private int hp;
	private int lives;
	private int powerLevel;
	private int laserLevel;
	private ProjectileListener pListener;

	//This is a singleton class.
	private static PlayerShip playerInstance;

	private static ArrayList<BufferedImage> explosionFrames = new ArrayList<BufferedImage>(8);

	private static final SoundPlayer soundPlayer = new SoundPlayer();

	/**
	 * Gets the Singleton object of this class.
	 * @return The reference to the Singleton
	 */
	public static PlayerShip getInstance(){
		if(playerInstance == null){
			playerInstance = new PlayerShip();
		}
		return playerInstance;
	}
	
	/**
	 * The basic constructor.
	 */
	private PlayerShip(){
		disableControl = false;
		powerLevel = 1;
		laserLevel = 0;
		hp = 1;
		lives = 5;
		score = 0;
		deathSequence = false;
		y = 600;
		x = 200;
		try {
			img = ImageIO.read(this.getClass().getResourceAsStream("/res/playersprite.png"));
			shipWidth = img.getWidth();
			shipHeight = img.getHeight();
			if(explosionFrames.size()==0){
				for(int i = 0; i < 8; i++){
					explosionFrames.add(ImageIO.read(this.getClass().getResourceAsStream("/res/explosions/explosion" + Integer.toString(i) + ".png")));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}


	}

	/**
	 * Draws the player ship.
	 */
	public void draw(Graphics g) {
		if(deathSequence){
			if(deathTimer < 24){
				int explosionFrameIndex = deathTimer/3;
				g.drawImage(explosionFrames.get(explosionFrameIndex), ((x+img.getWidth()/2)-(explosionFrames.get(explosionFrameIndex).getWidth()/2)), 
						((y+img.getHeight()/2)-(explosionFrames.get(explosionFrameIndex).getHeight()/2)), null);
				if(deathTimer==23){
					if(powerLevel > 1){
						pListener.addProjectile(new PowerUp(x, y));
					}
					if(powerLevel > 4){
						pListener.addProjectile(new PowerUp(x, y));
					}
					powerLevel = 1;
					x = 200;
					y = 800;
				}
			}
			else if(deathTimer<400){
				if(deathTimer%20<10){
					g.drawImage(img, x, y, null);
				}

				if(deathTimer%2==0&&deathTimer<300&&y>720-img.getHeight())
					y -= 1;
			}
			else{
				deathSequence = false;
			}
			deathTimer++;
		}
		else
			g.drawImage(img, x, y, null);
	}

	/**
	 * Determines if Player has collided with another ship.
	 */
	public boolean collided(Entity obj) {
		return obj.getBounds().intersects(this.getBounds());
	}

	@Override
	public void updatePath() {}

	/**
	 * Moves the ship up at a certain speed.
	 * @param speed How fast the ship moves up.
	 */
	public void moveUp(int speed){
		if(disableControl){
			return;
		}
		if(y > 0)
			y -= speed;
	}
	
	/**
	 * Moves the ship down at a certain speed.
	 * @param speed How fast the ship moves down.
	 */
	public void moveDown(int speed){
		if(disableControl){
			return;
		}
		if(y + shipHeight < 780 )
			y += speed;
	}
	
	/**
	 * Moves the ship right at a certain speed.
	 * @param speed How fast the ship moves right.
	 */
	public void moveRight(int speed){
		if(disableControl){
			return;
		}
		if(x + shipWidth < 600)
			x += speed;
	}
	
	/**
	 * Moves the ship left at a certain speed.
	 * @param speed How fast the ship moves left.
	 */
	public void moveLeft(int speed){
		if(disableControl){
			return;
		}
		if(x > 0)
			x -= speed;
	}
	
	/**
	 * Prints out the coordinates of the PlayerShip. Used for debugging.
	 */
	public String toString(){
		return "Coordinates: "+this.x + " " + this.y;
	}

	@Override
	public int getDamage() {
		return 1;
	}

	/**
	 * Used when drawing the ship in its different states.  When the ship moves left or right,
	 * this method determines which image to draw at the time.
	 * @param imageLocation The file location of the image to use.
	 */
	public void setImage(String imageLocation)
	{
		try {
			img = ImageIO.read(this.getClass().getResourceAsStream(imageLocation));
			shipWidth = img.getWidth();
			shipHeight = img.getHeight();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean isTerminated() {
		return false;
	}

	/**
	 * Gets the x-coordinate of the ship's center
	 * @return The ships x-coordinate center
	 */
	public int getXCenter(){
		return x + (img.getWidth()/2);
	}

	/**
	 * Gets the y-coordinate of the ship's center
	 * @return The ships y-coordinate center
	 */
	public int getYCenter(){
		return y + (img.getHeight()/2);
	}

	@Override
	public Double getBounds() {

		return new Rectangle2D.Double(x + shipWidth/2 - shipWidth/8, y + shipHeight/4 + shipHeight/8, shipWidth/4, shipHeight/4);
	}

	@Override
	public void recieveDamage(int damage){
		if(!deathSequence){
			if(damage >= hp){
				deathSequence = true;
				deathTimer = 0;
				laserLevel = 0;
				//lives--;
				soundPlayer.playSound("/res/soundeffects/playerexplosion.wav");

			}
		}
		else{

		}
	}

	/**
	 * Sets the ProjectileListener for the Player class.
	 * @param listener The listener the player uses.
	 */
	public void setProjectileListener(ProjectileListener listener){
		pListener = listener;
	}
	
	/**
	 * Used to set the player at the bottom of the screen and have him/her move up onto
	 * the screen.
	 */
	public void spawnPlayer(){
		deathSequence=true;
		deathTimer = 24;
		x = 200;
		y = 800;
	}
	
	/**
	 * After the player completes a level, this method is used to fly him off screen
	 */
	public void flyPlayerOffscreen(){
		if(y > -100)
			y -= 9;
	}

	/**
	 * Handles the firing mechanism for the PlayerShip.
	 */
	public void fire(){
		if(disableControl){
			return;
		}
		if(deathSequence && deathTimer<120){
			return;
		}
		switch(powerLevel){
		case 1:
			pListener.addProjectile(new PlayerBasicBullet(this.getXCenter(),this.getYCenter(),BasicBullet.STRAIGHT_PATH_UP, 12, 1));
			break;
		case 2:
			pListener.addProjectile(new PlayerBasicBullet(this.getXCenter()+17,this.getYCenter(),BasicBullet.STRAIGHT_PATH_UP, 12, 1));
			pListener.addProjectile(new PlayerBasicBullet(this.getXCenter()-17,this.getYCenter(),BasicBullet.STRAIGHT_PATH_UP, 12, 1));
			break;
		case 3:
			pListener.addProjectile(new PlayerBasicBullet(this.getXCenter()+17,this.getYCenter(),BasicBullet.STRAIGHT_PATH_UP, 12, 2));
			pListener.addProjectile(new PlayerBasicBullet(this.getXCenter()-17,this.getYCenter(),BasicBullet.STRAIGHT_PATH_UP, 11, 2));
			break;
		case 4:
			pListener.addProjectile(new PlayerBasicBullet(this.getXCenter()+17,this.getYCenter(),BasicBullet.STRAIGHT_PATH_UP, 12, 2));
			pListener.addProjectile(new PlayerBasicBullet(this.getXCenter()-17,this.getYCenter(),BasicBullet.STRAIGHT_PATH_UP, 12, 2));
			PlayerBasicBullet upRightBullet = new PlayerBasicBullet(this.getXCenter()+20, this.getYCenter(), BasicBullet.NO_PATH, 12, 1);
			PlayerBasicBullet upLeftBullet = new PlayerBasicBullet(this.getXCenter()-20, this.getYCenter(), BasicBullet.NO_PATH, 12, 1);
			upRightBullet.setPath(this.getXCenter()+30, this.getYCenter()-25);
			upLeftBullet.setPath(this.getXCenter()-30, this.getYCenter()-25);
			pListener.addProjectile(upLeftBullet);
			pListener.addProjectile(upRightBullet);
			break;
		case 5:
			pListener.addProjectile(new PlayerBasicBullet(this.getXCenter(),this.getYCenter(),BasicBullet.STRAIGHT_PATH_UP, 12, 3));
			pListener.addProjectile(new PlayerBasicBullet(this.getXCenter()+25,this.getYCenter(),BasicBullet.STRAIGHT_PATH_UP, 12, 3));
			pListener.addProjectile(new PlayerBasicBullet(this.getXCenter()-25,this.getYCenter(),BasicBullet.STRAIGHT_PATH_UP, 12, 3));
			PlayerBasicBullet upRightBullet1 = new PlayerBasicBullet(this.getXCenter()+20, this.getYCenter(), BasicBullet.NO_PATH, 12, 1);
			PlayerBasicBullet upLeftBullet1 = new PlayerBasicBullet(this.getXCenter()-20, this.getYCenter(), BasicBullet.NO_PATH, 12, 1);
			upRightBullet1.setPath(this.getXCenter()+30, this.getYCenter()-25);
			upLeftBullet1.setPath(this.getXCenter()-30, this.getYCenter()-25);
			pListener.addProjectile(upLeftBullet1);
			pListener.addProjectile(upRightBullet1);
			break;
		case 6:
			pListener.addProjectile(new PlayerBasicBullet(this.getXCenter(),this.getYCenter() -20,BasicBullet.STRAIGHT_PATH_UP, 12, 3));
			pListener.addProjectile(new PlayerBasicBullet(this.getXCenter(),this.getYCenter() +20,BasicBullet.STRAIGHT_PATH_UP, 12, 3));
			pListener.addProjectile(new PlayerBasicBullet(this.getXCenter()+25,this.getYCenter(),BasicBullet.STRAIGHT_PATH_UP, 12, 3));
			pListener.addProjectile(new PlayerBasicBullet(this.getXCenter()-25,this.getYCenter(),BasicBullet.STRAIGHT_PATH_UP, 12, 3));
			
			PlayerBasicBullet upRightBullet2 = new PlayerBasicBullet(this.getXCenter()+20, this.getYCenter(), BasicBullet.NO_PATH, 12, 5);
			PlayerBasicBullet upLeftBullet2 = new PlayerBasicBullet(this.getXCenter()-20, this.getYCenter(), BasicBullet.NO_PATH, 12, 4);
			upRightBullet2.setPath(this.getXCenter()+30, this.getYCenter()-25);
			upLeftBullet2.setPath(this.getXCenter()-30, this.getYCenter()-25);
			pListener.addProjectile(upLeftBullet2);
			pListener.addProjectile(upRightBullet2);
			break;
		default:
			pListener.addProjectile(new PlayerBasicBullet(this.getXCenter(),this.getYCenter(),BasicBullet.STRAIGHT_PATH_UP, 12, 1));
			break;
		}

		soundPlayer.playSound("/res/soundeffects/shot2.wav");
	}
	
	/**
	 * This fires the secondary fire mode for the player
	 */
	public void fireLaser()
	{
		if(disableControl){
			return;
		}
		if(deathSequence && deathTimer<120){
			return;
		}
		else
		{
			switch(laserLevel){
			case 0:
				pListener.addProjectile(new PlayerBasicLaser(this.getXCenter(),this.getYCenter(),BasicBullet.STRAIGHT_PATH_UP, 14, 1));
				break;
			case 1:
				pListener.addProjectile(new PlayerBasicLaser(this.getXCenter(),this.getYCenter(),BasicBullet.STRAIGHT_PATH_UP, 14, 1));
				break;
			case 2:
				pListener.addProjectile(new PlayerBasicLaser(this.getXCenter()-20,this.getYCenter(),BasicBullet.STRAIGHT_PATH_UP, 14, 1));
				pListener.addProjectile(new PlayerBasicLaser(this.getXCenter()+20,this.getYCenter(),BasicBullet.STRAIGHT_PATH_UP, 14, 1));
				break;
			case 3:
				pListener.addProjectile(new PlayerBasicLaser(this.getXCenter(),this.getYCenter(),BasicBullet.STRAIGHT_PATH_UP, 14, 1));
				pListener.addProjectile(new PlayerBasicLaser(this.getXCenter()+22,this.getYCenter(),BasicBullet.STRAIGHT_PATH_UP, 14, 1));
				pListener.addProjectile(new PlayerBasicLaser(this.getXCenter()-22,this.getYCenter(),BasicBullet.STRAIGHT_PATH_UP, 14, 1));
				break;

			}
		}
	}

	/**
	 * The player has various degree of power depending on how many power ups he has picked up.
	 * This is to increase his power by 1 when he collects a power up.
	 */
	public void increasePower()
	{
		if(powerLevel!=6){
			powerLevel++;
			soundPlayer.playSound("/res/soundeffects/powerup.wav");
		}
	}
	/**
	 * This is used to increase the fire power of the secondary firing mode.
	 */
	public void increaseLaserPower()
	{
		laserLevel = powerLevel/2;
	}
	
	/**
	 * Gets how many lives the player has.
	 * @return The number of lives available to the player.
	 */
	public int getLives(){
		return lives;
	}
	
	/**
	 * Determines if the player is invincible.  The player is invincible during the beginning of
	 * a level and when spawning after being killed.
	 * @return Returns true if the player is in invincible mode.
	 */
	public boolean isInvincible()
	{
		return deathSequence;
	}

	/**
	 * Resets the player's lives and fire power to its starting point.
	 */
	public void resetShip() {
		lives = 5;
		powerLevel = 0;
		laserLevel = 0;
		score = 0;
	}
	
	/**
	 * At the end of a stage, the player loses control of the ship so that we can transition into
	 * the next stage.
	 */
	public void disableControl(){
		disableControl = true;
		setImage("/res/playersprite.png");
	}
	
	/**
	 * This gives the player back control of the ship when the level begins.
	 */
	public void enableControl(){
		disableControl = false;
	}

	public void increaseScore(int score){
		this.score += score;
	}
	public int getScore(){
		return score;
	}
}
