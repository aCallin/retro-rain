package view.util;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

/**
 * Contains static methods for invoking code on the AWT event dispatching thread for convenience.
 * 
 * @author Adam
 */
public class EDTDispatcher {
	/**
	 * Wraps {@link SwingUtilities#invokeLater(Runnable)}.
	 * @param runnable what to perform asynchronously on the AWT event dispatching thread.
	 */
	public static void doLater(Runnable runnable) {
		SwingUtilities.invokeLater(runnable);
	}
	
	/**
	 * Wraps {@link SwingUtilities#invokeAndWait(Runnable)}.
	 * @param runnable what to perform synchronously on the AWT event dispatching thread.
	 */
	public static void doAndWait(Runnable runnable) {
		// This was the main reason for wrapping these methods in a class.
		try {
			SwingUtilities.invokeAndWait(runnable);
		} catch (InvocationTargetException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
