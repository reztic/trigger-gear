package edu.cs151.trigger.util;

import java.io.File;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * This class is used to play the background music in all stages of the game.
 * @author Philip Vaca
 *
 */
public class MusicPlayer implements Runnable{

	private Sequence seq;
	private Sequencer player;
	private String filename;
	private volatile boolean play;
	private AudioInputStream audioInputStream;
	private SourceDataLine line;
	private int wavOrMidi; // 1 is wav, 0 is midi
	
	/**
	 * Creates a separate thread to play the background music.  It supports both wav and
	 * midi files.
	 * @param file The file path of the music file.
	 */
	public MusicPlayer(String file){
		if(file.split("\\.")[1].equalsIgnoreCase("wav")){
			System.out.println("Music File is Wav");

			this.filename = file;
			play = false;
			wavOrMidi = 1;
		} else if (file.split("\\.")[1].equalsIgnoreCase("mid") ||file.split("\\.")[1].equalsIgnoreCase("midi")){
			System.out.println("Music File is MIDI");
			this.filename = file;
			play = false;
			wavOrMidi = 0;
		}
	}
	
	/**
	 * Initializes the file before running the thread.
	 */
	public void init(){
		if(wavOrMidi == 1){
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
		} else if(wavOrMidi == 0){
			try
			{
				seq = MidiSystem.getSequence(this.getClass().getResourceAsStream(filename));
				player = MidiSystem.getSequencer();
				player.setSequence(seq);
				player.open();
				player.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
			} catch (InvalidMidiDataException e)
			{
				e.printStackTrace();
			} catch (IOException e)
			{
				e.printStackTrace();
			} catch (MidiUnavailableException e)
			{
				e.printStackTrace();
			}
			
		}
	}
	
	/**
	 * Creates a thread and plays the music file.
	 */
	public void start(){
		
		play = true;
		new Thread(this).start();
	}
	
	/**
	 * Closes the thread and stops the music file.
	 */
	public void stop(){
		System.out.println("STOP music");
		play = false;
		if(wavOrMidi == 0){
			if(player.isRunning()){
				player.stop();
				player.close();
			}
			
		}
	}
	
	/**
	 * This is the method that runs when thread is created.
	 */
	public void run() {
		init();
		if(wavOrMidi == 1){
			int	nBytesRead = 0;
			byte[]	abData = new byte[524288];
			try
			{
				while (nBytesRead != -1 && play == true)
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
		}else if(wavOrMidi == 0){
			player.start();

		}
	} 

}
