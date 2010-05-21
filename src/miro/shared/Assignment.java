package miro.shared;

import java.io.Serializable;

import javax.persistence.Embedded;
import javax.persistence.Id;

/**
 * Cette classe rep�sente un projet associ� � une personne avec les diff�rentes
 * prestations
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
	 * Construit un Assignment par d�faut
	 **/
	public Assignment() {
		project = null;
		person = null;
		initRecordListOfPrestation();
	}

	/**
	 * Construit un Assignment
	 * 
	 * @param project
	 *            Le projet
	 * @param personne
	 *            La personne
	 **/
	public Assignment(Project project, Person person) {
		this.project = project;
		this.person = person;
		initRecordListOfPrestation();
	}

	/**
	 * Permet d'initialiser la liste des prestations
	 **/
	private void initRecordListOfPrestation() {
		for (int i = 0; i < recordListOfPrestation.length; i++) {
			recordListOfPrestation[i] = new Record();
		}
	}

	/**
	 * Permet d'obtenir une prestation de la liste
	 * 
	 * @param index
	 *            Indice de la prestation dans la liste
	 * @return La prestation
	 **/
	public Record getPrestation(int index) {
		return recordListOfPrestation[index];
	}

	/**
	 * Permet de modifier une prestation de la liste
	 * 
	 * @param i
	 *            Indice de la prestation dans la liste
	 * @param record
	 *            Nouvelle prestation
	 **/
	public void setPrestation(int i, Record record) {
		recordListOfPrestation[i] = record;
	}

	/**
	 * Permet d'obtenir le projet
	 * 
	 * @return Le projet
	 **/
	public Project getProject() {
		return project;
	}

	/**
	 * Permet d'obtenir la personne
	 * 
	 * @return La personne
	 **/
	public Person getPerson() {
		return person;
	}

	/**
	 * Modifie la personne
	 * 
	 * @param person
	 *            La personne
	 **/
	public void setPerson(Person person) {
		this.person = person;
	}

	/**
	 * Compare par �galit� deux assignements
	 * 
	 * @param o
	 *            Objet cens� repr�sent� l'Assignment � comparer
	 * @return true si les deux �l�ments sont �gaux
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