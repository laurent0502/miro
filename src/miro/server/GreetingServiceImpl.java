package miro.server;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import miro.shared.Assignment;
import miro.shared.Connection;
import miro.shared.GreetingService;
import miro.shared.Lock;
import miro.shared.Person;

import org.apache.log4j.Logger;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;

@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
		GreetingService {
	Logger logger = Logger.getLogger(GreetingServiceImpl.class);
	

	static {
		ObjectifyService.register(Lock.class);
		ObjectifyService.register(Connection.class);
		ObjectifyService.register(Assignment.class);

		Objectify ofy = ObjectifyService.begin();
		
		if (ofy.query(Connection.class).countAll() == 0) {

			Objectify obj = ObjectifyService.beginTransaction();
			obj.put(new Connection("DE PESSEMIER", "Johann", "projet"));
			obj.getTxn().commit();
			obj = ObjectifyService.beginTransaction();
			obj.put(new Connection("AUCQUIERE", "Eric", "projet"));

			obj.getTxn().commit();
		}
		
		if(ofy.query(Lock.class).countAll() == 0){
			
			Objectify obj = ObjectifyService.beginTransaction();
			obj.put(new Lock(1l));
			obj.getTxn().commit();
		}
	}

	String error = "";
	Calendar calendar = new GregorianCalendar();

	public void setLocked(boolean locked) throws IllegalArgumentException {
		// ObjectifyService.register(Lock.class);
		Objectify ofy = ObjectifyService.begin();
		Lock lock = (Lock) ofy.find(new Key(Lock.class, 1));

		if (lock.isLocked()) {
			if (locked)
				throw new IllegalArgumentException(
						"Table utilisée par un autre utilisateur.\nRéessayez plus tard!");
			else
				lock.setLocked(false);
		} else {
			lock.setLocked(true);
		}
		ofy.put(lock);
	}

	public List<Assignment> getAssignments() {
		List<Assignment> assignmentList = new ArrayList<Assignment>();

		Objectify ofy = ObjectifyService.begin();

		Query<Assignment> query = ofy.query(Assignment.class);

		for (Assignment assignmentFromList : query) {
			assignmentList.add(assignmentFromList);
		}

		return assignmentList;
	}

	// private void test() {
	// }

	public void updateAssignments(List<Person> personList,
			List<Assignment> assignmentList) {
		// Récupération des assignments actuel de la BD
		List<Assignment> assignmentList2 = getAssignments();

		for (Assignment assignment : assignmentList) {

			// Si l'assignment existait déja dans la bd,il faut juste
			// modifier les prestations,sinon,il faut le rajouter à
			// la liste actuelle d'assignment
			if (assignmentList2.contains(assignment)) {
				int indexOf = assignmentList2.indexOf(assignment);
				Assignment assignment2 = assignmentList2.get(indexOf);

				for (int i = 0; i < 12; i++) {
					assignment2.setPrestation(i, assignment.getPrestation(i));
				}
			} else {
				assignmentList2.add(assignment);
			}
		}
		// Mise à jour des informations propres à chaque personne
		updateInfosPersonInAssignmentList(personList, assignmentList2);

		putData(assignmentList2);

		setLocked(false);
	}

	private void updateInfosPersonInAssignmentList(List<Person> personList,
			List<Assignment> assignmentList2) {

		for (int i = 0; i < personList.size(); i++) {

			Person person = personList.get(i);

			for (int j = 0; j < assignmentList2.size(); j++) {
				if (assignmentList2.get(j).getPerson().equals(person)) {
					assignmentList2.get(j).setPerson(person);
				}
			}
		}
	}

	void putData(List<Assignment> assignmentList) {
		

		for (Assignment assignment : assignmentList) {
			Objectify ofy = ObjectifyService.beginTransaction();
			ofy.put(assignment);
			ofy.getTxn().commit();
		}

	}

	public List<Connection> getConnections() {
		
		List<Connection> connectionList = new ArrayList<Connection>();

		Objectify ofy = ObjectifyService.begin();

		Query<Connection> query = ofy.query(Connection.class);

		for (Connection connection : query) {
			connectionList.add(connection);
		}

		return connectionList;
	}

	public String getMonthOfDate() {
		return "" + (calendar.get(GregorianCalendar.MONTH) + 1);
	}

	public String getYearOfDate() {
		return "" + calendar.get(GregorianCalendar.YEAR);
	}
}