package miro.shared;

import java.io.Serializable;

/**
 * Cette classe représente un projet du département Projet
 **/
public class Project implements Serializable, Comparable {

	private String name;

	/**
	 * Construit un Project par défaut
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
	 * Compare par égalité deux Project
	 * 
	 * @param o
	 *            Objet à comparer et censé représenter un Projet
	 * @return true si les deux éléments sont égaux
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
	 *            Objet censé représenter le projet à comparer
	 * @return 1 si le nom du projet courant est plus grand que l'autre
	 * @return 0 si le nom des deux projets sont égaux
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