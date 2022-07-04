package model;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Represents a depot of audio files. An audio file is just a file that can be given to a {@link model.Track} 
 * object and played on a {@link controller.util.TrackPlayer}. A {@link HashMap} is used to store these audio files.
 * 
 * @author Adam
 */
public class AudioFileList {
	private Map<String, File> audioFiles;
	
	/**
	 * Creates a new <code>AudioFileList</code> object.
	 */
	public AudioFileList() {
		audioFiles = new HashMap<String, File>();
	}
	
	/**
	 * Adds an audio file with a given identifier to a map of audio files.
	 * @param identifier the name referring to this particular audio file.
	 * @param audioFile the file to store in the map.
	 */
	public void addAudioFile(String identifier, File audioFile) {
		audioFiles.put(identifier, audioFile);
	}
	
	/**
	 * Receives an audio file based on the name given to it.
	 * @param identifier the name referring to the audio file (i.e., the key).
	 * @return the file with the associated identifier, or null if it does not exist.
	 */
	public File getAudioFile(String identifier) {
		return audioFiles.get(identifier);
	}
	
	/**
	 * Returns a collection of each audio file's identifier.
	 * @return a set representing the keys of all of the audio files.
	 */
	public Set<String> getAllAudioFileIdentifiers() {
		return audioFiles.keySet();
	}
}
