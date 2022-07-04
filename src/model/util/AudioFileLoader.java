package model.util;

import java.io.File;

import model.AudioFileList;

/**
 * Used to load all audio files from a directory and store them in an <code>AudioFileList</code>. 
 * 
 * @author Adam
 */
public class AudioFileLoader {
	private static AudioFileLoader instance;
	
	/**
	 * Constructs a new <code>AudioFileLoader</code> object. Private for the Singleton pattern.
	 */
	private AudioFileLoader() {}
	
	/**
	 * Returns the Singleton instance of this class.
	 * @return an <code>AudioFileLoader</code> instance.
	 */
	public static AudioFileLoader get() {
		if (instance == null)
			instance = new AudioFileLoader();
		return instance;
	}
	
	/**
	 * Loads all files ending in a valid audio file extension and stores it in an <code>AudioFileList</code>.  
	 * @param directory the directory containing the audio files.
	 * @param supportedExtensions a list of valid audio file extensions (ex: ".wav", ".mp3").
	 * @param audioFileList where to store the audio files. The identifier given to the audio file is determined by the 
	 * method {@link File#getName()}.
	 */
	public void loadFromDirectory(String directory, String supportedExtensions[], AudioFileList audioFileList) {
		File audioFilesDirectory = new File(directory);
		File[] audioFiles = audioFilesDirectory.listFiles((File file, String name) -> {
			boolean hasSupportedExtension = false;
			for (String extension : supportedExtensions) {
				if (name.endsWith(extension)) {
					hasSupportedExtension = true;
					break;
				}
			}
			return hasSupportedExtension;
		});
		
		for (File audioFile : audioFiles)
			audioFileList.addAudioFile(audioFile.getName(), audioFile);
	}
}
