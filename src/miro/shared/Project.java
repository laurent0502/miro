package miro.shared;

import java.io.Serializable;

public class Project implements Serializable,Comparable {

    private String name;

    public Project() {
    }

    public Project(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
/*
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Project other = (Project) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}*/

    public boolean equals(Object o) {
        boolean isEquals = false;
        
        if( o != null && this == null ) return false;
        if( o == null && this != null ) return false;
        
        if( o == null && this == null ) return true;
        //isEquals = ( this != null && o != null ) || (this == null && this == null);
        //isEquals = isEquals && ( getClass() == o.getClass() );
        
        if (o instanceof Project) {
            Project project = (Project) o;

            isEquals = project.name.equals(name);
        }
        return isEquals;
    }
    
    @Override
	public int compareTo(Object arg0) {
		
		if( arg0 instanceof Project){
			Project project = (Project)arg0;
			int compare = name.compareTo(project.name);
			
			if( compare > 0)
				return 1;
			if( compare < 0 )
				return -1;
			
			return 0;
		}
		return 1;
	}
}