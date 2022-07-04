package view.util;

import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicScrollBarUI;

/**
 * Represents a retro-style UI that can be applied to <code>JScrollBar</code> objects.
 * 
 * @author Adam
 */
public class CustomScrollbarUI extends BasicScrollBarUI {
	/**
	 * Constructs a new <code>CustomScrollbarUI</code> object.
	 */
	public CustomScrollbarUI() {
		super();
	}
	
	/**
	 * Informs a custom scrollbar what its increment and decrement buttons are.
	 * <p>This only works if the scrollbar has set its UI to this <code>CustomScrollbarUI</code> object and the 
	 * increment and decrement buttons have been "invoked" in some way or another.
	 * @param scrollbar the <code>CustomScrollbar</code> object to inform.
	 */
	public void setScrollbarButtons(CustomScrollbar scrollbar) {
		scrollbar.setButtons(incrButton, decrButton);
	}
	
	@Override
	public JButton createIncreaseButton(int orientation) {
		return new CustomButton(ViewResources.get().getImage("button_increase.png"));
	}
	
	@Override
	public JButton createDecreaseButton(int orientation) {
		return new CustomButton(ViewResources.get().getImage("button_decrease.png"));
	}
	
	@Override
	public void paintTrack(Graphics graphics, JComponent component, Rectangle trackBounds) {
		// Fill.
		graphics.setColor(GUIConstants.COLOR_SECONDARY);
		graphics.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
    }
	
	@Override
	public void paintThumb(Graphics graphics, JComponent component, Rectangle thumbBounds) {
		// Fill.
		graphics.setColor(GUIConstants.COLOR_SECONDARY);
		graphics.fillRect(thumbBounds.x + GUIConstants.SIZE_THUMB_EDGE, thumbBounds.y + GUIConstants.SIZE_THUMB_EDGE, 
				thumbBounds.width - (GUIConstants.SIZE_THUMB_EDGE * 2), thumbBounds.height - 
				(GUIConstants.SIZE_THUMB_EDGE * 2));
		
		// Top, left, bottom, right edge.
		graphics.setColor(GUIConstants.COLOR_OUTLINE);
		graphics.fillRect(thumbBounds.x + GUIConstants.SIZE_THUMB_EDGE, thumbBounds.y, thumbBounds.width - 
				(GUIConstants.SIZE_THUMB_EDGE * 2), GUIConstants.SIZE_THUMB_EDGE);
		graphics.fillRect(thumbBounds.x, thumbBounds.y + GUIConstants.SIZE_THUMB_EDGE, GUIConstants.SIZE_THUMB_EDGE, 
				thumbBounds.height - (GUIConstants.SIZE_THUMB_EDGE * 2));
		graphics.fillRect(thumbBounds.x + GUIConstants.SIZE_THUMB_EDGE, thumbBounds.y + (int)thumbBounds.getHeight() - 
				GUIConstants.SIZE_THUMB_EDGE, thumbBounds.width - (GUIConstants.SIZE_THUMB_EDGE * 2), 
				GUIConstants.SIZE_THUMB_EDGE);
		graphics.fillRect(thumbBounds.x + thumbBounds.width - GUIConstants.SIZE_THUMB_EDGE, thumbBounds.y + 
				GUIConstants.SIZE_THUMB_EDGE, GUIConstants.SIZE_THUMB_EDGE, thumbBounds.height - 
				(GUIConstants.SIZE_THUMB_EDGE * 2));
		
		// Shine.
        graphics.setColor(GUIConstants.COLOR_SHINE);
        for (int i = 0; i < GUIConstants.SIZE_SHINE; i++) {
        	int x = thumbRect.x + GUIConstants.SIZE_THUMB_EDGE + i;
        	int y = thumbRect.y + GUIConstants.SIZE_THUMB_EDGE + i;
        	int width = thumbRect.width - (GUIConstants.SIZE_THUMB_EDGE * 2) - 1 - (i * 2);
        	int height = thumbRect.height - (GUIConstants.SIZE_THUMB_EDGE * 2) - 1 - (i * 2);
        	graphics.drawRect(x, y, width, height);
        }
	}
}
