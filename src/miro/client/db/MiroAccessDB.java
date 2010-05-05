package miro.client.db;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import miro.shared.*;
import java.util.List;

public class MiroAccessDB {

	static final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	public static void getAssignments(AsyncCallback<List<Assignment>> callback) {
		greetingService.getAssignments(callback);
	}

	public static void updateAssignments(List<Person> personList,
			List<Assignment> assignmentList, AsyncCallback<String> callback) {
		greetingService.updateAssignments(personList, assignmentList, callback);
	}

	public static void setLocked(boolean isLocked, AsyncCallback callback) {
			greetingService.setLocked(isLocked, callback);
	}
	
	public static void getMonthOfDate(AsyncCallback<String> callback){
		greetingService.getMonthOfDate(callback);
	}
}