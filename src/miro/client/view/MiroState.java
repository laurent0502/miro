package miro.client.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import miro.shared.Assignment;
import miro.shared.Person;
import miro.shared.Project;
import miro.shared.Record;
import miro.shared.Time;

public class MiroState {
	private static List<Person> personList = new ArrayList<Person>();
	private static List<Project> projectList = new ArrayList<Project>();
	private static List<Assignment> assignmentList = new ArrayList<Assignment>();
	public static final int CURRENT_YEAR = 2010;
	
    static final Record[] numberDaysByMonthArray = {
            new Record(30, new Time(1, CURRENT_YEAR)),
            new Record(29, new Time(2, CURRENT_YEAR)),
            new Record(31, new Time(3, CURRENT_YEAR)),
            new Record(31, new Time(4, CURRENT_YEAR)),
            new Record(30, new Time(5, CURRENT_YEAR)),
            new Record(31, new Time(6, CURRENT_YEAR)),
            new Record(30, new Time(7, CURRENT_YEAR)),
            new Record(30, new Time(8, CURRENT_YEAR)),
            new Record(31, new Time(9, CURRENT_YEAR)),
            new Record(31, new Time(10, CURRENT_YEAR)),
            new Record(31, new Time(11, CURRENT_YEAR)),
            new Record(31, new Time(12, CURRENT_YEAR))
    };
    
    static final Record[] numberOfficialHolidaysArray = {
            new Record(1, new Time(1, CURRENT_YEAR)),
            new Record(0, new Time(2, CURRENT_YEAR)),
            new Record(0, new Time(3, CURRENT_YEAR)),
            new Record(1, new Time(4, CURRENT_YEAR)),
            new Record(3, new Time(5, CURRENT_YEAR)),
            new Record(0, new Time(6, CURRENT_YEAR)),
            new Record(1, new Time(7, CURRENT_YEAR)),
            new Record(0, new Time(8, CURRENT_YEAR)),
            new Record(0, new Time(9, CURRENT_YEAR)),
            new Record(0, new Time(10, CURRENT_YEAR)),
            new Record(4, new Time(11, CURRENT_YEAR)),
            new Record(5, new Time(12, CURRENT_YEAR))
    };

    
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
	
	public static List<Person> getPersonNotAssigned(Project project){
		List<Person> personNotAssignedOfProject = new ArrayList<Person>();
		
		if(project!=null){
			for(Person personFromList : personList){
				Assignment assignment = new Assignment(project,personFromList);
				
				if(!assignmentList.contains(assignment)){
					personNotAssignedOfProject.add(personFromList);
				}
			}
		}

		Collections.sort(personNotAssignedOfProject);

		return personNotAssignedOfProject;
	}

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
	
	public static List<Assignment> getAssignments(Project project){
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
	
	public static Person getPerson(String lastName,String firstName){
		Person person = new Person(lastName,firstName);
		
		int indexOf = personList.indexOf(person);
		
		if(indexOf < 0 ){
			return null;
		}
		return personList.get(indexOf);
	}
	
	public static Assignment getAssignment(Person person,Project project){
		
		Assignment assignment = new Assignment(project,person);		
		int indexOfAssignment = assignmentList.indexOf(assignment);
		
		return assignmentList.get(indexOfAssignment);
	}

	public static void updateViewState(List<Assignment> assignmentList) {
		MiroState.assignmentList = assignmentList;
		
		updatePersonList();
		updateProjectList();
	}

	public static void updatePersonList() {
		personList.clear();

		for (Assignment assignment : assignmentList) {
			Person personFromAssignment = assignment.getPerson();

			if (!personList.contains(personFromAssignment)) {
				personList.add(personFromAssignment);
			}
		}

		Collections.sort(personList);

	}

	public static void addAssignment(Assignment assignment) {
		if(assignment != null) assignmentList.add(assignment);
	}

	public static void updateProjectList() {
		projectList.clear();

		for (Assignment assignment : assignmentList) {
			Project projectFromAssignment = assignment.getProject();

			if (!projectList.contains(projectFromAssignment)) {
				projectList.add(projectFromAssignment);
			}
		}

		Collections.sort(projectList);

	}
	
	public static Project getProject(String name){
		Project project = new Project(name);
		
		int indexOfProject = projectList.indexOf(project);
		
		if(indexOfProject < 0 ){
			return null;
		}
		return projectList.get(indexOfProject);
	}

	public static List<Person> getPersonList() {
		return personList;
	}

	public static List<Project> getProjectList() {
		return projectList;
	}
	
	public static List<Assignment> getAssignmentList(){
		return assignmentList;
	}
}
