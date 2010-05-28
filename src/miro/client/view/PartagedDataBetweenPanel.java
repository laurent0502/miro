package miro.client.view;

import miro.shared.Person;
import miro.shared.Project;

/**
 * This class contains a set of informations for the application
 */
public class PartagedDataBetweenPanel {

	static boolean hasChangedView = true;
	static boolean isImporting = false;
	static boolean isReadOnly = false;
	static boolean isLocked = false;
	static boolean isSaving = false;

	static ViewType viewType = ViewType.PERSON_VIEW;

	static Person currentPerson = null;

	static Project currentProject = null;
}
