package miro.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embedded;

/**
 * This class represents a person from the project department
 **/
public class Person implements Serializable, Comparable {

	private String lastName;
	private String firstName;

	@Embedded
	private List<Record> holidaysList = new ArrayList<Record>();

	@Embedded
	private List<Record> trainingList = new ArrayList<Record>();

	// liste des prestations pour les activites hors-projets
	@Embedded
	private List<Record> otherList = new ArrayList<Record>();

	/**
	 * Defines a person with defaults last name and first name
	 **/
	public Person() {
		initLists();
	}

	/**
	 * Defines a person with a last and first name specified
	 * 
	 * @param lastName
	 *            Last name of the person
	 * @param firstName
	 *            First name of the person
	 **/
	public Person(String lastName, String firstName) {
		this.lastName = lastName.toUpperCase();
		this.firstName = firstName;

		initLists();
	}

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
	 * Returns a benefit from the holidays list for a Time specified
	 * 
	 * @param time
	 *            The time
	 * @return The benefit
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
	 * Returns a benefit from the training list for a time specified
	 * 
	 * @param time
	 *            The time
	 * @return The benefit
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
	 * Returns the benefit from the out-project activities list for a time
	 * specified
	 * 
	 * @param time
	 *            The time
	 * @return The benefit
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
	 * Returns the last name of the person
	 * 
	 * @return The last name
	 **/
	public String getLastName() {
		return lastName;
	}

	/**
	 * Returns the first name of the person
	 * 
	 * @return The first name
	 **/
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Compares this Person to the specified object
	 * 
	 * @param o
	 *            The object to compare this Person against
	 * @return true if the Person are equal; false otherwise.
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
	 * Compares the egality of the last name of two person
	 * 
	 * @param arg0
	 *            The object to compare this Person against
	 * @return 1 if the last name of the current person is greater
	 * @return 0 if the last name of the current person is equals
	 * @return -1 if the last name of the current person is smaller
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