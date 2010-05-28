package miro.shared;

import java.io.Serializable;

import javax.persistence.Embedded;
import javax.persistence.Id;

/**
 * This class defines a project contains a person with his prestations
 **/
public class Assignment implements Serializable {

	@Id
	Long id;

	@Embedded
	private Project project;

	@Embedded
	private Person person;

	@Embedded
	private Record[] recordListOfPrestation = new Record[12];

	/**
	 * Defines a Assignment with a project and a person with null value
	 **/
	public Assignment() {
		project = null;
		person = null;
		initRecordListOfPrestation();
	}

	/**
	 * Defines a Assignment with the project and the person specified
	 * 
	 * @param project
	 *            A project
	 * @param personne
	 *            A person
	 **/
	public Assignment(Project project, Person person) {
		this.project = project;
		this.person = person;
		initRecordListOfPrestation();
	}

	private void initRecordListOfPrestation() {
		for (int i = 0; i < recordListOfPrestation.length; i++) {
			recordListOfPrestation[i] = new Record();
		}
	}

	/**
	 * Returns a prestation of the list
	 * 
	 * @param index
	 *            Index of the prestation in the list
	 * @return The prestation
	 **/
	public Record getPrestation(int index) {
		return recordListOfPrestation[index];
	}

	/**
	 * Set a prestation of the list
	 * 
	 * @param index
	 *            Index of the prestation in the list
	 * @param record
	 *            The new prestation
	 **/
	public void setPrestation(int index, Record record) {
		recordListOfPrestation[index] = record;
	}

	/**
	 * Returns the current project
	 * 
	 * @return The project,or null if none was supplied
	 **/
	public Project getProject() {
		return project;
	}

	/**
	 * Returns the current person
	 * 
	 * @return The person,or null if none was supplied
	 **/
	public Person getPerson() {
		return person;
	}

	/**
	 * Set the current person
	 * 
	 * @param person
	 *            The new person
	 **/
	public void setPerson(Person person) {
		this.person = person;
	}

	/**
	 * Compares this Assignment to the specified object
	 * 
	 * @param o
	 *            The object to compare this Assignment against
	 * @return true if the Assignment are equal; false otherwise.
	 **/
	public boolean equals(Object o) {
		boolean isEquals = false;

		if (o instanceof Assignment) {
			Assignment assignment = (Assignment) o;

			isEquals = project.equals(assignment.project)
					&& person.equals(assignment.person);
		}
		return isEquals;
	}
}