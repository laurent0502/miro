package miro.shared;

import java.io.Serializable;

/**
 * Cette classe repr�sente un projet du d�partement Projet
 **/
public class Project implements Serializable, Comparable {

	private String name;

	/**
	 * Construit un Project par d�faut
	 **/
	public Project() {
		name = "";
	}

	/**
	 * Construit un Project
	 * 
	 * @param name
	 *            Nom du projet
	 **/
	public Project(String name) {
		this.name = name;
	}

	/**
	 * Permet d'obtenir le nom du projet
	 * 
	 * @return Le nom
	 **/
	public String getName() {
		return name;
	}

	/**
	 * Compare par �galit� deux Project
	 * 
	 * @param o
	 *            Objet � comparer et cens� repr�senter un Projet
	 * @return true si les deux �l�ments sont �gaux
	 **/
	public boolean equals(Object o) {
		boolean isEquals = false;

		if (o != null && this == null)
			return false;
		if (o == null && this != null)
			return false;

		if (o == null && this == null)
			return true;

		if (o instanceof Project) {
			Project project = (Project) o;

			isEquals = project.name.equals(name);
		}
		return isEquals;
	}

	/**
	 * Compare le nom de deux projets
	 * 
	 * @param arg0
	 *            Objet cens� repr�senter le projet � comparer
	 * @return 1 si le nom du projet courant est plus grand que l'autre
	 * @return 0 si le nom des deux projets sont �gaux
	 * @return -1 si le nom du projet courant est plus petit que l'autre
	 **/
	@Override
	public int compareTo(Object arg0) {

		if (arg0 instanceof Project) {
			Project project = (Project) arg0;
			int compare = name.compareTo(project.name);

			if (compare > 0)
				return 1;
			if (compare < 0)
				return -1;

			return 0;
		}
		return 1;
	}
}