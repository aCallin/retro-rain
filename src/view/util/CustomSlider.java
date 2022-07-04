package view.util;

import java.awt.Dimension;

import javax.swing.JSlider;

/**
 * Represents a custom <code>JSlider</code> that is shorter in width and has a retro-styled UI.
 * 
 * @author Adam
 */
@SuppressWarnings("serial")
public class CustomSlider extends JSlider {	
	private static final int WIDTH = 75;
	private static final int HEIGHT = 20;
	
	/**
	 * Creates a new <code>CustomSlider</code> instance and sets the UI to a new <code>CustomSliderUI</code> instance.
	 * @param min the minimum value of the slider.
	 * @param max the maximum value of the slider.
	 * @param value the starting value of the slider.
	 */
	public CustomSlider(int min, int max, int value) {
		super(min, max, value);
		
		Dimension size = new Dimension(WIDTH, HEIGHT);
		setSize(size);
		setPreferredSize(size);
		setBackground(GUIConstants.COLOR_SECONDARY);
		setUI(new CustomSliderUI(this));
	}
}
