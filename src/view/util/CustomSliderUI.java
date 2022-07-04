package view.util;

import java.awt.Graphics;

import javax.swing.JSlider;
import javax.swing.plaf.basic.BasicSliderUI;

/**
 * Represents a retro-styled UI that can be applied to <code>JSlider</code> objects. 
 * 
 * @author Adam
 */
public class CustomSliderUI extends BasicSliderUI {
	private static final int TRACK_HEIGHT = 3;
	
	/**
	 * Creates a new <code>CustomSliderUI</code> object that can be applied to a slider.
	 * @param slider the <code>JSlider</code> object that the UI will be applied to.
	 */
	public CustomSliderUI(JSlider slider) {
		super(slider);
	}
	
	@Override
	public void paintTrack(Graphics graphics) {
		graphics.setColor(GUIConstants.COLOR_OUTLINE);
		graphics.fillRect(0, (slider.getHeight() / 2) - (TRACK_HEIGHT / 2), slider.getWidth(), TRACK_HEIGHT);
	}
	
	@Override
    public void paintThumb(final Graphics graphics) {
        // Top, left, bottom, right edge.
		graphics.setColor(GUIConstants.COLOR_OUTLINE);
		graphics.fillRect(thumbRect.x + GUIConstants.SIZE_THUMB_EDGE, thumbRect.y, thumbRect.width - 
				(GUIConstants.SIZE_THUMB_EDGE * 2), GUIConstants.SIZE_THUMB_EDGE);
        graphics.fillRect(thumbRect.x, thumbRect.y + GUIConstants.SIZE_THUMB_EDGE, GUIConstants.SIZE_THUMB_EDGE, 
        		thumbRect.height - (GUIConstants.SIZE_THUMB_EDGE * 2));
        graphics.fillRect(thumbRect.x + GUIConstants.SIZE_THUMB_EDGE, thumbRect.y + thumbRect.height -
        		GUIConstants.SIZE_THUMB_EDGE, thumbRect.width - (GUIConstants.SIZE_THUMB_EDGE * 2), 
        		GUIConstants.SIZE_THUMB_EDGE);
        graphics.fillRect(thumbRect.x + thumbRect.width - GUIConstants.SIZE_THUMB_EDGE, thumbRect.y + 
        		GUIConstants.SIZE_THUMB_EDGE, GUIConstants.SIZE_THUMB_EDGE, thumbRect.height - 
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
        
        // Fill.
        int x = thumbRect.x + GUIConstants.SIZE_THUMB_EDGE + GUIConstants.SIZE_SHINE;
        int y = thumbRect.y + GUIConstants.SIZE_THUMB_EDGE + GUIConstants.SIZE_SHINE;
        int width = thumbRect.width - (GUIConstants.SIZE_THUMB_EDGE * 2) - (GUIConstants.SIZE_SHINE * 2);
        int height = thumbRect.height - (GUIConstants.SIZE_THUMB_EDGE * 2) - (GUIConstants.SIZE_SHINE * 2);
        graphics.setColor(GUIConstants.COLOR_SECONDARY);
        graphics.fillRect(x, y, width, height);
    }
	
	@Override
	public void paintFocus(Graphics graphics) {}
}
