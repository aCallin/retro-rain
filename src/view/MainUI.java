package view;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import view.util.CursorManager;
import view.util.ViewResources;

/**
 * Represents the top-layer component of the UI, which contains subcomponents such as the <code>OverlayPanel</code>.
 * 
 * @author Adam
 */
@SuppressWarnings("serial")
public class MainUI extends JFrame {
	private static final String TITLE = "RetroRain";
	private static final int MINIMUM_WIDTH = 400;
	private static final int MINIMUM_HEIGHT = 250;
	
	private OverlayPanel overlayPanel;
	
	private CursorManager cursorManager;
	
	/**
	 * Creates a new <code>MainUI</code> instance. Note that this does not create the UI.
	 * @see #create()
	 */
	public MainUI() {}
	
	/**
	 * Creates and shows the actual main UI, creating subcomponents and registering to events as needed. The frame is 
	 * packed and positioned in the middle of the screen.
	 */
	public void create() {
		setTitle(TITLE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setFocusable(false);
		setUndecorated(true);
		setPreferredSize(new Dimension(MINIMUM_WIDTH, MINIMUM_HEIGHT));
		
		setIcon();
		createOverlayPanel();
		createCursorManager();
		setupEventHandling();
		
		pack();
		setLocationRelativeTo(null);
	}
	
	/**
	 * Returns the overlay panel.
	 * @return <code>MainUI</code>'s overlay panel.
	 */
	public OverlayPanel getOverlayPanel() {
		return overlayPanel;
	}
	
	/**
	 * Sets the icon of this window.
	 */
	private void setIcon() {
		setIconImage(ViewResources.get().getImage("icon.png"));
	}
	
	/**
	 * Creates the overlay panel and sets it as this frame's content pane.
	 */
	private void createOverlayPanel() {
		overlayPanel = new OverlayPanel();
		setContentPane(overlayPanel);
	}
	
	/**
	 * Creates and starts the cursor manager.
	 */
	private void createCursorManager() {
		cursorManager = new CursorManager(this);
		cursorManager.start();
	}
	
	/**
	 * Sets up the events that the main UI will have to work with.
	 */
	private void setupEventHandling() {
		overlayPanel.getToolbarPanel().addPropertyChangeListener("toolbarDragged", propertyChange -> 
				onWindowDragged((Point)propertyChange.getNewValue()));
		overlayPanel.addPropertyChangeListener("overlayExtended", propertyChange -> {
			Point extendedInfo = (Point)propertyChange.getNewValue();
			onWindowExtended(extendedInfo.x, extendedInfo.y);
		});
		overlayPanel.getToolbarPanel().getMinimizeButton().addActionListener((ActionEvent e) -> minimize());
		overlayPanel.getToolbarPanel().getExitButton().addActionListener((ActionEvent e) -> close());
	}
	
	/**
	 * Handles the logic for when the window is being dragged around the screen.
	 * @param offset the (x, y) pair of how much the window should move.
	 */
	private void onWindowDragged(Point offset) {
		Point newLocation = new Point((int)(getLocation().getX() + offset.getX()), (int)(getLocation().getY() + 
				offset.getY()));
		setLocation(newLocation);
	}
	
	/**
	 * Handles the logic for when the window is being extended.
	 * @param direction the direction of the extension.
	 * @param amount how much the window should extend in the given direction.
	 */
	private void onWindowExtended(int direction, int amount) {
		// Extend up.
		if (direction == OverlayPanel.DIRECTION_UP) {
			if (getPreferredSize().height + amount < MINIMUM_HEIGHT)
				amount = 0;
			setPreferredSize(new Dimension(getPreferredSize().width, getPreferredSize().height + amount));
			setLocation(new Point(getLocation().x, getLocation().y - amount));
		}
		// Extend left.
		if (direction == OverlayPanel.DIRECTION_LEFT) {
			if (getPreferredSize().width + amount < MINIMUM_WIDTH)
				amount = 0;
			setPreferredSize(new Dimension(getPreferredSize().width + amount, getPreferredSize().height));
			setLocation(new Point(getLocation().x - amount, getLocation().y));
		}
		// Extend down.
		if (direction == OverlayPanel.DIRECTION_DOWN) {
			if (getPreferredSize().height + amount < MINIMUM_HEIGHT)
				amount = 0;
			setPreferredSize(new Dimension(getPreferredSize().width, getPreferredSize().height + amount));
		}
		// Extend right.
		if (direction == OverlayPanel.DIRECTION_RIGHT) {
			if (getPreferredSize().width + amount < MINIMUM_WIDTH)
				amount = 0;
			setPreferredSize(new Dimension(getPreferredSize().width + amount, getPreferredSize().height));
		}
		pack();
	}
	
	/**
	 * Minimizes the window by quickly fading out.
	 */
	private void minimize() {
		Thread thread = new Thread(() -> {
			final float OPACITY_DECREMENT = 0.15f;
			float opacity = getOpacity();
			while (opacity > 0.0f) {
				opacity = Math.max(opacity - OPACITY_DECREMENT, 0);
				setOpacity(opacity);
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			setExtendedState(JFrame.ICONIFIED);
			setOpacity(1.0f);
		});
		thread.start();
	}
	
	/**
	 * Closes the window, terminating the program.
	 */
	private void close() {
		dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}
}
