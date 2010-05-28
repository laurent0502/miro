package miro.client.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import miro.shared.Assignment;
import miro.shared.Person;
import miro.shared.Project;

/**
 * This class contains the informations to print
 */
public class MiroState {
	private static List<Person> personList = new ArrayList<Person>();
	private static List<Project> projectList = new ArrayList<Project>();
	private static List<Assignment> assignmentList = new ArrayList<Assignment>();

	/**
	 * Returns the project list which contains the not assigned projects for a
	 * person specified
	 * 
	 * @param person
	 *            The person
	 * @return The list
	 */
	public static List<Project> getNotAssignedProject(Person person) {
		List<Project> projectNotAssigned = new ArrayList<Project>();

		if (person != null) {
			for (Project projectFromList : projectList) {
				Assignment assignment = new Assignment(projectFromList, person);

				if (!assignmentList.contains(assignment))
					projectNotAssigned.add(projectFromList);
			}
		}
		Collections.sort(projectNotAssigned);

		return projectNotAssigned;
	}

	/**
	 * Return the person list which contains the persons not affected on the
	 * project specified
	 * 
	 * @param project
	 *            The project
	 * @return The list
	 */
	public static List<Person> getPersonNotAssigned(Project project) {
		List<Person> personNotAssignedOfProject = new ArrayList<Person>();

		if (project != null) {
			for (Person personFromList : personList) {
				Assignment assignment = new Assignment(project, personFromList);

				if (!assignmentList.contains(assignment)) {
					personNotAssignedOfProject.add(personFromList);
				}
			}
		}

		Collections.sort(personNotAssignedOfProject);

		return personNotAssignedOfProject;
	}

	/**
	 * Return the assignments of a person
	 * 
	 * @param person
	 *            The person
	 * @return The list
	 */
	public static List<Assignment> getAssignments(Person person) {
		List<Assignment> assignmentFromPerson = new ArrayList<Assignment>();

		if (person != null) {
			for (Assignment assignment : assignmentList) {

				if (assignment.getPerson().equals(person)) {
					assignmentFromPerson.add(assignment);
				}
			}
		}
		return assignmentFromPerson;
	}

	/**
	 * Return the assignments list for a project specified
	 * 
	 * @param project
	 *            The project
	 * @return The list
	 */
	public static List<Assignment> getAssignments(Project project) {
		List<Assignment> assignmentFromProject = new ArrayList<Assignment>();

		if (project != null) {
			for (Assignment assignment : assignmentList) {

				if (assignment.getProject().equals(project)) {
					assignmentFromProject.add(assignment);
				}
			}
		}
		return assignmentFromProject;
	}

	/**
	 * Returns a person with its last and first name specified
	 * 
	 * @param lastName
	 *            Last name of the person
	 * @param firstName
	 *            First name of the person
	 * @return The person
	 */
	public static Person getPerson(String lastName, String firstName) {
		Person person = new Person(lastName, firstName);

		int indexOf = personList.indexOf(person);

		if (indexOf < 0) {
			return null;
		}
		return personList.get(indexOf);
	}

	/**
	 * Returns the assignment from a person and a project specified
	 * 
	 * @param person
	 *            The person
	 * @param project
	 *            The project
	 * @return The assignment
	 */
	public static Assignment getAssignment(Person person, Project project) {

		Assignment assignment = new Assignment(project, person);
		int indexOfAssignment = assignmentList.indexOf(assignment);

		return assignmentList.get(indexOfAssignment);
	}

	static void updateViewState(List<Assignment> assignmentList) {
		MiroState.assignmentList = assignmentList;

		updatePersonList();
		updateProjectList();
	}

	private static void updatePersonList() {
		personList.clear();

		for (Assignment assignment : assignmentList) {
			Person personFromAssignment = assignment.getPerson();

			if (!personList.contains(personFromAssignment)) {
				personList.add(personFromAssignment);
			}
		}

		Collections.sort(personList);

	}

	static void addAssignment(Assignment assignment) {
		if (assignment != null)
			assignmentList.add(assignment);
	}

	private static void updateProjectList() {
		projectList.clear();

		for (Assignment assignment : assignmentList) {
			Project projectFromAssignment = assignment.getProject();

			if (!projectList.contains(projectFromAssignment)) {
				projectList.add(projectFromAssignment);
			}
		}

		Collections.sort(projectList);

	}

	/**
	 * Returns the project with its name specified
	 * 
	 * @param projectName
	 *            Name of the project
	 * @return The project
	 */
	public static Project getProject(String projectName) {
		Project project = new Project(projectName);

		int indexOfProject = projectList.indexOf(project);

		if (indexOfProject < 0) {
			return null;
		}
		return projectList.get(indexOfProject);
	}

	static List<Person> getPersonList() {
		return personList;
	}

	static List<Project> getProjectList() {
		return projectList;
	}

	static List<Assignment> getAssignmentList() {
		return assignmentList;
	}
}
