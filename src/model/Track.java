package model;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Converts an audio file into something that can be played by a <code>TrackPlayer</code>.
 * 
 * @author Adam
 */
public class Track {
	private Clip clip;
	private AudioInputStream audioInputStream;
	
	/**
	 * Creates a new <code>Track</code> instance and obtains an audio input stream for the provided audio file.
	 * @param audioFile the file that will be used to play audio from.
	 */
	public Track(File audioFile) {
		try {
			clip = AudioSystem.getClip();
			audioInputStream = AudioSystem.getAudioInputStream(audioFile);
		} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns the <code>Clip</code> associated with this object.
	 * @return this <code>Track</code>'s <code>Clip</code>.
	 */
	public Clip getClip() {
		return clip;
	}
	
	/**
	 * Returns the audio input stream of this <code>Track</code>'s audio file.
	 * @return the audio input stream of this <code>Track</code>.
	 */
	public AudioInputStream getAudioInputStream() {
		return audioInputStream;
	}
}
