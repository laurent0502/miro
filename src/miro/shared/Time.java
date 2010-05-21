package miro.shared;

import java.io.Serializable;

/**
 * Cette classe représente une période
 **/
public class Time implements Serializable {

	private int month;
	private int year;

	/**
	 * Construit un Time
	 **/
	public Time() {
		month = 0;
		year = 0;
	}

	/**
	 * Construit un Time
	 * 
	 * @param month
	 *            Le mois
	 * @param year
	 *            L'année
	 **/
	public Time(int month, int year) {
		this.month = month;
		this.year = year;
	}

	/**
	 * Permet d'obtenir le mois
	 * 
	 * @return Le mois
	 **/
	public int getMonth() {
		return month;
	}

	/**
	 * Permet d'obtenir l'année
	 * 
	 * @return L'année
	 **/
	public int getYear() {
		return year;
	}

	/**
	 * Compare par égalité deux Time
	 * 
	 * @param o
	 *            Objet qui représente un Time
	 * @return true si les deux éléments sont égaux
	 **/
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Time time = (Time) o;

		if (month != time.month)
			return false;
		if (year != time.year)
			return false;

		return true;
	}
}