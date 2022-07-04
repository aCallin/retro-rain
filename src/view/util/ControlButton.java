package view.util;

import javax.swing.ImageIcon;

/**
 * Represents a button that switches between a play and pause state.
 * 
 * @author Adam
 */
@SuppressWarnings("serial")
public class ControlButton extends CustomButton {
	/**
	 * An enum representing the current mode (play or pause) of a control button.
	 * 
	 * @author Adam
	 */
	public static enum ControlMode { PLAY, PAUSE }
	
	private static boolean LOADED_RESOURCES = false;
	private static ImageIcon ICON_PLAY;
	private static ImageIcon ICON_PAUSE;
	
	private ControlMode mode;
	
	/**
	 * Loads the play and pause icons that will be used for <code>ControlButton</code>s.
	 */
	public static void init() {
		if (!LOADED_RESOURCES) {
			loadResources();
			LOADED_RESOURCES = true;
		}
	}
	
	/**
	 * Creates a new <code>ControlButton</code>, with the default state being <code>ControlMode.PAUSE</code>.
	 */
	public ControlButton() {
		super(ICON_PLAY);
		mode = ControlMode.PAUSE;
	}
	
	/**
	 * Switches from <code>ControlMode.PLAY</code> to <code>ControlMode.PAUSE</code> or vice versa.
	 */
	public void switchMode() {
		if (mode == ControlMode.PLAY) {
			mode = ControlMode.PAUSE;
			setIcon(ICON_PLAY);
		} else {
			mode = ControlMode.PLAY;
			setIcon(ICON_PAUSE);
		}
	}
	
	/**
	 * Sets the mode of this <code>ControlButton</code>.
	 * @param mode either <code>ControlMode.PLAY</code> or <code>ControlMode.PAUSE</code>. Note that if 
	 * <code>mode</code> equals this button's current mode, then no effect occurs.
	 */
	public void setMode(ControlMode mode) {
		if (mode != this.mode)
			switchMode();
	}
	
	/**
	 * Returns this control button's mode.
	 * @return either <code>ControlMode.PLAY</code> or <code>ControlMode.PAUSE</code>.
	 */
	public ControlMode getMode() {
		return mode;
	}
	
	/**
	 * Loads the play and pause icons.
	 */
	private static void loadResources() {
		ICON_PLAY = new ImageIcon(ViewResources.get().getImage("button_play.png"));
		ICON_PAUSE = new ImageIcon(ViewResources.get().getImage("button_pause.png"));
	}
}
