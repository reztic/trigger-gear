package edu.cs151.trigger.util;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.*;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;


public class SoundPlayer {
	private static SoundPlayer instance = null;
	private ThreadPoolExecutor threadPoll = null;
	public SoundPlayer(){
		this.threadPoll = new ThreadPoolExecutor(10, 40, 3000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), new ThreadPoolExecutor.DiscardPolicy());
	}

	public static SoundPlayer getInstance(){
		if( instance == null ){
			instance = new SoundPlayer();
		}
		return instance;
	}

	public void playSound( String file ){
		System.out.println(this.threadPoll.getCorePoolSize());
		this.threadPoll.submit( new SoundClip( file ));
	}
}


class SoundClip implements Runnable{

	private String filename;
	private AudioInputStream audioInputStream;
	private SourceDataLine line;
	
	/**
	 * Creates a separate thread which will play a sound file.
	 * @param file The file path of the sound effect to play.
	 */
	public SoundClip(String file){
		this.filename = file;
	}

	/**
	 * Executed when the thread is created.
	 */
	public void run() {
		try {
			audioInputStream  = AudioSystem.getAudioInputStream(this.getClass().getResourceAsStream(filename));
			AudioFormat	audioFormat = audioInputStream.getFormat();
			line = null;
			DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);	
			line = (SourceDataLine) AudioSystem.getLine(info);
			line.open(audioFormat);
			line.start();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		
	
		int	nBytesRead = 0;
		byte[]	abData = new byte[524288];
		try
		{
			while (nBytesRead != -1)
			{
	
					nBytesRead = audioInputStream.read(abData, 0, abData.length);
					if (nBytesRead >= 0)
					{
						line.write(abData, 0, nBytesRead);
					}
				}

			}
		catch (IOException e)
		{
			e.printStackTrace();
			return;
		} 
		finally{
			line.drain();
			line.close();
		}
	}

}
