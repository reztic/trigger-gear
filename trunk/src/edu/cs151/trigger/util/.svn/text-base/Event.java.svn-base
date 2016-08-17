package edu.cs151.trigger.util;

import java.util.ArrayList;

import edu.cs151.trigger.entities.Entity;

/**
 * Levels are comprised of a series of Events.  Each Event tells the game when to spawn enemies,
 * where to spawn them, when to play music, and if the screen has to be clear of enemies before
 * moving on
 * @author Tan Nguyen
 *
 */
public class Event {
	
	/**
	 * This enumeration is used to specify what kind of event this Event is.
	 * @author Tan Nguyen
	 *
	 */
	public static enum EventType {
		SPAWN, MUSIC
	}
	
	/**
	 * This creates a basic event.
	 * @param delay Specifies how long to wait before executing the Event.
	 * @param clear A flag to determine if this Event should be cleared before moving on.
	 */
	public Event(long delay, boolean clear){
		this.delay = delay;
		this.waitForScreenClear = clear;
		this.eventString = null;
		this.musicFile = null;
		this.consumed = false;
	}
	
	/**
	 * Add an Entity to this Event
	 * @param e The Entity to spawn at this Event
	 */
	public void addEntity(Entity e){
		eventString = EventType.SPAWN;
		if(entity == null)
			entity = new ArrayList<Entity>();

		entity.add(e);
	}
	
	/**
	 * This is used to specify which music file to play at this Event
	 * @param file The filename and path of the music.  Only supports .mid files.
	 */
	public void addMusic(String file){
		eventString = EventType.MUSIC;
		musicFile = file;
	}
	
	/**
	 * Executes this Event.
	 * @return If this event spawn enemies, it will return an ArrayList of Entities, otherwise null.
	 */
	public ArrayList<Entity> execute(){
		this.consumed = true;
		return entity;
	}
	
	/**
	 * Gets the file path of the music file if this Event is an EventType: MUSIC.
	 * @return The file path of the music file.
	 */
	public String getMusic(){
		return musicFile;
	}
	
	/**
	 * Gets the delay of this Event.
	 * @return The delay of when to start this Event.
	 */
	public long getDelay(){
		return delay;
	}
	
	/**
	 * Determines where this Event needs to be clear of enemies before moving on
	 * @return Returns true if the flag is turn on, false otherwise.
	 */
	public boolean waitForScreenClear(){
		return waitForScreenClear;
	}
	
	/**
	 * Sets the flag to notify this Event that the screen has been cleared of enemies.
	 */
	public void setScreenClear(){
		waitForScreenClear = false;
	}
	
	/**
	 * Gets the type of Event
	 * @return The name of the type of Event.  There are 2 different EventTypes: SPAWN and MUSIC
	 */
	public EventType getEventString(){
		return eventString;
	}
	
	/**
	 * Used to determine if this Event has been consumed.
	 * @return True if this Event has been consumed, false otherwise.
	 */
	public boolean hasBeenConsumed(){
		return consumed;
	}

	
	private String musicFile;
	private EventType eventString;
	private long delay;
	private ArrayList<Entity> entity;
	private boolean waitForScreenClear;
	private boolean consumed;
}
