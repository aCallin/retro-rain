package view.util;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import view.ContentPanel;
import view.ControlPanel;
import view.MainUI;
import view.OverlayPanel;
import view.ToolbarPanel;
import view.UserSelectionPanel;

/**
 * Works alongside the main UI to dictate which cursor to display at any given moment. The cursor will change depending 
 * on certain factors such as if a slider is being hovered over or if a button is pressed.
 * 
 * @author Adam
 */
public class CursorManager {
	private MainUI mainUI;
	private OverlayPanel overlayPanel;
	private ToolbarPanel toolbarPanel;
	private ContentPanel contentPanel;
	private ControlPanel controlPanel;
	
	private Cursor selectCursor;
	private Cursor selectingCursor;
	private Cursor grabCursor;
	private Cursor grabbingCursor;
	private Cursor bidirectionalCursor;
	private Cursor horizontalCursor;
	private Cursor verticalCursor;
	private Cursor nextCursor;
	
	private MouseAdapter selectCursorBehaviour;
	private MouseAdapter grabCursorBehaviour;
	private MouseAdapter extendCursorBehaviour;
	private boolean canChangeCursors;
	
	/**
	 * Creates a <code>CursorManager</code>.
	 * @param mainUI the main UI. The <code>CursorManager</code> needs to work alongside this to function properly.
	 */
	public CursorManager(MainUI mainUI) {
		this.mainUI = mainUI;
		overlayPanel = mainUI.getOverlayPanel();
		toolbarPanel = overlayPanel.getToolbarPanel();
		contentPanel = overlayPanel.getContentPanel();
		controlPanel = overlayPanel.getControlPanel();
	}
	
	/**
	 * Starts setting the window's cursor in response to mouse events.
	 */
	public void start() {
		createCursors();
		createCursorBehaviour();
		subscribeToMouseEvents();
	}
	
	/**
	 * Creates the various custom cursors that will be used in the UI.
	 */
	private void createCursors() {
		ViewResources resources = ViewResources.get();
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		
		selectCursor = toolkit.createCustomCursor(resources.getImage("cursor_select.png"), new Point(8, 0), 
				"select cursor");
		selectingCursor = toolkit.createCustomCursor(resources.getImage("cursor_selecting.png"), new Point(8, 2), 
				"selecting cursor");
		grabCursor = toolkit.createCustomCursor(resources.getImage("cursor_grab.png"), new Point(6, 1), "grab cursor");
		grabbingCursor = toolkit.createCustomCursor(resources.getImage("cursor_grabbing.png"), new Point(6, 1), 
				"grabbing cursor");
		bidirectionalCursor = toolkit.createCustomCursor(resources.getImage("cursor_bidirectional.png"), 
				new Point(13, 12), "bidirectional cursor");
		horizontalCursor = toolkit.createCustomCursor(resources.getImage("cursor_horizontal.png"), new Point(13, 4), 
				"horizontal cursor");
		verticalCursor = toolkit.createCustomCursor(resources.getImage("cursor_vertical.png"), new Point(5, 11), 
				"vertical cursor");
		
		mainUI.setCursor(selectCursor);
		nextCursor = selectCursor;
		canChangeCursors = true;
	}
	
	/**
	 * Creates the behaviour for all cursors.
	 */
	private void createCursorBehaviour() {
		// For things like buttons or hovering over nothing of interest.
		selectCursorBehaviour = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					mainUI.setCursor(selectingCursor);
					canChangeCursors = false;
				}
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					mainUI.setCursor(nextCursor);
					canChangeCursors = true;
				}
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				if (canChangeCursors)
					mainUI.setCursor(selectCursor);
				nextCursor = selectCursor;
			}
		};
		
		// For things like the toolbar, scrollbar, and sliders.
		grabCursorBehaviour = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					mainUI.setCursor(grabbingCursor);
					canChangeCursors = false;
				}
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					mainUI.setCursor(nextCursor);
					canChangeCursors = true;
				}
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				if (canChangeCursors)
					mainUI.setCursor(grabCursor);					
				nextCursor = grabCursor;
			}
		};
		
		// For extending the window.
		extendCursorBehaviour = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1)
					canChangeCursors = false;
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					mainUI.setCursor(nextCursor);
					canChangeCursors = true;
				}
			}
			
			@Override
			public void mouseMoved(MouseEvent e) {
				if (overlayPanel.getExtendedType() == OverlayPanel.EXTENDED_BIDIRECTIONAL)
					mainUI.setCursor(bidirectionalCursor);
				else if (overlayPanel.getExtendedType() == OverlayPanel.EXTENDED_HORIZONTAL)
					mainUI.setCursor(horizontalCursor);
				else if (overlayPanel.getExtendedType() == OverlayPanel.EXTENDED_VERTICAL)
					mainUI.setCursor(verticalCursor);
			}
		};
	}
	
	/**
	 * Listens to mouse events to dictate which cursor to display.
	 */
	private void subscribeToMouseEvents() {
		// Overlay panel.
		overlayPanel.addMouseListener(extendCursorBehaviour);
		overlayPanel.addMouseMotionListener(extendCursorBehaviour);

		// Toolbar panel.
		toolbarPanel.addMouseListener(grabCursorBehaviour);
		toolbarPanel.getMinimizeButton().addMouseListener(selectCursorBehaviour);
		toolbarPanel.getMaximizeButton().addMouseListener(selectCursorBehaviour);
		toolbarPanel.getExitButton().addMouseListener(selectCursorBehaviour);
		
		// Content panel.
		contentPanel.addMouseListener(selectCursorBehaviour);
		contentPanel.addPropertyChangeListener("userSelectionAdded", (propertyChange) -> {
			UserSelectionPanel userSelection = (UserSelectionPanel)propertyChange.getNewValue();
			userSelection.getControlButton().addMouseListener(selectCursorBehaviour);
			userSelection.getVolumeSlider().addMouseListener(grabCursorBehaviour);
		});
		contentPanel.addPropertyChangeListener("scrollbarCreated", (propertyChange) -> {
			CustomScrollbar scrollbar = (CustomScrollbar)propertyChange.getNewValue();
			scrollbar.addMouseListener(grabCursorBehaviour);
			scrollbar.getIncrementButton().addMouseListener(selectCursorBehaviour);
			scrollbar.getDecrementButton().addMouseListener(selectCursorBehaviour);
		});
		
		// Control panel.
		controlPanel.addMouseListener(selectCursorBehaviour);
		controlPanel.getControlButton().addMouseListener(selectCursorBehaviour);
		controlPanel.getVolumeSlider().addMouseListener(grabCursorBehaviour);
	}
}
