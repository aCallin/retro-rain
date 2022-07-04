package view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import view.util.ControlButton;
import view.util.CustomSlider;
import view.util.GUIConstants;
import view.util.ViewResources;

/**
 * Represents a panel containing the master controls (i.e., the master volume and universal play / pause button). Also 
 * contains a label for displaying the current audio selection.
 * 
 * @author Adam
 */
@SuppressWarnings("serial")
public class ControlPanel extends JPanel {
	private static final int PANEL_HEIGHT = 30;
	
	private SpringLayout layout;
	
	private JLabel label;
	private ControlButton controlButton;	
	private CustomSlider volumeSlider;
	
	/**
	 * Constructs a <code>ControlPanel</code> and all of its subcomponents.
	 */
	public ControlPanel() {
		layout = new SpringLayout();
		setLayout(layout);
		setFocusable(false);
		setBackground(GUIConstants.COLOR_SECONDARY);
		
		Dimension startSize = new Dimension(0, PANEL_HEIGHT);
		setSize(startSize);
		setPreferredSize(startSize);
		
		createLabel();
		createControlButton();
		createVolumeSlider();
		setupEventHandling();
	}
	
	/**
	 * Returns the selection label.
	 * @return the <code>JLabel</code> representing the selection label.
	 */
	public JLabel getLabel() {
		return label;
	}
	
	/**
	 * Returns the control button.
	 * @return the <code>ControlButton</code> representing the control button.
	 */
	public ControlButton getControlButton() {
		return controlButton;
	}
	
	/**
	 * Returns the volume slider.
	 * @return the <code>CustomSlider</code> representing the volume slider.
	 */
	public CustomSlider getVolumeSlider() {
		return volumeSlider;
	}
	
	@Override
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		graphics.setColor(GUIConstants.COLOR_OUTLINE);
		graphics.fillRect(0, 0, getWidth(), GUIConstants.SIZE_THUMB_EDGE);
	}
	
	/**
	 * Creates and adds the selection label to this panel.
	 */
	private void createLabel() {
		label = new JLabel("master controls");
		label.setForeground(GUIConstants.COLOR_OUTLINE);
		label.setFont(ViewResources.get().getFont("prstart.ttf").deriveFont(9.0f));
		add(label);
	}
	
	/**
	 * Creates and adds the control button to this panel.
	 */
	private void createControlButton() {
		controlButton = new ControlButton();
		add(controlButton);
	}
	
	/**
	 * Creates and adds the volume slider to this panel.
	 */
	private void createVolumeSlider() {
		volumeSlider = new CustomSlider(0, 100, 100);
		add(volumeSlider);
	}
	
	/**
	 * Subscribes to events that this panel will need.
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
	 * Places the selection label, control button, and volume slider in their correct spots and revalidates the panel. 
	 * This method should be invoked whenever the panel is resized.
	 */
	private void onPanelResized() {
		placeLabel();
		placeControlButton();
		placeVolumeSlider();
		revalidate();
	}
	
	/**
	 * Places the selection label at the left of the panel.
	 */
	private void placeLabel() {
		int x = GUIConstants.SIZE_PADDING;
		int y = (getHeight() / 2) - (label.getFont().getSize() / 2) + GUIConstants.SIZE_THUMB_EDGE;
		int width = (getWidth() / 2) - (controlButton.getWidth() / 2) - (GUIConstants.SIZE_PADDING * 2);
		
		Dimension size = new Dimension(width, label.getFont().getSize());
		label.setSize(size);
		label.setPreferredSize(size);
		layout.putConstraint(SpringLayout.WEST, label, x, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, label, y, SpringLayout.NORTH, this);
	}
	
	/**
	 * Places the control button in the middle of the panel.
	 */
	private void placeControlButton() {
		int x = (getWidth() / 2) - (controlButton.getWidth() / 2);
		int y = (getHeight() / 2) - (controlButton.getHeight() / 2) + GUIConstants.SIZE_THUMB_EDGE;
		layout.putConstraint(SpringLayout.WEST, controlButton, x, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, controlButton, y, SpringLayout.NORTH, this);
	}
	
	/**
	 * Places the volume slider at the right of the panel.
	 */
	private void placeVolumeSlider() {
		int y = (getHeight() / 2) - (volumeSlider.getHeight() / 2) + GUIConstants.SIZE_THUMB_EDGE;
		layout.putConstraint(SpringLayout.EAST, volumeSlider, -GUIConstants.SIZE_PADDING, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.NORTH, volumeSlider, y, SpringLayout.NORTH, this);
	}
}
