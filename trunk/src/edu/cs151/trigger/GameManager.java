package edu.cs151.trigger;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.*;

import edu.cs151.trigger.entities.Entity;
import edu.cs151.trigger.entities.enemies.Boss;
import edu.cs151.trigger.entities.enemies.BossTwo;
import edu.cs151.trigger.entities.enemies.Enemy;
import edu.cs151.trigger.entities.enemies.Popcorn;
import edu.cs151.trigger.entities.enemies.Popcorn.EnemyType;
import edu.cs151.trigger.entities.enemies.Boss.BossType;
import edu.cs151.trigger.entities.enemies.BossTwo.BossTwoType;
import edu.cs151.trigger.entities.player.PlayerShip;
import edu.cs151.trigger.entities.projectile.PowerUp;
import edu.cs151.trigger.entities.projectile.bullet.BasicBullet;
import edu.cs151.trigger.entities.projectile.bullet.PlayerBasicBullet;
import edu.cs151.trigger.entities.projectile.bullet.PlayerBasicLaser;
import edu.cs151.trigger.layers.AboutLayer;
import edu.cs151.trigger.layers.BackgroundLayer;
import edu.cs151.trigger.layers.Level;
import edu.cs151.trigger.layers.MenuLayer;
import edu.cs151.trigger.layers.UILayer;
import edu.cs151.trigger.util.Event;
import edu.cs151.trigger.util.MusicPlayer;
import edu.cs151.trigger.util.SoundPlayer;
import edu.cs151.trigger.util.Path.PathType;
import edu.cs151.trigger.util.ProjectileListener;
import edu.cs151.trigger.util.Event.EventType;

/**
 * The point is the have a level manager which reads the level file. Then can change the level accordingly. 
 * @author Chris Wong
 *
 */
public class GameManager extends JPanel implements KeyListener, ProjectileListener{

	LinkedList<Event> eventList;
	PlayerShip ps;
	private boolean keyUp = false;
	private boolean keyDown = false;
	private boolean keyLeft = false;
	private boolean keyRight = false;
	private boolean firing = false;
	private boolean firinglaser = false;
	private boolean isScreenClear;
	private int speed = 5;
	private int firedelay = 0;
	private int gameTime = 0;
	private MusicPlayer mp;
	private Event currentEvent;
	private boolean levelComplete;
	private long timeStamp;
	private ArrayList<Entity> onScreenEnemyEntites;
	private ArrayList<Entity> onScreenFriendlyEntites;
	private ArrayList<Entity> tempEntities;
	private ArrayList<Entity> tempEntities2;
	private BackgroundLayer bg1;
	private BackgroundLayer bg2;
	private Level level;
	private MenuLayer menu;
	private AboutLayer about;
	private UILayer uilayer;
	private int menuOrLevel = 0;
	private final int numberOfLevels = 2;
	private int currentLevel;

	private static final SoundPlayer soundPlayer = new SoundPlayer();


	public GameManager(){

		this.setFocusable(true);
		this.setFocusTraversalKeysEnabled(false);
		this.addKeyListener(this);
		this.setBackground(Color.BLACK);
		bg1 = new BackgroundLayer(4,false);
		bg2 = new BackgroundLayer(2,true);
		menu = new MenuLayer();
		about = new AboutLayer();
	}
	/**
	 * This is the main loop of the game. Controls all repainting.
	 */
	public void gameLoop(){
		if(menuOrLevel == 1){
			//If the level is complete, then transition to the next level.
			if(levelComplete  && onScreenEnemyEntites.size() == 0){
				uilayer.setMessage("Level  " + currentLevel + "  Complete");
				moveShip();
				fireWeapon();
				
				if(gameTime == 100){
					ps.disableControl();
				}
				if(gameTime > 300 && ps.getYCenter() > -50){
					ps.flyPlayerOffscreen();
					
				}
				//After the player flies offscreen, this determines if we need to load the next level
				if(ps.getYCenter() <= -50 && uilayer.getState() == UILayer.NORMAL){
					if(currentLevel+1>numberOfLevels){
						mp.stop();
						ps.resetShip();
						ps.enableControl();
						gameTime = 0;
						firedelay = 0;
						onScreenEnemyEntites.clear();
						onScreenFriendlyEntites.clear();
						keyDown = keyUp = keyLeft = keyRight = firing = firinglaser = false;
						onScreenFriendlyEntites.add(ps);
						menuOrLevel = 0;
						menu = new MenuLayer();
						levelComplete = false;
						
					}
					else{
						uilayer.drawFadeOut();
						currentLevel++;
						mp.stop();
					}
				}
				if(uilayer.getState()==UILayer.BLANK_SCREEN){
					if(currentLevel <= numberOfLevels){
						uilayer.drawFadeIn();
						gameTime = 0;
						firedelay = 0;
						loadLevel(currentLevel);
						keyDown = keyUp = keyLeft = keyRight = firing = firinglaser = false;
						ps.enableControl();
						ps.spawnPlayer();
						levelComplete = false;
						uilayer.setMessage("Level  " + currentLevel + "  Start");

					}
				}
				this.repaint();
				gameTime++;
				return;
				
			}
			
			//This continues the game
			gameTime++;
			moveShip();
			fireWeapon();
			if(uilayer.getState() == UILayer.NORMAL){
				if (currentEvent == null)
				{
					if (!eventList.isEmpty())
					{
						currentEvent = (Event) eventList.poll();
						System.out.println("polled");
						if (currentEvent == null)
						{
							levelComplete = true;
							gameTime = 0;
						}
						timeStamp = gameTime;
		
					}
					else
					{
						levelComplete = true;
						gameTime = 0;
					}
				} 
				
				else{
					if (!currentEvent.hasBeenConsumed())
					{
						EventType et = currentEvent.getEventString();
						switch(et)
						{
							case SPAWN:
									if (timeStamp == 0)
									{
										timeStamp = gameTime;
		
									}
									if (gameTime - timeStamp >= currentEvent
											.getDelay())
									{
										proccessEnemyEvent(currentEvent.execute());
										isScreenClear = false;
										timeStamp = 0;
									}
								break;
							case MUSIC:
								musicStop();
								musicStart(currentEvent.getMusic());
								currentEvent.execute();
								break;
							default:
								break;
						}
						
					}
					else
					{
						if(currentEvent.waitForScreenClear()==true){
							if( isScreenClear ){
								currentEvent = null;
							}
						}
						else{
							currentEvent = null;
						}
						
					}
				}
			}
			
			onScreenEnemyEntites.addAll(tempEntities);
			onScreenFriendlyEntites.addAll(tempEntities2);
			tempEntities.clear();
			tempEntities2.clear();
			
			//Collision detection
			for(Entity friendly:onScreenFriendlyEntites){
				//Just doing for bullets for now
				if(friendly instanceof PlayerBasicBullet || friendly instanceof PlayerBasicLaser){
					for(Entity enemy:onScreenEnemyEntites){
						if(enemy instanceof Enemy)
						{
							if(enemy.collided(friendly))
							{
								enemy.recieveDamage(friendly.getDamage());
								friendly.recieveDamage(enemy.getDamage());

							}
						}
						
					}
				}
			}
			isScreenClear = true;
			//Player collision detection
			for(Entity enemy:onScreenEnemyEntites){
				if(enemy instanceof Enemy){
					isScreenClear = false;
				}
				if(ps.collided(enemy))
				{
					
					if(enemy instanceof PowerUp){
						ps.increasePower();
						ps.increaseLaserPower();
						enemy.recieveDamage(ps.getDamage());
					}
					if(!ps.isInvincible())
					{
						enemy.recieveDamage(ps.getDamage());
					}
					ps.recieveDamage(enemy.getDamage());
				}
			}
			if(ps.getLives() <= 0){
				mp.stop();
				ps.resetShip();
				ps.enableControl();
				onScreenEnemyEntites.clear();
				onScreenFriendlyEntites.clear();
				keyDown = keyUp = keyLeft = keyRight = firing = firinglaser = false;
				onScreenFriendlyEntites.add(ps);
				menuOrLevel = 0;
				menu = new MenuLayer();
			}
		}
		this.repaint();
	}
	/**
	 * Paints the appropriate layers.
	 */
	public void paintComponent(Graphics g){
		super.paintComponents(g);
		//Turn on anti alaising
		Graphics2D g2 = (Graphics2D)g;
		
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		
		
		bg1.draw(g);
		bg2.draw(g);
		if(menuOrLevel == 0){
			menu.draw(g);
		} else if(menuOrLevel == 2){
			about.draw(g);
		} else{
			level.draw(g);
			uilayer.draw(g);
		}
	}
	/**
	 * Find out if the screen is clear.
	 * @return true if level is clear of enemies.
	 */
	public boolean levelClear(){
		if (onScreenEnemyEntites.size() == 0)
		{
			return true;
		} else
		{
			return false;
		}
	
	}
	/**
	 * Track whether a player is pressing a key.
	 */
	public void keyPressed(KeyEvent ke) {
		if(ke.getKeyCode() == KeyEvent.VK_SHIFT){
			speed = 2;
		}
		if(ke.getKeyCode() == KeyEvent.VK_LEFT)
		{
			if(menuOrLevel!=0&&menuOrLevel!=2){
				//Testing set the ship angled to the left
				keyLeft = true;
				ps.setImage("/res/moveleft.png");
			}
		}
		if(ke.getKeyCode() == KeyEvent.VK_UP){
			if(menuOrLevel == 0){
				soundPlayer.playSound("/res/soundeffects/shot2.wav");
				menu.moveUp();
			} else {
				if(keyDown){
					keyUp = false;
				}
				keyUp = true;
			}
		}
		if(ke.getKeyCode() == KeyEvent.VK_RIGHT)
		{
			if(menuOrLevel!=0&&menuOrLevel!=2){
			//Testing set the ship angled to the right
				keyRight = true;
				ps.setImage("/res/moveright.png");
			}
		}
		if(ke.getKeyCode() == KeyEvent.VK_DOWN){
			if(menuOrLevel == 0){
				soundPlayer.playSound("/res/soundeffects/shot2.wav");
				menu.moveDown();
			} else {
				if(keyUp){
					keyUp = false;
				}
				keyDown = true;
			}
		}
		if(ke.getKeyCode() == KeyEvent.VK_Z){
			if(firinglaser){
				firinglaser = false;
			}
			firing = true;
		}
		if(ke.getKeyCode() == KeyEvent.VK_X)
		{
			if( firing ){
				firing = false;
			}
			firinglaser = true;
		}
		if(ke.getKeyCode() == KeyEvent.VK_ENTER){
			if(menuOrLevel == 0){
				soundPlayer.playSound("/res/soundeffects/shot2.wav");
				selectionMenu(menu.getCurrentIndex());
			}
			else if(menuOrLevel == 2){
				System.out.println("exit");
				menuOrLevel = 0;
			}
		}
	}
	/**
	 * Track whether a player released a key.
	 */
	public void keyReleased(KeyEvent ke) {
		if(menuOrLevel!=0&&menuOrLevel!=2){
			if(ke.getKeyCode() == KeyEvent.VK_SHIFT){
				speed = 5;
			}
			if(ke.getKeyCode() == KeyEvent.VK_LEFT)
			{
				
				//Since the ship stopped moving to the left
				//set him back to the default position
				keyLeft = false;
				ps.setImage("/res/playersprite.png");
			
			}
			if(ke.getKeyCode() == KeyEvent.VK_UP){
				keyUp = false;
			}
			if(ke.getKeyCode() == KeyEvent.VK_RIGHT)
			{
				//Since the ship stopped moving to the right
				//set him back to the default position
				keyRight = false;
				ps.setImage("/res/playersprite.png");
			}
			if(ke.getKeyCode() == KeyEvent.VK_DOWN){
				keyDown = false;
			}
			if(ke.getKeyCode() == KeyEvent.VK_Z){
				firing = false;
			}
			if(ke.getKeyCode() == KeyEvent.VK_X)
			{
				firinglaser = false;
			}
		}
	}

	public void keyTyped(KeyEvent ke) {}
	
	/**
	 * If a player is pressing a key, move the ship.
	 */
	public void moveShip(){
		if(keyLeft == true){
			ps.moveLeft(speed);
		}
		if(keyRight == true){
			ps.moveRight(speed);
		}
		if(keyDown == true){
			ps.moveDown(speed);
		}
		if(keyUp == true){
			ps.moveUp(speed);
		}
	}
	/**
	 * If player is pressing the button, the playership fires.
	 */
	public void fireWeapon(){
		if(firing)
		{
			if(firedelay < gameTime)
			{
				firedelay = gameTime + 9;
				ps.fire();
				
			}
		}
		else if(firinglaser)
		{
			if(firedelay < gameTime)
			{
				firedelay = gameTime + 2;
				ps.fireLaser();
			}
		}
	}
	/**
	 * When on the menu screen, this selects the what the program should do.
	 * @param selection the index of the menu selected.
	 */
	public void selectionMenu(int selection){
		switch(selection){

			case 0:
				menu.stopMusic();
				menuOrLevel = 1;
				currentLevel = 1;
				loadLevel(currentLevel);
				uilayer.drawFadeIn();
				ps.spawnPlayer();
				uilayer.setMessage("Level  " + currentLevel + "  Start");

				break;
			case 1:
				menuOrLevel = 2;
				break;
			case 2:
				System.exit(0);
				break;
			default:
				break;
		}
	}
	/**
	 * Load a level
	 * @param levelNumber The level number to load.
	 */
	public void loadLevel(int levelNumber){
		//Init Variables
		eventList = new LinkedList<Event>();
		ps = PlayerShip.getInstance();
		ps.setProjectileListener(this);
		currentEvent = null;
		if(onScreenEnemyEntites==null){
			onScreenEnemyEntites = new ArrayList<Entity>();
			onScreenFriendlyEntites = new ArrayList<Entity>();
			tempEntities = new ArrayList<Entity>();
			tempEntities2 = new ArrayList<Entity>();
			onScreenFriendlyEntites.add(ps);
			level = new Level(onScreenEnemyEntites,onScreenFriendlyEntites);
			uilayer = new UILayer();
		}
		
		
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/Level"+levelNumber+".txt"))); 
			Popcorn.loadEnemySet(levelNumber);
			String s; 
			Event event = null;
			while((s = br.readLine()) != null) {
				String[] result = s.split("\\,");
				if(result[0].equalsIgnoreCase("bgmusic")){
					System.out.println("New Music");
					event = new Event(0, Boolean.parseBoolean(result[2]));
					event.addMusic(result[1]);
					eventList.add(event);
				}
				else if(result[0].equalsIgnoreCase("startevent")){
					System.out.println("New Event");
					event = new Event(Integer.parseInt(result[1]), Boolean.parseBoolean(result[2]));
				}
				else if(result[0].equalsIgnoreCase("popcorn")){
					if(event != null){
						if(result.length == 6){
							event.addEntity(new Popcorn(Integer.parseInt(result[1]),Integer.parseInt(result[2]),
								EnemyType.valueOf(result[3]),PathType.valueOf(result[4]), true));
						}
						else{
							event.addEntity(new Popcorn(Integer.parseInt(result[1]),Integer.parseInt(result[2]),
									EnemyType.valueOf(result[3]),PathType.valueOf(result[4]), false));
						}
						
					}
					
				}
				else if(result[0].equalsIgnoreCase("boss")){
					if(event != null)
					{
							event.addEntity(new Boss(Integer.parseInt(result[1]), Integer.parseInt(result[2]), BossType.valueOf(result[3]),PathType.valueOf(result[4])));
					}
				}
				else if(result[0].equalsIgnoreCase("bosstwo"))
				{
					if(event != null)
					{
						event.addEntity(new BossTwo(Integer.parseInt(result[1]), Integer.parseInt(result[2]), BossTwoType.valueOf(result[3]),PathType.valueOf(result[4])));
					}
				}
				else if(result[0].equalsIgnoreCase("endevent")){
					eventList.add(event);
				}
				else {
					System.out.println("nothing");
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		
		
	}
	
	/**
	 * Start music
	 * @param musicFile the filname of the music file.
	 */
	public void musicStart(String musicFile){
	
		mp = new MusicPlayer(musicFile);
		mp.start();
	}
	
	/**
	 * Process the array of enemies in an event.
	 * @param e the array of enemies.
	 */
	public void proccessEnemyEvent(ArrayList<Entity> e){
		for(Entity i:e){
			 if(i instanceof Enemy){
				 ((Enemy)i).setProjectileListener(this);
			 }
		}
		onScreenEnemyEntites.addAll(e);
	}
	

	/**
	 * Adds a projectile to the tempEntities array.
	 * @param e the project to add.
	 */
	public void addProjectile(Entity e)
	{

		if(e instanceof PlayerBasicBullet || e instanceof PlayerBasicLaser){
			tempEntities2.add(e);
		}

		else if(e instanceof BasicBullet){
			if(!((BasicBullet)e).isPathSet()){
				((BasicBullet)e).setPath(ps.getXCenter(), ps.getYCenter());
			}
			tempEntities.add(e);
		}
		else{
			tempEntities.add(e);
		}
		
		
		
	}
	/**
	 * Stops the music from playing.
	 */
	public void musicStop(){
		if(mp != null)
			mp.stop();
	}
}
