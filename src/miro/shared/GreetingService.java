package miro.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.List;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greetingService")
public interface GreetingService extends RemoteService {

    List<Assignment> getAssignments();
    String updateAssignments(List<Person> personList,List<Assignment> assignmentList);
    void setLocked(boolean locked)throws IllegalArgumentException,NullPointerException;
    String getMonthOfDate();
}
