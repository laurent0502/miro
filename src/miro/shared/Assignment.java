package miro.shared;

import javax.persistence.Embedded;
import javax.persistence.Id;

import org.apache.log4j.Logger;

import com.google.gwt.user.client.Window;

import java.io.Serializable;

public class Assignment implements Serializable {
    @Id
    Long id;
    @Embedded
    private Project project;
    @Embedded
    private Person person;
    @Embedded
    private Record[] recordListOfPrestation = new Record[12];
    
    static int cpt = 0;
    
    public Assignment() {
    	project = null;
    	person = null;
    	initRecordListOfPrestation();
    }

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
    
    public Record getPrestation(int i){
    	return recordListOfPrestation[i];
    }

    public void setPrestation(int i, Record record) {
        recordListOfPrestation[i] = record;
    }

    public Project getProject() {
        return project;
    }

    public Person getPerson() {
        return person;
    }
    
    public void setPerson(Person person){
    	this.person = person;
    }
    
    public boolean equals(Object o){
        boolean isEquals = false;
        
        if(o instanceof Assignment){
            Assignment assignment = (Assignment)o;

           /* if(project == null){
            	if(assignment.project == null){
            		return 
            	}
            }*/
            isEquals = project.equals(assignment.project) && person.equals(assignment.person); 
        }
        return isEquals;
    }
    
}
