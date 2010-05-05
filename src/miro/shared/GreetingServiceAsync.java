package miro.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
    void getAssignments(AsyncCallback<List<Assignment>> callback);
    void updateAssignments(List<Person> personList,List<Assignment> assignmentList,AsyncCallback<String> callback);
    void setLocked(boolean locked,AsyncCallback callback)throws IllegalArgumentException,NullPointerException;
    void getMonthOfDate(AsyncCallback<String> callback);
}
