package miro.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("greetingService")
public interface GreetingService extends RemoteService {

	List<Assignment> getAssignments();

	void updateAssignments(List<Person> personList,
			List<Assignment> assignmentList);

	void setLocked(boolean locked) throws IllegalArgumentException;

	String getMonthOfDate();

	List<Connection> getConnections();
}
