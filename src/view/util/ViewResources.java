package view.util;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

/**
 * Represents the resource pools needed by UI components.
 * 
 * @author Adam
 */
public class ViewResources {
	private static ViewResources instance;
	
	private Map<String, Image> imagePool;
	private Map<String, Font> fontPool;
	
	/**
	 * Creates a new <code>ViewResources</code> instance. Private for the Singleton pattern.
	 */
	private ViewResources() {
		imagePool = new HashMap<String, Image>();
		fontPool = new HashMap<String, Font>();
	}
	
	/**
	 * Returns the Singleton instance of this class.
	 * @return the single <code>ViewResources</code> instance.
	 */
	public static ViewResources get() {
		if (instance == null)
			instance = new ViewResources();
		return instance;
	}
	
	/**
	 * Loads each image from a folder / directory given it has a supported extension. Note that when the image is added 
	 * to the image pool, the identifier given to it is determined by {@link File#getName()} of the image file.
	 * @param directory where to load the images from.
	 * @param supportedExtensions a list of file extensions supported (ex: {".jpg", ".png"}).
	 */
	public void loadImagesFromDirectory(String directory, String[] supportedExtensions) {
		File imagesDirectory = new File(directory);
		File[] imageFiles = imagesDirectory.listFiles((File file, String name) -> {
			boolean hasSupportedExtension = false;
			for (String extension : supportedExtensions) {
				if (name.endsWith(extension)) {
					hasSupportedExtension = true;
					break;
				}
			}
			return hasSupportedExtension;
		});
		
		for (File imageFile : imageFiles) {
			try {
				imagePool.put(imageFile.getName(), ImageIO.read(imageFile));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Loads each font from a folder / directory given it has a supported extension. Note that when the font is added 
	 * to the font pool, the identifier given to it is determined by {@link File#getName()} of the font file.
	 * @param directory where to load the fonts from.
	 * @param supportedExtensions a list of font extensions supported (ex: {".ttf"}).
	 */
	public void loadFontsFromDirectory(String directory, String[] supportedExtensions) {
		File fontsDirectory = new File(directory);
		File[] fontFiles = fontsDirectory.listFiles((File file, String name) -> {
			boolean hasSupportedExtension = false;
			for (String extension : supportedExtensions) {
				if (name.endsWith(extension)) {
					hasSupportedExtension = true;
					break;
				}
			}
			return hasSupportedExtension;
		});
		
		for (File fontFile : fontFiles) {
			try {
				fontPool.put(fontFile.getName(), Font.createFont(Font.TRUETYPE_FONT, fontFile));
			} catch (FontFormatException | IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Returns an image from the image pool.
	 * @param identifier the name given to the image when it was added to the pool.
	 * @return the <code>Image</code> associated with the key <code>identifier</code>, or <code>null</code> if it does 
	 * not exist.
	 * @throws NullPointerException thrown if the image associated with that identifier does not exist in the image 
	 * pool.
	 */
	public Image getImage(String identifier) {
		Image result = imagePool.get(identifier);
		if (result == null)
			throw new NullPointerException("An image does not exist in the image pool with the identifier \"" + 
					identifier + "\"");
		return result;
	}
	
	/**
	 * Returns a font from the font pool.
	 * @param identifier the name given to the font when it was added to the pool.
	 * @return the <code>Font</code> associated with the key <code>identifier</code>, or <code>null</code> if it does 
	 * not exist.
	 * @throws NullPointerException thrown if the font associated with that identifier does not exist in the font pool.
	 */
	public Font getFont(String identifier) {
		Font result = fontPool.get(identifier);
		if (result == null)
			throw new NullPointerException("A font does not exist in the font pool with the identifier \"" + 
					identifier + "\"");
		return result;
	}
}
