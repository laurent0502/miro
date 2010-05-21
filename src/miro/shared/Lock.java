package miro.shared;

import javax.persistence.Id;

/**
 * Cette classe repr�sente un objet pour repr�senter le verouillage en �criture
 * de la base de donn�es
 **/
public class Lock {

	@Id
	Long id;

	private boolean isLocked = false;

	/**
	 * Contruit un Lock par d�faut
	 **/
	public Lock() {
	}

	/**
	 * Construit un Lock
	 * 
	 * @param id
	 *            Identifiant du Lock
	 **/
	public Lock(Long id) {
		this.id = id;
	}

	/**
	 * Dit si le Lock est verouill�
	 * 
	 * @return true si le Lock est verouill�
	 **/
	public boolean isLocked() {
		return isLocked;
	}

	/**
	 * Modifie le verouillage du Lock
	 * 
	 * @param isLocked
	 *            Indique si le Lock est v�rouill�
	 **/
	public void setLocked(boolean isLocked) {
		this.isLocked = isLocked;
	}
}