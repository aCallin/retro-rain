package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import view.util.ViewResources;
import view.util.CustomButton;
import view.util.GUIConstants;

/**
 * Represents the toolbar.
 * <p>The toolbar consists of a title label along with a minimize, maximize, and exit button. It also supports dragging.
 * 
 * @author Adam
 */
@SuppressWarnings("serial")
public class ToolbarPanel extends JPanel {
	private static final int TOOLBAR_HEIGHT = 30;
	
	private SpringLayout layout;
	
	private JLabel titleLabel;
	private CustomButton minimizeButton;
	private CustomButton maximizeButton;
	private CustomButton exitButton;
	
	private Point initialMousePosition;
	private boolean leftMouseButtonDown;
	
	/**
	 * Creates a new <code>ToolbarPanel</code> instance along with its subcomponents.
	 */
	public ToolbarPanel() {
		layout = new SpringLayout();
		setLayout(layout);
		setBackground(GUIConstants.COLOR_PRIMARY);
		setFocusable(false);
		Dimension startSize = new Dimension(0, TOOLBAR_HEIGHT);
		setSize(startSize);
		setPreferredSize(startSize);
		
		createTitleLabel();
		createButtons();
		setupEventHandling();
	}
	
	/**
	 * Returns the title label.
	 * @return the {@link JLabel} representing the title.
	 */
	public JLabel getTitleLabel() {
		return titleLabel;
	}
	
	/**
	 * Returns the minimize button.
	 * @return the {@link CustomButton} representing the minimize button.
	 */
	public CustomButton getMinimizeButton() {
		return minimizeButton;
	}
	
	/**
	 * Returns the maximize button.
	 * @return the {@link CustomButton} representing the maximize button.
	 */
	public CustomButton getMaximizeButton() {
		return maximizeButton;
	}
	
	/**
	 * Returns the exit button.
	 * @return the {@link CustomButton} representing the exit button.
	 */
	public CustomButton getExitButton() {
		return exitButton;
	}
	
	@Override
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		
		// Paint a shine effect on the top and left edges of the panel.
		graphics.setColor(GUIConstants.COLOR_SHINE);
		graphics.fillRect(0, 0, getWidth(), GUIConstants.SIZE_SHINE);
		graphics.fillRect(0, 0, GUIConstants.SIZE_SHINE, getHeight());
	}
	
	/**
	 * Creates the title label and adds it to the panel.
	 */
	private void createTitleLabel() {
		titleLabel = new JLabel("RetroRain.exe");
		titleLabel.setFont(ViewResources.get().getFont("prstart.ttf").deriveFont(12.0f));
		titleLabel.setForeground(Color.WHITE);
		add(titleLabel);
	}
	
	/**
	 * Creates the minimize, maximize, and exit buttons and adds them to the panel.
	 */
	private void createButtons() {
		minimizeButton = new CustomButton(ViewResources.get().getImage("button_minimize.png"));
		maximizeButton = new CustomButton(ViewResources.get().getImage("button_maximize.png"));
		exitButton = new CustomButton(ViewResources.get().getImage("button_exit.png"));
		add(minimizeButton);
		add(maximizeButton);
		add(exitButton);
	}
	
	/**
	 * Sets up the few events that this panel will work with.
	 */
	private void setupEventHandling() {		
		// The event that the toolbar has been resized.
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				onPanelResized();
			}
		});

		// The event that the toolbar has been dragged by the user.
		leftMouseButtonDown = false;
		MouseAdapter mouseAdapter = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					initialMousePosition = e.getPoint();
					leftMouseButtonDown = true;
				}
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				leftMouseButtonDown = false;
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				if (leftMouseButtonDown) { // Unfortunately needed since e.getButton() doesn't work here.
					Point currentMousePosition = e.getPoint();
					Point offset = new Point((int)(currentMousePosition.getX() - initialMousePosition.getX()), 
							(int)(currentMousePosition.getY() - initialMousePosition.getY()));
					firePropertyChange("toolbarDragged", null, offset);
				}
			}
		};
		addMouseListener(mouseAdapter);
		addMouseMotionListener(mouseAdapter);
	}
	
	/**
	 * Dictates what happens whenever the toolbar is resized.
	 */
	private void onPanelResized() {
		placeButtons();
		placeTitleLabel();
		revalidate();
	}
	
	/**
	 * Places the title label in its correct spot and calculates how wide the label can be. 
	 * 
	 * <p>The width of the title label is based on how much real estate is left over after the buttons have been 
	 * placed, so {@link #placeButtons()} should be invoked first. The reason for this is that the label's text should 
	 * never appear over the buttons.
	 */
	private void placeTitleLabel() {
		int availableWidth = getWidth() - (int)(exitButton.getSize().getWidth() * 3) - (GUIConstants.SIZE_PADDING * 5);
		Dimension titleLabelSize = new Dimension(availableWidth, titleLabel.getFont().getSize());
		titleLabel.setSize(titleLabelSize);
		titleLabel.setPreferredSize(titleLabelSize);
		layout.putConstraint(SpringLayout.NORTH, titleLabel, (getHeight() / 2) - (titleLabel.getFont().getSize() / 2), 
				SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, titleLabel, GUIConstants.SIZE_PADDING, SpringLayout.WEST, this);
	}
	
	/**
	 * Places the exit, maximize, and minimize buttons in their correct spots.
	 */
	private void placeButtons() {
		layout.putConstraint(SpringLayout.NORTH, exitButton, (getHeight() / 2) - (exitButton.getHeight() / 2), 
				SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.EAST, exitButton, -GUIConstants.SIZE_PADDING, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.NORTH, maximizeButton, 0, SpringLayout.NORTH, exitButton);
		layout.putConstraint(SpringLayout.EAST, maximizeButton, -GUIConstants.SIZE_PADDING, SpringLayout.WEST, 
				exitButton);
		layout.putConstraint(SpringLayout.NORTH, minimizeButton, 0, SpringLayout.NORTH, exitButton);
		layout.putConstraint(SpringLayout.EAST, minimizeButton, -GUIConstants.SIZE_PADDING, SpringLayout.WEST, 
				maximizeButton);
	}
}
