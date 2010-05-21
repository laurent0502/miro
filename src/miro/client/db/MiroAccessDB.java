package miro.client.db;

import java.util.List;

import miro.shared.Assignment;
import miro.shared.Connection;
import miro.shared.GreetingService;
import miro.shared.GreetingServiceAsync;
import miro.shared.Person;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Cette classe fait l'intermédiaire entre le code client et le code serveur de
 * mon application
 **/
public class MiroAccessDB {

	static final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	/**
	 * Fait appel au code serveur pour obtenir la liste des assignements
	 * 
	 * @param GET_ASSIGNMENTS_CALLBACK
	 *            Param�tre qui re�oit la r�ponse du serveur
	 **/
	public static void getAssignments(
			AsyncCallback<List<Assignment>> GET_ASSIGNMENTS_CALLBACK) {
		greetingService.getAssignments(GET_ASSIGNMENTS_CALLBACK);
	}

	/**
	 * Fait appel au code serveur pour mettre les assignements à jour
	 * 
	 * @param personList
	 *            Liste des personnes
	 * @param assignmentList
	 *            Liste des assignements
	 * @param UPDATE_ASSIGNMENTS_CALLBACK
	 *            Paramètre qui re�oit la r�ponse du serveur
	 **/
	public static void updateAssignments(List<Person> personList,
			List<Assignment> assignmentList,
			AsyncCallback<String> UPDATE_ASSIGNMENTS_CALLBACK) {
		greetingService.updateAssignments(personList, assignmentList,
				UPDATE_ASSIGNMENTS_CALLBACK);
	}

	/**
	 * Fait appel au serveur pour verouiller/d�verouiller en �criture la
	 * table des assignements
	 * 
	 * @param isLocked
	 *            Dit si la table doit �tre verouill�e
	 * @param SET_LOCKED_CALLBACK
	 *            Param�tre qui re�oit la r�ponse du serveur
	 **/
	public static void setLocked(boolean isLocked,
			AsyncCallback SET_LOCKED_CALLBACK) {
		greetingService.setLocked(isLocked, SET_LOCKED_CALLBACK);
	}

	/**
	 * Fait appel au serveur pour obtenir l'entier qui repr�sente le mois
	 * courant
	 * 
	 * @param MONTH_CALLBACK
	 * 
	 **/
	public static void getMonthOfDate(AsyncCallback<String> MONTH_CALLBACK) {
		greetingService.getMonthOfDate(MONTH_CALLBACK);
	}

	/**
	 * Fait appel au serveur pour obtenir la liste des informations de logging
	 * des utilisateurs
	 * 
	 * @param CONNECTION_CALLBACK
	 *            Param�tre qui re�oit la r�ponse du serveur
	 **/
	public static void getConnections(
			AsyncCallback<List<Connection>> CONNECTION_CALLBACK) {
		greetingService.getConnections(CONNECTION_CALLBACK);
	}
}