package miro.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {

	void getAssignments(AsyncCallback<List<Assignment>> callback);

	void updateAssignments(List<Person> personList,
			List<Assignment> assignmentList, AsyncCallback callback);

	void setLocked(boolean locked, AsyncCallback callback)
			throws IllegalArgumentException;

	void getMonthOfDate(AsyncCallback<String> callback);

	void getConnections(AsyncCallback/*<List<Connection>>*/ callback);
}
