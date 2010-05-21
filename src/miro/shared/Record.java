package miro.shared;

import java.io.Serializable;

import javax.persistence.Embedded;

/**
 * Cette classe repr�sente une prestation
 **/
public class Record implements Serializable {

	private double number;

	@Embedded
	private Time time;

	/**
	 * Construit un Record
	 **/
	public Record() {
		number = 0;
		time = new Time();
	}

	/**
	 * Construit un Record
	 * 
	 * @param number
	 *            Valeur de la prestation
	 * @param time
	 *            P�riode de la prestation
	 **/
	public Record(double number, Time time) {
		this.number = number;
		this.time = time;
	}

	/**
	 * Modifie la valeur du Record
	 * 
	 * @param number
	 *            Nouvelle valeur du Record
	 * @return true si la modification a pu �tre effectu�e
	 **/
	public boolean setNumber(double number) {
		boolean isModified = (number >= 0);

		if (isModified)
			this.number = number;

		return isModified;
	}

	/**
	 * Permet d'obtenir la valeur du Record
	 * 
	 * @return La valeur
	 **/
	public double getNumber() {
		return number;
	}

	/**
	 * Permet d'obtenir la p�riode du Record
	 * 
	 * @return La p�riode
	 **/
	public Time getTime() {
		return time;
	}

	/**
	 * Compare par �galit� deux Record
	 * 
	 * @param o
	 *            Objet � qui repr�sente un Record
	 * @return true si les deux �l�ments sont �gaux
	 **/
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Record record = (Record) o;

		if (number != record.number)
			return false;

		if (time != null ? !time.equals(record.time) : record.time != null)
			return false;

		return true;
	}
}
