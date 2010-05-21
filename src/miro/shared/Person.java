package miro.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embedded;

/**
 * Cette classe repr�sente une personne du d�partement Projet
 **/
public class Person implements Serializable, Comparable {

	private String lastName;
	private String firstName;

	@Embedded
	private List<Record> holidaysList = new ArrayList<Record>();

	@Embedded
	private List<Record> trainingList = new ArrayList<Record>();

	// liste des prestations pour les activit�s hors-projets
	@Embedded
	private List<Record> otherList = new ArrayList<Record>();

	/**
	 * Construit une personne
	 **/
	public Person() {
		initLists();
	}

	/**
	 * Construit une personne
	 * 
	 * @param lastName
	 *            Nom de la personne
	 * @param firstName
	 *            Pr�nom de la personne
	 **/
	public Person(String lastName, String firstName) {
		this.lastName = lastName.toUpperCase();
		this.firstName = firstName;

		initLists();
	}

	/**
	 * Initialise la liste des absences,formations et activit�s hors-projets
	 **/
	private void initLists() {
		Record record = null;

		for (int i = 0; i < 12; i++) {
			Time time = new Time(i + 1, 2010);
			record = new Record(0, time);

			holidaysList.add(record);
		}

		for (int i = 0; i < 12; i++) {
			Time time = new Time(i + 1, 2010);
			record = new Record(0, time);

			trainingList.add(record);
		}

		for (int i = 0; i < 12; i++) {
			Time time = new Time(i + 1, 2010);
			record = new Record(0, time);

			otherList.add(record);
		}
	}

	/**
	 * Permet d'obtenir la prestation pour une p�riode de vacances de la
	 * personne
	 * 
	 * @param time
	 *            P�riode sur laquelle on veut obtenir la prestation
	 * @return La prestation
	 **/
	public Record getHoliday(final Time time) {
		Record recordToReturn = null;

		for (Record record : holidaysList) {
			Time otherTime = record.getTime();

			if (otherTime.equals(time)) {
				recordToReturn = record;
			}
		}
		return recordToReturn;
	}

	/**
	 * Permet d'obtenir la prestation pour une p�riode de formations de la
	 * personne
	 * 
	 * @param time
	 *            P�riode sur laquelle on veut obtenir la prestation
	 * @return La prestation
	 **/
	public Record getTraining(final Time time) {
		Record recordToReturn = null;

		for (Record record : trainingList) {
			Time otherTime = record.getTime();

			if (otherTime.equals(time)) {
				recordToReturn = record;
			}
		}
		return recordToReturn;
	}

	/**
	 * Permet d'obtenir la prestation pour une p�riode d'activit�s hors-projets
	 * de la personne
	 * 
	 * @param time
	 *            P�riode sur laquelle on veut obtenir la prestation
	 * @return La prestation
	 **/
	public Record getOther(final Time time) {
		Record recordToReturn = null;

		for (Record record : otherList) {
			Time otherTime = record.getTime();

			if (otherTime.equals(time)) {
				recordToReturn = record;
			}
		}
		return recordToReturn;
	}

	/**
	 * Permet d'obtenir le nom de la personne
	 * 
	 * @return Le nom
	 **/
	public String getLastName() {
		return lastName;
	}

	/**
	 * Permet d'obtenir le pr�nom de la personne
	 * 
	 * @return Le pr�nom
	 **/
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Compare par �galit� deux personnes
	 * 
	 * @param o
	 *            Objet � comparer �tant cens� repr�senter une personne
	 * @return true si les deux personnes sont �gaux
	 **/
	public boolean equals(Object o) {

		boolean isEquals = false;

		if (o instanceof Person) {
			Person person = (Person) o;

			isEquals = person.lastName.equals(lastName)
					&& person.firstName.equals(firstName);
		}

		return isEquals;
	}

	/**
	 * Compare par �galit� les noms de deux personnes
	 * 
	 * @param arg0
	 *            Objet qui repr�sente la personne
	 * @return 1 si le nom de la personne courante est plus grande
	 * @return 0 si le nom de la personne courante est �gal
	 * @return -1 si le nom de la personne courante est plus petite
	 **/
	@Override
	public int compareTo(Object arg0) {

		if (arg0 instanceof Person) {
			Person person = (Person) arg0;
			int compare = lastName.compareTo(person.lastName);

			if (compare > 0)
				return 1;
			if (compare < 0)
				return -1;

			return 0;
		}
		return 1;
	}
}