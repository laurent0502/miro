package miro.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import miro.shared.Assignment;
import miro.shared.Person;
import miro.shared.Project;
import miro.shared.Time;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
//
public class SampleUploadServlet extends HttpServlet {
	private Time time;
	private List<Person> personList = new ArrayList<Person>();
	private List<Assignment> assignmentList;
	private GreetingServiceImpl greetingService = new GreetingServiceImpl();
	private Logger logger = Logger.getLogger(this.getClass());

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException{
		assignmentList = greetingService.getAssignments();
		/*
		 * récupération dans assignmentList des personnes afin de les mettre
		 * dans personList
		 */
		setPersonList();

		try {
			ServletFileUpload upload = new ServletFileUpload();
			FileItemIterator it = upload.getItemIterator(request);

			while (it.hasNext()) {
				FileItemStream item = it.next();

				parseFile(item);
				logger.error("parseFile end");
				modifyDB();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		} 
		catch (FileUploadException e) {
			e.printStackTrace();
		}
	}

	private void decodeFirstLineOfFile(String line) {
		List<String> itemList = getItemsOfLine(line);
		String lastAndFirstName = itemList.get(0);
		String year = itemList.get(1);
		String month = itemList.get(2);
		String missionType = itemList.get(3);
		String missionName = itemList.get(4);
		String activityName = itemList.get(5);
		String prestationForMonth = itemList.get(6);

		addPerson(lastAndFirstName);
		setTime(year, month);
		logger.error("decodefirstlineoffile");
		manageLastItems(getPerson(lastAndFirstName), missionType, missionName,
				activityName, prestationForMonth);
	}

	private Person getPerson(String lastAndFirstName) {
		int indexSpacing = lastAndFirstName.indexOf(" ");
		String lastName = lastAndFirstName.substring(0, indexSpacing);
		String firstName = lastAndFirstName.substring(indexSpacing + 1);

		int indexOfPerson = personList.indexOf(new Person(lastName, firstName));

		if (indexOfPerson < 0)
			return null;

		return personList.get(indexOfPerson);
	}

	private boolean addPerson(String lastAndFirstName) {
		int indexSpacing = lastAndFirstName.indexOf(" ");
		String lastName = lastAndFirstName.substring(0, indexSpacing);
		String firstName = lastAndFirstName.substring(indexSpacing + 1);
		Person person = new Person(lastName, firstName);
		boolean isAdded = !personList.contains(person);

		if (isAdded)
			personList.add(person);

		return isAdded;
	}

	private void decodeLine(String line) {
		List<String> itemList = getItemsOfLine(line);
		String lastAndFirstName = itemList.get(0);
		String missionType = itemList.get(3);
		String missionName = itemList.get(4);
		String activityName = itemList.get(5);
		String prestationForMonth = itemList.get(6);

		addPerson(lastAndFirstName);
		manageLastItems(getPerson(lastAndFirstName), missionType, missionName,
				activityName, prestationForMonth);
	}

	private void parseFile(FileItemStream item) throws ServletException {
		
		try {
			InputStream stream = item.openStream();
			InputStreamReader inputStreamReader = new InputStreamReader(stream);
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);

			bufferedReader.readLine();
			String line = bufferedReader.readLine();

			decodeFirstLineOfFile(line);

			if (!isPeriodOfFileCorrect()) {
				logger.error("periode invalide");
				throw new ServletException("Periode du fichier invalide !!!");
			}
			while ((line = bufferedReader.readLine()).charAt(0) != ';') {
				decodeLine(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean isPeriodOfFileCorrect() {
		String month = greetingService.getMonthOfDate();
		String year = greetingService.getYearOfDate();

		return time.getMonth() < Integer.parseInt(month)
				&& time.getYear() == Integer.parseInt(year);
	}

	private List<String> getItemsOfLine(String line) {
		List<String> itemList = new ArrayList<String>();

		for (int i = 0; i < 6; i++) {
			int pointIndex = line.indexOf(";");
			String item = line.substring(0, pointIndex);

			itemList.add(item);

			line = line.substring(pointIndex + 1);
		}
		itemList.add(line);

		return itemList;
	}

	private void setTime(String item, String item2) {
		int year = Integer.parseInt(item);
		int month = Integer.parseInt(item2);

		time = new Time(month, year);
	}

	private void manageLastItems(Person person, String item1, String item2,
			String item3, String item4) {
		item3 = item3.toLowerCase();
		item1 = item1.replace(item1.charAt(1), 'e');

		double valueOfPrestation = Double.valueOf(item4);

		if (item1.equals("Generique")) {
			logger.error("passe bien ici");
			if (item3.equals("absence")) {
				person.getHoliday(time).setNumber(valueOfPrestation);
			}
			if (item3.equals("formations")) {
				person.getTraining(time).setNumber(valueOfPrestation);
			}
			if (item3.equals("others")) {
				person.getOther(time).setNumber(valueOfPrestation);
			}
		} else {
			Project project = new Project(item2);
			Assignment assignment = new Assignment(project, person);
			assignment.getPrestation(time.getMonth() - 1).setNumber(
					valueOfPrestation);

			if (assignmentList.contains(assignment)) {
				int indexOfAssignment = assignmentList.indexOf(assignment);

				assignmentList.get(indexOfAssignment).getPrestation(
						time.getMonth() - 1).setNumber(valueOfPrestation);
			} else {
				assignmentList.add(assignment);
			}
		}
	}

	private void modifyDB() {
		GreetingServiceImpl greetingService = new GreetingServiceImpl();

		greetingService.putData(assignmentList);
	}

	private void addAssignmentsForPersonWithoutProject() {

		for (Person person : personList) {
			boolean hasAssignmentForPerson = false;

			for (Assignment assignment : assignmentList) {
				if (assignment.getPerson().equals(person)) {
					hasAssignmentForPerson = true;
				}
			}
			if (!hasAssignmentForPerson) {
				assignmentList.add(new Assignment(null, person));
			}
		}
	}

	private void setPersonList() {
		for (Assignment assignment : assignmentList) {
			Person person = assignment.getPerson();

			if (!personList.contains(person)) {
				personList.add(person);
			}
		}
	}
}