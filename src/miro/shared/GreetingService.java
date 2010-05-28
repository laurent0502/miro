package miro.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The GreetingService interface provides the synchronious methods used for the
 * RPC
 */
@RemoteServiceRelativePath("greetingService")
public interface GreetingService extends RemoteService {

	/**
	 * Invoked to get the assignment list
	 */
	List<Assignment> getAssignments();

	/**
	 * Invoked to update the assignment list
	 */
	void updateAssignments(List<Person> personList,
			List<Assignment> assignmentList);

	/**
	 * Invoked to set the lock of the database
	 */
	void setLocked(boolean locked) throws IllegalArgumentException;

	/**
	 * Invoked to get the current month
	 */
	String getMonthOfDate();

	/**
	 * Invoked to get the connection list
	 */
	List<Connection> getConnections();
}
