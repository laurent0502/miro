package miro.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The GreetingServiceAsync interface provides the asynchronious methods used
 * for the RPC
 */
public interface GreetingServiceAsync {

	/**
	 * Invoked to get the assignment list
	 */
	void getAssignments(AsyncCallback<List<Assignment>> callback);

	/**
	 * Invoked to update the assignment list
	 */
	void updateAssignments(List<Person> personList,
			List<Assignment> assignmentList, AsyncCallback callback);

	/**
	 * Invoked to set the lock of the database
	 */
	void setLocked(boolean locked, AsyncCallback callback)
			throws IllegalArgumentException;

	/**
	 * Invoked to get the current month
	 */
	void getMonthOfDate(AsyncCallback<String> callback);

	/**
	 * Invoked to get the connection list
	 */
	void getConnections(AsyncCallback callback);
}
