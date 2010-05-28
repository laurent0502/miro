package miro.client.view;

import com.google.gwt.user.client.ui.Widget;

/**
 * This interface is implemented by all listeners
 */
public interface EventListener {
	/**
	 * Method which receive the response
	 * 
	 * @param widget
	 *            Widget sending the event
	 */
	public void notifyChange(Widget widget);
}
