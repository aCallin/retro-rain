package controller.util;

import java.io.IOException;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;

import model.Track;

/**
 * Represents an audio player for <code>Track</code> objects.
 * 
 * @author Adam
 */
public class TrackPlayer {
	public static final int PLAY_INDEFINITELY = 0;
	
	private static final int LOW_VOLUME = -30;
	
	private Track track;
	private float startVolume;
	private FloatControl gainControl;
	
	/**
	 * Creates a new <code>TrackPlayer</code> instance.
	 */
	public TrackPlayer() {
		startVolume = 1.0f;
	}
	
	/**
	 * Sets the <code>Track</code> object that this player can play or stop.
	 * @param track the <code>Track</code> to operate on.
	 */
	public void setTrack(Track track) {
		if (this.track != null)
			stop();
		this.track = track;
	}
	
	/**
	 * Plays a track the given amount of times. 
	 * <p>If a track has not yet been set, or one has been playing but the <code>stop</code> method was invoked, then 
	 * the <code>setTrack</code> method will need to be called first for this method to have any effect.
	 * @param times how many times to play the track. A constant is defined for playing indefinitely. 
	 */
	public void play(int times) {
		Clip clip = track.getClip();
		if (times >= 0 && !clip.isOpen()) {
			try {
				clip.open(track.getAudioInputStream());
				gainControl = (FloatControl)track.getClip().getControl(FloatControl.Type.MASTER_GAIN);
				if (startVolume != 1.0f)
					setVolume(startVolume);
				clip.loop(times - 1);
			} catch (LineUnavailableException | IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Stops and closes a track if it has already been opened via the <code>play</code> method. Note that once a track 
	 * is closed it may not be possible to reopen it. In this case, the <code>setTrack</code> method should be invoked 
	 * first.
	 */
	public void stop() {
		Clip clip = track.getClip();
		if (clip.isOpen()) {
			clip.stop();
			clip.close();
		}
	}
	
	/**
	 * Sets the volume of this particular <code>TrackPlayer</code>. 
	 * @param volume a value between 0 and 1, where 0 is no volume and 1 is full volume.
	 */
	public void setVolume(float volume) {
		if (volume < 0 || volume > 1)
			throw new IllegalArgumentException("volume must be a value between 0 and 1");
		
		if (track == null)
			startVolume = volume;
		else {
			if (volume == 0)
				gainControl.setValue(gainControl.getMinimum());
			else {
				float value = LOW_VOLUME + (-LOW_VOLUME * volume);
				gainControl.setValue(value);
			}
		}
	}
}
