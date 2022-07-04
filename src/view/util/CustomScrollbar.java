package view.util;

import javax.swing.JButton;
import javax.swing.JScrollBar;

@SuppressWarnings("serial")
/**
 * Represents a custom <code>JScrollBar</code> with a retro style.
 * 
 * @author Adam
 */
public class CustomScrollbar extends JScrollBar {
	private JButton incrementButton;
	private JButton decrementButton;
	
	/**
	 * Constructs a new <code>CustomScrollbar</code> object with a specified orientation.
	 * @param orientation the orientation of the scrollbar.
	 */
	public CustomScrollbar(int orientation) {
		super(orientation);
	}
	
	/**
	 * Sets the increment and decrement buttons of this scrollbar (for some reason only the scrollbar UI knows about 
	 * them). 
	 * @param incrementButton the increment (bottom) button of the scrollbar.
	 * @param decrementButton the decrement (top) button of the scrollbar.
	 */
	public void setButtons(JButton incrementButton, JButton decrementButton) {
		this.incrementButton = incrementButton;
		this.decrementButton = decrementButton;
	}
	
	/**
	 * Returns the increment / bottom button of this scrollbar.
	 * @return the <code>JButton</code> responsible for incrementing the value of the scrollbar.
	 */
	public JButton getIncrementButton() {
		return incrementButton;
	}
	
	/**
	 * Returns the decrement / top button of this scrollbar.
	 * @return the <code>JButton</code> responsible for decrementing the value of the scrollbar.
	 */
	public JButton getDecrementButton() {
		return decrementButton;
	}
}
