package miro.shared;

import java.io.Serializable;

import javax.persistence.Id;

public class Connection implements Serializable {
	@Id
	Long id;

	private String lastName = "";
	private String firstName = "";
	private String pwd = "";

	public Connection() {
	}

	public Connection(String lastName, String firstName, String pwd) {
		this.lastName = lastName;
		this.firstName = firstName;
		this.pwd = pwd;
	}

	public String getLastName() {
		return lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getPwd() {
		return pwd;
	}

	public boolean equals(Object o) {
		boolean isEquals = false;

		if (o instanceof Connection) {
			Connection connection = (Connection) o;
			isEquals = connection.lastName.equals(lastName)
					&& connection.firstName.equals(firstName);
		}
		return isEquals;
	}
}
