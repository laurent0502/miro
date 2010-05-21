package miro.shared;

import javax.persistence.Id;

/**
 * Cette classe représente un objet pour représenter le verouillage en écriture
 * de la base de données
 **/
public class Lock {

	@Id
	Long id;

	private boolean isLocked = false;

	/**
	 * Contruit un Lock par défaut
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
	 * Dit si le Lock est verouillé
	 * 
	 * @return true si le Lock est verouillé
	 **/
	public boolean isLocked() {
		return isLocked;
	}

	/**
	 * Modifie le verouillage du Lock
	 * 
	 * @param isLocked
	 *            Indique si le Lock est vérouillé
	 **/
	public void setLocked(boolean isLocked) {
		this.isLocked = isLocked;
	}
}