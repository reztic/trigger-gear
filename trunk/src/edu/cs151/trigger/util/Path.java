package edu.cs151.trigger.util;

import edu.cs151.trigger.entities.enemies.Enemy;
/**
 * The paths for different enemy types
 * @author Philip Vaca
 *
 */
public class Path {
	//Hiding constructor
	private Path(){};
	private static boolean leftOrRight = false;
	
	public static enum PathType{
		SINE_WAVE, STRAIGHT_DOWN, IN_AND_OUT, FAST_STRAIGHT_DOWN, WIDE_SINE_WAVE, BOSS1, BOSS2
	}
	
	/**
	 * Grabs the next Y position for an Enemy depending on varying factors.
	 * @param currentX Current X position of the Enemy
	 * @param currentY Current Y position of the Enemy
	 * @param pathType The type of path this entity should use.
	 * @param xOffset The X offset of this Enemy starting position relative to the (0,0) point
	 * @param yOffset The Y offset of this Enemy starting position relative to the (0,0) point
	 * @param spawnTime How long this Enemy has be alive for.
	 * @param enemy The Enemy which the Path class is updating for.
	 * @return The new Y position
	 */
	public static double getNextY(double currentX, double currentY, PathType pathType, int xOffset, int yOffset, int spawnTime, Enemy enemy){
		switch(pathType){
		case SINE_WAVE:
			if(currentY>800){
				enemy.setTermination();
			}
			return currentY + 2;
		case STRAIGHT_DOWN:
			if(currentY>800){
				enemy.setTermination();
			}
			return currentY + 3;
		case IN_AND_OUT:
			if(spawnTime > 600)
			{
				enemy.setTermination();
			}
			if(spawnTime > 200){
				return currentY - 2;
			}
			return currentY + 2;	
		case FAST_STRAIGHT_DOWN:
			if(currentY>800){
				enemy.setTermination();
			}
			return currentY + 7;
		case WIDE_SINE_WAVE:
			if(currentY>800){
				enemy.setTermination();
			}
			return currentY + 1.5;
		case BOSS1:
			if(spawnTime < 140)
			{
				return currentY = currentY + 1;
			}
			return currentY;
		case BOSS2:
			if(spawnTime < 180)
			{
				return currentY = currentY + 1;
			}
			return currentY;
		default:
				return 0;
			
			
			
		}
	}
	
	/**
	 * Grabs the next X position for an Enemy depending on varying factors.
	 * @param currentX Current X position of the Enemy
	 * @param currentY Current Y position of the Enemy
	 * @param pathType The type of path this entity should use.
	 * @param xOffset The X offset of this Enemy starting position relative to the (0,0) point
	 * @param yOffset The Y offset of this Enemy starting position relative to the (0,0) point
	 * @param spawnTime How long this Enemy has be alive for.
	 * @param enemy The Enemy which the Path class is updating for.
	 * @return The new X position
	 */
	public static double getNextX(double currentX, double currentY, PathType pathType, int xOffset, int yOffset, int spawnTime, Enemy enemy){
		switch(pathType){
		case SINE_WAVE:
			/*
			 * The function here is f(fequency*(y-yOffset))+xOffset, where f(y) = 45*sin(y)
			 * The smaller the fequency the longer the wave will be stretched out
			 * So with any path that uses a function, define it in terms of y so that x = f(y-yOffset) + xOffset.
			 * It is important to make your function pass the (0,0) point and be continuous.
			 */
			return (int)(45*Math.sin(.02*(currentY-yOffset)))+xOffset;
		case STRAIGHT_DOWN:
			return currentX;
		case IN_AND_OUT:
			if(spawnTime > 50 && spawnTime < 200){
				if(currentX >= 300) //Have the enemy move to the left
				{
					return currentX - 1;
				}
				else //Have the enemy move to the right
				{
					return currentX + 1;
				}
			}
			return currentX;
		case FAST_STRAIGHT_DOWN:
			return currentX;
		case WIDE_SINE_WAVE:
			return (int)(400*Math.sin(.0125*(currentY-yOffset)))+xOffset;
		case BOSS1:
			if(spawnTime < 140)
			{
				return currentX;
			}
			if(currentX == -20)
			{
				leftOrRight = true;
			}
			else if(currentX == 220)
			{
				leftOrRight = false;
			}
			if(leftOrRight == true)
			{
				return currentX+1;
			}
			else
			{
				return currentX-1;
			}
		case BOSS2:
			return currentX;
		default:
			return 0;
		}
	}
}
