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
import miro.shared.OfficialInformation;
import miro.shared.Person;
import miro.shared.Project;
import miro.shared.Record;
import miro.shared.Time;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

public class SampleUploadServlet extends HttpServlet {
	private Time time;
	private List<Person> personList = new ArrayList<Person>();
	private List<Assignment> assignmentList;
	private GreetingServiceImpl greetingService = new GreetingServiceImpl();

	/**
	 * Methode appelee lorsque l'utilisateur soumet un fichier au serveur
	 * 
	 * @param request
	 *            Requete demandee
	 * @param response
	 *            Reponse de la requete
	 * @throws ServletException
	 *             Si les informations contenues dans le fichier ne concerne pas
	 *             un mois precedent de l'annee courante
	 **/
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException {

		assignmentList = greetingService.getAssignments();
		/*
		 * recuperation dans assignmentList des personnes afin de les mettre
		 * dans l'attribut global personList
		 */
		setPersonList();
		ServletFileUpload upload = new ServletFileUpload();
		try {

			FileItemIterator it = upload.getItemIterator(request);

			while (it.hasNext()) {
				FileItemStream item = it.next();

				parseFile(item);// list = new AssignmentParser(file).parse(item)

				for (Assignment assignment : assignmentList) {
					Objectify ofy = ObjectifyService.beginTransaction();
					ofy.put(assignment);
					ofy.getTxn().commit();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FileUploadException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Decode la premiere ligne du fichier
	 * 
	 * @param line
	 *            Premiere ligne du fichier
	 **/
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

		manageLastItems(getPerson(lastAndFirstName), missionType, missionName,
				activityName, prestationForMonth);
	}

	/**
	 * Permet de retrouver la personne avec ses informations d'apres son nom et
	 * son prenom
	 * 
	 * @param lastAndFirstName
	 *            Nom et prenom de la personne
	 * @return La personne
	 **/
	private Person getPerson(String lastAndFirstName) {
		int indexSpacing = lastAndFirstName.indexOf(" ");
		String lastName = lastAndFirstName.substring(0, indexSpacing);
		String firstName = lastAndFirstName.substring(indexSpacing + 1);

		int indexOfPerson = personList.indexOf(new Person(lastName, firstName));

		if (indexOfPerson < 0)
			return null;

		return personList.get(indexOfPerson);
	}

	/**
	 * Permet d'ajouter une personne e la liste des personnes
	 * 
	 * @param lastAndFirstName
	 *            Nom et prenom de la personne
	 * @return true si la personne a pu etre ajoutee
	 **/
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

	/**
	 * Permet de decoder une ligne du fichier
	 * 
	 * @param line
	 *            Ligne du fichier
	 **/
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

	/**
	 * Permet de parcourir le fichier
	 * 
	 * @param item
	 *            Objet qui represente le fichier e parcourir
	 * @throws ServletException
	 *             Si les informations contenues dans le fichier ne concerne pas
	 *             un mois precedent de l'annee courante
	 **/
	private void parseFile(FileItemStream item) throws ServletException,
			IOException {
		InputStream stream = item.openStream();
		try {

			InputStreamReader inputStreamReader = new InputStreamReader(stream);
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);

			bufferedReader.readLine();
			String line = bufferedReader.readLine();

			decodeFirstLineOfFile(line);

			// Si la periode des informations du fichier est incorrect,une
			// exception est lancee
			if (!isPeriodOfFileCorrect()) {
				throw new ServletException("Periode du fichier invalide !!!");
			}
			while ((line = bufferedReader.readLine()).charAt(0) != ';') {
				decodeLine(line);
			}
		} finally {
			stream.close();
		}
	}

	/**
	 * Indique si les informations contenues dans le fichier concerne bien un
	 * mois precedent de l'annee courante
	 * 
	 * @return true si il s'agit d'informations antecedentes
	 **/
	private boolean isPeriodOfFileCorrect() {
		String month = greetingService.getMonthOfDate();
		String year = greetingService.getYearOfDate();

		return time.getMonth() < Integer.parseInt(month)
				&& time.getYear() == Integer.parseInt(year);
	}

	/**
	 * Decortique une ligne du fichier afin de mettre le resultat dans une liste
	 * 
	 * @param line
	 *            Ligne du fichier
	 * @return La liste
	 **/
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

	/**
	 * Modifie l'attribut Time contenu en global
	 * 
	 * @param yearTxt
	 *            Represente l'annee
	 * @param monthTxt
	 *            Repesente le mois
	 **/
	private void setTime(String yearTxt, String monthTxt) {
		int year = Integer.parseInt(yearTxt);
		int month = Integer.parseInt(monthTxt);

		time = new Time(month, year);
	}

	private void manageLastItems(Person person, String item1, String item2,
			String item3, String item4) {
		item3 = item3.toLowerCase();
		item1 = item1.replace(item1.charAt(1), 'e');
		item1 = item1.replace(item1.charAt(3), 'e');
		item1 = item1.toLowerCase();

		item4 = item4.replace(",", ".");

		double valueOfPrestation = Double.valueOf(item4);

		if (item1.equals("generique")) {
			if (item3.equals("absence")) {
				Record record = OfficialInformation.numberOfficialHolidaysArray[time
						.getMonth() - 1];
				person.getHoliday(time).setNumber(
						valueOfPrestation - record.getNumber());
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

	/**
	 * Modifie la liste des personnes en se basant sur la liste des assignements
	 **/
	private void setPersonList() {
		personList.clear();
		for (Assignment assignment : assignmentList) {
			Person person = assignment.getPerson();

			if (!personList.contains(person)) {
				personList.add(person);
			}
		}
	}
}