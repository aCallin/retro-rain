package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import controller.util.TrackPlayer;
import view.util.ControlButton;
import view.util.CustomSlider;
import view.util.GUIConstants;
import view.util.ViewResources;

/**
 * Graphically represents a user selection. This panel contains a play / pause button (the control button), a label 
 * describing the user selection, and a volume slider.
 * 
 * @author Adam
 */
@SuppressWarnings("serial")
public class UserSelectionPanel extends JPanel {
	public static final int PANEL_HEIGHT = 30;
	
	private static boolean LOADED_RESOURCES = false;
	private static Font FONT;
	
	private SpringLayout layout;
	
	private ControlButton controlButton;
	private JLabel nameLabel;
	private CustomSlider volumeSlider;
	
	private TrackPlayer trackPlayer;
	
	/**
	 * Constructs a new <code>UserSelectionPanel</code> object, setting up its control button, name label, and 
	 * volume slider.
	 * @param audioLabel the text for the label to present.
	 */
	public UserSelectionPanel(String audioName) {
		layout = new SpringLayout();
		setLayout(layout);
		setFocusable(false);
		setBackground(GUIConstants.COLOR_SECONDARY);
		
		if (!LOADED_RESOURCES) {
			loadResources();
			LOADED_RESOURCES = true;
		}
		
		createControlButton();
		createNameLabel(audioName);
		createVolumeSlider();
		setupEventHandling();
		
		trackPlayer = new TrackPlayer();
	}
	
	/**
	 * Returns the control button.
	 * @return the <code>ControlButton</code> associated with the control button.
	 */
	public ControlButton getControlButton() {
		return controlButton;
	}
	
	/**
	 * Returns the name label.
	 * @return the <code>JLabel</code> associated with this user selection's name.
	 */
	public JLabel getNameLabel() {
		return nameLabel;
	}
	
	/**
	 * Returns the volume slider.
	 * @return the <code>CustomSlider</code> associated with the volume slider.
	 */
	public CustomSlider getVolumeSlider() {
		return volumeSlider;
	}
	
	/**
	 * Return the track player.
	 * @return the <code>TrackPlayer</code> associated with this user selection.
	 */
	public TrackPlayer getTrackPlayer() {
		return trackPlayer;
	}
	
	/**
	 * Loads resources that are common to all <code>UserSelectionPanel</code> objects.
	 */
	private void loadResources() {
		FONT = ViewResources.get().getFont("prstart.ttf").deriveFont(12.0f);
		LOADED_RESOURCES = true;
	}
	
	/**
	 * Creates and adds the control button to this panel.
	 */
	private void createControlButton() {
		controlButton = new ControlButton();
		add(controlButton);
	}
	
	/**
	 * Creates and adds the name label to this panel.
	 * @param text the initial text of the name label.
	 */
	private void createNameLabel(String text) {
		nameLabel = new JLabel(text);
		nameLabel.setFont(FONT);
		nameLabel.setForeground(Color.BLACK);
		add(nameLabel);
	}
	
	/**
	 * Creates and adds the volume slider to this panel.
	 */
	private void createVolumeSlider() {
		volumeSlider = new CustomSlider(0, 100, 100);
		add(volumeSlider);
	}
	
	/**
	 * Sets up the events that this panel will need to receive or notify.
	 */
	private void setupEventHandling() {
		// The event that this panel was resized.
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				onPanelResized();
			}
		});
	}
	
	/**
	 * The actions to be performed whenever this panel is resized.
	 */
	private void onPanelResized() {
		placeControlButton();
		placeVolumeSlider();
		placeNameLabel();
		revalidate();
	}
	
	/**
	 * Places the control button in its correct spot.
	 */
	private void placeControlButton() {
		layout.putConstraint(SpringLayout.NORTH, controlButton, (getHeight() / 2) - 
				(controlButton.getSize().height / 2), SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, controlButton, GUIConstants.SIZE_PADDING, SpringLayout.WEST, this);
	}
	
	/**
	 * Places the name label in its correct spot and adjusts its width.
	 * 
	 * <p>The width of the name label is based on the free space between the control button and the volume slider, so 
	 * {@link #placeControlButton()} and {@link #placeVolumeSlider()} should be invoked before this method.
	 */
	private void placeNameLabel() {
		int width = getWidth() - controlButton.getWidth() - volumeSlider.getWidth() - (GUIConstants.SIZE_PADDING * 4);
		Dimension size = new Dimension(width, nameLabel.getPreferredSize().height);
		nameLabel.setSize(size);
		nameLabel.setPreferredSize(size);
		layout.putConstraint(SpringLayout.NORTH, nameLabel, (getHeight() / 2) - (nameLabel.getSize().height / 2), 
				SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, nameLabel, GUIConstants.SIZE_PADDING, SpringLayout.EAST, controlButton);
	}
	
	/**
	 * Places the volume slider in its correct spot.
	 */
	private void placeVolumeSlider() {
		layout.putConstraint(SpringLayout.NORTH, volumeSlider, (getHeight() / 2) - (volumeSlider.getSize().height / 2), 
				SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.EAST, volumeSlider, -GUIConstants.SIZE_PADDING, SpringLayout.EAST, this);
	}
}
