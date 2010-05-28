package miro.client.db;

import java.util.List;

import miro.shared.Assignment;
import miro.shared.Connection;
import miro.shared.GreetingService;
import miro.shared.GreetingServiceAsync;
import miro.shared.Person;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The class is a intermediate class between the client and the server
 **/
public class MiroAccessDB {

	static final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	/**
	 * Call to get the assignment list
	 * 
	 * @param GET_ASSIGNMENTS_CALLBACK
	 *            Getting the response of the server
	 **/
	public static void getAssignments(
			AsyncCallback<List<Assignment>> GET_ASSIGNMENTS_CALLBACK) {
		greetingService.getAssignments(GET_ASSIGNMENTS_CALLBACK);
	}

	/**
	 * Call to update the assignment list
	 * 
	 * @param personList
	 *            The person list
	 * @param assignmentList
	 *            The assignment list
	 * @param UPDATE_ASSIGNMENTS_CALLBACK
	 *            Getting the response of the server
	 **/
	public static void updateAssignments(List<Person> personList,
			List<Assignment> assignmentList,
			AsyncCallback<String> UPDATE_ASSIGNMENTS_CALLBACK) {
		greetingService.updateAssignments(personList, assignmentList,
				UPDATE_ASSIGNMENTS_CALLBACK);
	}

	/**
	 * Call to update the lock of the database
	 * 
	 * @param isLocked
	 *            The boolean state of the lock
	 * @param SET_LOCKED_CALLBACK
	 *            Getting the response of the server
	 **/
	public static void setLocked(boolean isLocked,
			AsyncCallback SET_LOCKED_CALLBACK) {
		greetingService.setLocked(isLocked, SET_LOCKED_CALLBACK);
	}

	/**
	 * Call to get the current month
	 * 
	 * @param MONTH_CALLBACK
	 *            Getting the response of the server
	 **/
	public static void getMonthOfDate(AsyncCallback<String> MONTH_CALLBACK) {
		greetingService.getMonthOfDate(MONTH_CALLBACK);
	}

	/**
	 * Call to get the connection list
	 * 
	 * @param CONNECTION_CALLBACK
	 *            Getting the response of the server
	 **/
	public static void getConnections(
			AsyncCallback<List<Connection>> CONNECTION_CALLBACK) {
		greetingService.getConnections(CONNECTION_CALLBACK);
	}
}