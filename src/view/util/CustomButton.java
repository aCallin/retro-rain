package view.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * Represents a custom button characterized by its empty border and mouse hovering / pressing effects. Icon buttons are 
 * the main goal of this custom button.
 * 
 * @author Adam
 */
@SuppressWarnings("serial")
public class CustomButton extends JButton {
	private static final Color PRESSED_TINT = new Color(0, 0, 0, 20);
	private static final Color HOVERED_TINT = new Color(255, 255, 255, 35);
	
	private boolean pressed;
	private boolean hovered;
	
	/**
	 * Creates a new <code>CustomButton</code> instance which displays an icon.
	 * @param icon the icon for this button to display.
	 */
	public CustomButton(Image icon) {
		this(new ImageIcon(icon));
	}
	
	/**
	 * Creates a new <code>CustomButton</code> instance which displays an icon.
	 * @param icon the icon for this button to display.
	 */
	public CustomButton(ImageIcon icon) {
		super(icon);
		setFocusPainted(false);
		setupBounds();
		setupMouseEvents();
	}
	
	@Override
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		
		if (pressed) {
			graphics.setColor(PRESSED_TINT);
			graphics.fillRect(0, 0, getWidth(), getHeight());
		} else if (hovered) {
			graphics.setColor(HOVERED_TINT);
			graphics.fillRect(0, 0, getWidth(), getHeight());
		}
	}
	
	/**
	 * Shrinks the size of this button to the size of its icon and gives it an empty border. 
	 */
	private void setupBounds() {
		Dimension size = new Dimension(getIcon().getIconWidth(), getIcon().getIconHeight());
		setSize(size);
		setPreferredSize(size);
		setBorder(BorderFactory.createEmptyBorder());
	}
	
	/**
	 * Listens to interesting mouse events to tell us if the button is currently being hovered over or pressed.
	 */
	private void setupMouseEvents() {
		pressed = false;
		hovered = false;
		MouseAdapter mouseEvents = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				pressed = true;
			}

		    @Override
		    public void mouseReleased(MouseEvent e) {
	    		pressed = false;
		    }

		    @Override
		    public void mouseEntered(MouseEvent e) {
	    		hovered = true;
		    }

		    @Override
		    public void mouseExited(MouseEvent e) {
	    		hovered = false;
		    	pressed = false;
		    }
		};
		addMouseListener(mouseEvents);
	}
}
