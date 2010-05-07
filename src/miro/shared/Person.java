package miro.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

public class Person implements Serializable ,Comparable{
//
	private static final long serialVersionUID = 1L;

	private String lastName;
	private String firstName;
	@Embedded
	private List<Record> holidaysList = new ArrayList<Record>();
	@Embedded
	private List<Record> trainingList = new ArrayList<Record>();
	// liste des prestations pour les activités hors-projets
	@Embedded
	private List<Record> otherList = new ArrayList<Record>();

	public Person() {
		initLists();
	}

	public Person(String lastName, String firstName) {
		this.lastName = lastName.toUpperCase();
		this.firstName = firstName;
		
		replaceSpecialCharactersOfFirstName();
		initLists();
	}
	
	private void replaceSpecialCharactersOfFirstName(){
		
		firstName = firstName.replace("é","e");
		firstName = firstName.replace("è","e");
		firstName = firstName.replace("ô","o");
		firstName = firstName.replace("î","i");
		
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

	public String getLastName() {
		return lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public boolean equals(Object o) {

		boolean isEquals = false;

		if (o instanceof Person) {
			Person person = (Person) o;

			isEquals = person.lastName.equals(lastName)
					&& person.firstName.equals(firstName);
		}

		return isEquals;
	}

	@Override
	public int compareTo(Object arg0) {
		
		if( arg0 instanceof Person){
			Person person = (Person)arg0;
			int compare = lastName.compareTo(person.lastName);
			
			if( compare > 0)
				return 1;
			if( compare < 0 )
				return -1;
			
			return 0;
		}
		return 1;
	}
}