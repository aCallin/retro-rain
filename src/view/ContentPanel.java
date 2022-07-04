package view;

import java.awt.Dimension;
import java.awt.event.AdjustmentEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;

import javax.swing.DefaultBoundedRangeModel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.SpringLayout;

import view.util.CustomScrollbar;
import view.util.CustomScrollbarUI;
import view.util.GUIConstants;

/**
 * Represents a visual container for <code>UserSelectionPanel</code> objects.
 * 
 * @author Adam
 */
@SuppressWarnings("serial")
public class ContentPanel extends JPanel {
	private SpringLayout layout;
	
	private ArrayList<UserSelectionPanel> userSelections;
	private CustomScrollbar scrollbar;
	private int scrollOffset;
	private int selectionsHeight;
	private int viewableHeight;
	
	/**
	 * Creates a new <code>ContentPanel</code> instance.
	 */
	public ContentPanel() {
		layout = new SpringLayout();
		setLayout(layout);
		setFocusable(false);
		setBackground(GUIConstants.COLOR_SECONDARY);
		
		userSelections = new ArrayList<UserSelectionPanel>();
		setupEventHandling();
	}
	
	/**
	 * Adds a new user selection to this panel.
	 * 
	 * <p>If enough user selections are added to the panel in that not all of the user selections can be fully seen, 
	 * then a scrollbar is added.
	 * @param userSelection the <code>UserSelectionPanel</code> object to add to this panel.
	 */
	public void addUserSelection(UserSelectionPanel userSelection) {
		userSelections.add(userSelection);
		add(userSelection);
		firePropertyChange("userSelectionAdded", null, userSelection);
		onUserSelectionAdded();
	}
	
	/**
	 * Sets up some of the events that this panel will need to recieve or notify about.
	 */
	private void setupEventHandling() {
		// The event that this panel has been resized.
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				onPanelResized();
			}
		});
		
		// The event that the user has scrolled the mouse wheel over this panel.
		addMouseWheelListener((MouseWheelEvent e) -> onMouseWheelRotated(e.getWheelRotation()));
	}
	
	/**
	 * The actions to be performed whenever a user selection is added.
	 */
	private void onUserSelectionAdded() {
		onPanelResized(); // As of now there is no distinction between the two so we'll reuse this.
	}
	
	/**
	 * The actions to be performed whenever this panel is resized.
	 */
	private void onPanelResized() {
		selectionsHeight = userSelections.size() * UserSelectionPanel.PANEL_HEIGHT;
		viewableHeight = getHeight();
		
		// Remove the current scrollbar from the panel if it exists and isn't needed anymore.
		if (!isScrollbarNeeded() && scrollbar != null)
			removeScrollbar();
		else if (isScrollbarNeeded()) {
			// Create the scrollbar if it doesn't exist and is now needed.
			if (scrollbar == null)
				createScrollbar();
			
			// Place the scrollbar and "poke" it to get rid of visual glitches.
			placeScrollbar();
			pokeScrollbar();
		}
		placeUserSelections();
		revalidate();
	}
	
	/**
	 * The actions to be performed whenever the mouse wheel is rotated.
	 * @param rotation the amount of "clicks" the mouse wheel has rotated.
	 */
	private void onMouseWheelRotated(int rotation) {
		if (scrollbar != null) {
			final int ROTATION_MULTIPLIER = 15;
			int offset = rotation * ROTATION_MULTIPLIER;
			int newValue = scrollbar.getValue() + offset;
			if (newValue < 0)
				newValue = 0;
			if (newValue > selectionsHeight - viewableHeight)
				newValue = selectionsHeight - viewableHeight;
			scrollbar.setValue(newValue); // Doing this will trigger onScrollbarUsed().
		}
	}
	
	/**
	 * Places the user selections in their correct spot, accounting for the scroll offset.
	 * 
	 * <p>Note that this also accounts for whether or not the scrollbar exists. Any scrollbar creation or deletion 
	 * should occur before this method is invoked.
	 */
	private void placeUserSelections() {
		int yOffset = 0;
		if (scrollbar != null)
			yOffset = -scrollOffset;
		
		for (UserSelectionPanel userSelection : userSelections) {
			int widthOffset = 0;
			if (scrollbar != null)
				widthOffset = -scrollbar.getPreferredSize().width;
			
			layout.putConstraint(SpringLayout.NORTH, userSelection, yOffset, SpringLayout.NORTH, this);
			layout.putConstraint(SpringLayout.WEST, userSelection, 0, SpringLayout.WEST, this);
			Dimension size = new Dimension(getWidth() + widthOffset, UserSelectionPanel.PANEL_HEIGHT);
			userSelection.setSize(size);
			userSelection.setPreferredSize(size);			
			
			yOffset += UserSelectionPanel.PANEL_HEIGHT;
		}
	}
	
	/**
	 * Determines if a scrollbar is needed to be able to view all of the user selections within the panel.
	 * @return true if a scrollbar is needed, false otherwise.
	 */
	private boolean isScrollbarNeeded() {
		return selectionsHeight > viewableHeight;
	}
	
	/**
	 * Creates the scrollbar and adds it to the panel.
	 */
	private void createScrollbar() {
		scrollbar = new CustomScrollbar(JScrollBar.VERTICAL);
		CustomScrollbarUI customUI = new CustomScrollbarUI();
		customUI.installUI(scrollbar); // A workaround to construct the scrollbar buttons in time (otherwise null).
		customUI.setScrollbarButtons(scrollbar);
		scrollbar.setUI(new CustomScrollbarUI());
		scrollbar.addAdjustmentListener((AdjustmentEvent e) -> onScrollbarUsed());
		add(scrollbar);
		firePropertyChange("scrollbarCreated", null, scrollbar);
	}
	
	/**
	 * Places the scrollbar in its correct spot. This will account for the scroll offset and reset the bounded range of 
	 * the scrollbar. 
	 */
	private void placeScrollbar() {
		// Prevent scrolling past the selections height.
		if (viewableHeight + scrollOffset > selectionsHeight)
			scrollOffset = (selectionsHeight - viewableHeight);
		scrollbar.setModel(new DefaultBoundedRangeModel(scrollOffset, viewableHeight, 0, selectionsHeight));
		
		Dimension size = new Dimension(scrollbar.getPreferredSize().width, getHeight());
		scrollbar.setSize(size);
		scrollbar.setPreferredSize(size);
		layout.putConstraint(SpringLayout.NORTH, scrollbar, 0, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.EAST, scrollbar, 0, SpringLayout.EAST, this);
	}
	
	/**
	 * Moves then unmoves the scrollbar to get rid of a graphical glitch that happens when the scrollbar is placed.
	 */
	private void pokeScrollbar() {
		scrollbar.setValue(scrollbar.getValue() + 1);
		scrollbar.setValue(scrollbar.getValue() - 1);
	}
	
	/**
	 * Removes the scrollbar from the panel and sets the scrollbar to <code>null</code>.
	 */
	private void removeScrollbar() {
		remove(scrollbar);
		scrollbar = null;
	}
	
	/**
	 * The actions to be performed whenever the scrollbar is used.
	 */
	private void onScrollbarUsed() {
		scrollOffset = scrollbar.getValue();
		scrollbar.revalidate();
		placeUserSelections();
		revalidate();
	}
}
