package view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.SpringLayout;

import view.util.GUIConstants;

/**
 * Represents the top-most panel of the UI. This panel is mainly comprised of the toolbar panel and the content panel, 
 * though also does its own custom painting and event triggering.
 * 
 * @author Adam
 */
@SuppressWarnings("serial")
public class OverlayPanel extends JPanel {
	public static final int DIRECTION_UP = 0;
	public static final int DIRECTION_LEFT = 1;
	public static final int DIRECTION_DOWN = 2;
	public static final int DIRECTION_RIGHT = 3;
	
	public static final int EXTENDED_NONE = 0;
	public static final int EXTENDED_HORIZONTAL = 1;
	public static final int EXTENDED_VERTICAL = 2;
	public static final int EXTENDED_BIDIRECTIONAL = 3;
	
	private static final int OUTER_BORDER_SIZE = 6;
	private static final int INNER_BORDER_SIZE = 3;
	
	private SpringLayout layout;
	
	private ToolbarPanel toolbarPanel;
	private ContentPanel contentPanel;
	private ControlPanel controlPanel;
	
	private boolean leftMouseButtonDown;
	private Point initialMousePosition;
	private Point lastMousePosition;
	private Point movedMousePosition;
	private int bottom;
	private int right;
	
	/**
	 * Constructs a new <code>OverlayPanel</code> instance, also creating the toolbar panel and content panel. 
	 */
	public OverlayPanel() {
		layout = new SpringLayout();
		setLayout(layout);
		setFocusable(false);
		setBackground(GUIConstants.COLOR_SECONDARY);
		
		createToolbarPanel();
		createContentPanel();
		createControlPanel();
		setupEventHandling();
	}
	
	/**
	 * Returns the toolbar panel.
	 * @return the <code>ToolbarPanel</code> object representing the toolbar.
	 */
	public ToolbarPanel getToolbarPanel() {
		return toolbarPanel;
	}
	
	/**
	 * Returns the content panel.
	 * @return the <code>ContentPanel</code> object representing the content panel.
	 */
	public ContentPanel getContentPanel() {
		return contentPanel;
	}
	
	/**
	 * Returns the control panel.
	 * @return the <code>ControlPanel</code> object representing the control panel.
	 */
	public ControlPanel getControlPanel() {
		return controlPanel;
	}
	
	/**
	 * Returns the type of extending that occured, if at all, on this panel.
	 * 
	 * <p>This is needed since in the event that the panel is being extended, each direction is handled individually. 
	 * This method goes one step further to determine if the extending is horizontal, vertical, or bidirectional (i.e., 
	 * in the corners).
	 * @return either EXTENDED_BIDIRECTIONAL, EXTENDED_HORIZONTAL, EXTENDED_VERTICAL, or EXTENDED_NONE.
	 */
	public int getExtendedType() {
		boolean extendedHorizontal = (movedMousePosition.x <= OUTER_BORDER_SIZE + GUIConstants.SIZE_PADDING) || 
				(movedMousePosition.x >= getWidth() - OUTER_BORDER_SIZE - GUIConstants.SIZE_PADDING);
		boolean extendedVertical = (movedMousePosition.y <= OUTER_BORDER_SIZE) || (movedMousePosition.y >= 
				getHeight() - OUTER_BORDER_SIZE - GUIConstants.SIZE_PADDING);
		
		if (extendedHorizontal && extendedVertical)
			return EXTENDED_BIDIRECTIONAL;
		else if (extendedHorizontal)
			return EXTENDED_HORIZONTAL;
		else if (extendedVertical)
			return EXTENDED_VERTICAL;
		else
			return EXTENDED_NONE;
	}
	
	@Override
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		drawOuterBorder(graphics);
		drawInnerBorder(graphics);
		drawShine(graphics);
	}
	
	/**
	 * Creates the toolbar panel and adds it to this panel.
	 */
	private void createToolbarPanel() {
		toolbarPanel = new ToolbarPanel();
		add(toolbarPanel);
	}
	
	/**
	 * Creates the content panel and adds it to this panel.
	 */
	private void createContentPanel() {
		contentPanel = new ContentPanel();
		add(contentPanel);
	}
	
	/**
	 * Creates the control panel and adds it to this panel.
	 */
	private void createControlPanel() {
		controlPanel = new ControlPanel();
		add(controlPanel);
	}
	
	/**
	 * Sets up the various events that this panel will need to either subscribe to or notify about.
	 */
	private void setupEventHandling() {
		// The event that the panel was resized.
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				onPanelResized();
			}
		});
		
		// The event that the user is dragging the panel from the edges. This is to resize the UI.
		leftMouseButtonDown = false;
		movedMousePosition = new Point();
		MouseAdapter mouseAdapter = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					leftMouseButtonDown = true;
					initialMousePosition = e.getPoint();
					lastMousePosition = e.getPoint();
					bottom = getHeight() - OUTER_BORDER_SIZE - GUIConstants.SIZE_PADDING;
					right = getWidth() - OUTER_BORDER_SIZE - GUIConstants.SIZE_PADDING;
				}
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1)
					leftMouseButtonDown = false;
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				if (leftMouseButtonDown) {
					Point currentMousePosition = e.getPoint();
					Point offset = new Point((int)(currentMousePosition.getX() - initialMousePosition.getX()), 
							(int)(currentMousePosition.getY() - initialMousePosition.getY()));
					Point velocity = new Point(currentMousePosition.x - lastMousePosition.x, currentMousePosition.y - 
							lastMousePosition.y);
					lastMousePosition = e.getPoint();
					
					// Extend up.
					if (initialMousePosition.y <= OUTER_BORDER_SIZE)
						firePropertyChange("overlayExtended", null, new Point(DIRECTION_UP, -offset.y));
					// Extend left.
					if (initialMousePosition.x <= OUTER_BORDER_SIZE + GUIConstants.SIZE_PADDING)
						firePropertyChange("overlayExtended", null, new Point(DIRECTION_LEFT, -offset.x));
					// Extend down.
					if (initialMousePosition.y >= bottom) {
						if (currentMousePosition.y >= getHeight() - OUTER_BORDER_SIZE - GUIConstants.SIZE_PADDING || 
								velocity.y < 0) {
							firePropertyChange("overlayExtended", null, new Point(DIRECTION_DOWN, velocity.y));
						}
					}
					// Extend right.
					if (initialMousePosition.x >= right) {
						if (currentMousePosition.x >= getWidth() - OUTER_BORDER_SIZE - GUIConstants.SIZE_PADDING || 
								velocity.x < 0) {
							firePropertyChange("overlayExtended", null, new Point(DIRECTION_RIGHT, velocity.x));
						}
					}
				}
			}
			
			@Override
			public void mouseMoved(MouseEvent e) {
				movedMousePosition = e.getPoint();
			}
		};
		addMouseListener(mouseAdapter);
		addMouseMotionListener(mouseAdapter);
	}
	
	/**
	 * The actions to take place when this panel is resized.
	 */
	private void onPanelResized() {
		placeToolbarPanel();
		placeControlPanel();
		placeContentPanel();
		revalidate();
	}
	
	/**
	 * Determines the size of the toolbar panel and places it in its correct spot.
	 */
	private void placeToolbarPanel() {
		layout.putConstraint(SpringLayout.WEST, toolbarPanel, OUTER_BORDER_SIZE, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, toolbarPanel, OUTER_BORDER_SIZE, SpringLayout.NORTH, this);
		Dimension toolbarSize = new Dimension(getWidth() - (OUTER_BORDER_SIZE * 2), toolbarPanel.getHeight());
		toolbarPanel.setSize(toolbarSize);
		toolbarPanel.setPreferredSize(toolbarSize);
	}
	
	/**
	 * Determines the size of the content panel and places it in its correct spot.
	 * 
	 * <p>Note that the size of the content panel depends on the size of the control panel, so 
	 * {@link #placeControlPanel()} should be invoked first.
	 */
	private void placeContentPanel() {
		int x = OUTER_BORDER_SIZE + GUIConstants.SIZE_PADDING + INNER_BORDER_SIZE;
		int y = OUTER_BORDER_SIZE + toolbarPanel.getHeight() + INNER_BORDER_SIZE;
		int width = getWidth() - (OUTER_BORDER_SIZE * 2) - (GUIConstants.SIZE_PADDING * 2) - (INNER_BORDER_SIZE * 2);
		int height = getHeight() - (OUTER_BORDER_SIZE * 2) - toolbarPanel.getHeight() - GUIConstants.SIZE_PADDING - 
				(INNER_BORDER_SIZE * 2) - controlPanel.getHeight();
		
		Dimension contentSize = new Dimension(width, height);
		contentPanel.setSize(contentSize);
		contentPanel.setPreferredSize(contentSize);
		layout.putConstraint(SpringLayout.WEST, contentPanel, x, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, contentPanel, y, SpringLayout.NORTH, this);
	}
	
	/**
	 * Determines the size of the control panel and places it in its correct spot. 
	 */
	private void placeControlPanel() {
		int width = getWidth() - (OUTER_BORDER_SIZE * 2) - (GUIConstants.SIZE_PADDING * 2) - (INNER_BORDER_SIZE * 2);
		
		Dimension size = new Dimension(width, controlPanel.getHeight());
		controlPanel.setSize(size);
		controlPanel.setPreferredSize(size);
		layout.putConstraint(SpringLayout.WEST, controlPanel, OUTER_BORDER_SIZE + GUIConstants.SIZE_PADDING + 
				INNER_BORDER_SIZE, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.SOUTH, controlPanel, -(OUTER_BORDER_SIZE + GUIConstants.SIZE_PADDING + 
				INNER_BORDER_SIZE), SpringLayout.SOUTH, this);
	}
	
	/**
	 * Paints the outer border of this panel.
	 * @param graphics the graphics context.
	 */
	private void drawOuterBorder(Graphics graphics) {
		graphics.setColor(GUIConstants.COLOR_OUTLINE);
		
		// Top, left, bottom, right.
		graphics.fillRect(0, 0, getWidth(), OUTER_BORDER_SIZE);
		graphics.fillRect(0, 0, OUTER_BORDER_SIZE, getHeight());
		graphics.fillRect(0, getHeight() - OUTER_BORDER_SIZE, getWidth(), OUTER_BORDER_SIZE);
		graphics.fillRect(getWidth() - OUTER_BORDER_SIZE, 0, OUTER_BORDER_SIZE, getHeight());
	}
	
	/**
	 * Draws the inner border of this panel.
	 * @param graphics the graphics context.
	 */
	private void drawInnerBorder(Graphics graphics) {
		graphics.setColor(GUIConstants.COLOR_OUTLINE);
		
		// Top.
		int x = OUTER_BORDER_SIZE + GUIConstants.SIZE_PADDING + INNER_BORDER_SIZE;
		int y = OUTER_BORDER_SIZE + toolbarPanel.getHeight();
		int width = getWidth() - (OUTER_BORDER_SIZE * 2) - (GUIConstants.SIZE_PADDING * 2) - (INNER_BORDER_SIZE * 2);
		int height = INNER_BORDER_SIZE;
		graphics.fillRect(x, y, width, height);
		
		// Left.
		x = OUTER_BORDER_SIZE + GUIConstants.SIZE_PADDING;
		y = OUTER_BORDER_SIZE + toolbarPanel.getHeight() + INNER_BORDER_SIZE;
		width = INNER_BORDER_SIZE;
		height = getHeight() - (OUTER_BORDER_SIZE * 2) - toolbarPanel.getHeight() - GUIConstants.SIZE_PADDING - 
				(INNER_BORDER_SIZE * 2);
		graphics.fillRect(x, y, width, height);
		
		// Bottom.
		x = OUTER_BORDER_SIZE + GUIConstants.SIZE_PADDING + INNER_BORDER_SIZE;
		y = getHeight() - OUTER_BORDER_SIZE - GUIConstants.SIZE_PADDING - INNER_BORDER_SIZE;
		width = getWidth() - (OUTER_BORDER_SIZE * 2) - (GUIConstants.SIZE_PADDING * 2) - (INNER_BORDER_SIZE * 2);
		height = INNER_BORDER_SIZE;
		graphics.fillRect(x, y, width, height);
		
		// Right.
		x = getWidth() - OUTER_BORDER_SIZE - GUIConstants.SIZE_PADDING - INNER_BORDER_SIZE;
		y = OUTER_BORDER_SIZE + toolbarPanel.getHeight() + INNER_BORDER_SIZE;
		width = INNER_BORDER_SIZE;
		height = getHeight() - (OUTER_BORDER_SIZE * 2) - GUIConstants.SIZE_PADDING - toolbarPanel.getHeight() - 
				(INNER_BORDER_SIZE * 2);
		graphics.fillRect(x, y, width, height);
	}
	
	/**
	 * Draws a "shine" effect over top of the inner border. This mimicks the shine effect of the toolbar panel.
	 * @param graphics
	 */
	private void drawShine(Graphics graphics) {
		graphics.setColor(GUIConstants.COLOR_SHINE);
		
		// Left.
		graphics.fillRect(OUTER_BORDER_SIZE, OUTER_BORDER_SIZE + toolbarPanel.getHeight(), GUIConstants.SIZE_SHINE, 
				getHeight() - (OUTER_BORDER_SIZE * 2) - toolbarPanel.getHeight());
	}
}
