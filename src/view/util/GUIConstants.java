package view.util;

import java.awt.Color;

/**
 * Represents a container for common colors and metrics used in the UI. Note that some UI components might still define 
 * their own specific constants if needed.
 * 
 * @author Adam
 */
public class GUIConstants {
	public static final Color COLOR_PRIMARY = new Color(128, 144, 156);
	public static final Color COLOR_SECONDARY = new Color(213, 201, 192);
	public static final Color COLOR_OUTLINE = Color.BLACK;
	public static final Color COLOR_SHINE = new Color(255, 246, 232);
	
	public static final int SIZE_PADDING = 5;
	public static final int SIZE_SHINE = 2;
	public static final int SIZE_THUMB_EDGE = 2;
	
	private GUIConstants() {}
}
