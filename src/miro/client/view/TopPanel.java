package miro.client.view;

import java.util.ArrayList;
import java.util.List;

import miro.client.db.MiroAccessDB;
import miro.shared.Assignment;
import miro.shared.Person;
import miro.shared.Project;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;

/**
 * This class represents the panel containing the north elements of the
 * application
 */
public class TopPanel extends Composite implements EventListener {

	interface TopPanelUiBinder extends UiBinder<AbsolutePanel, TopPanel> {
	}

	static TopPanelUiBinder ourUiBinder = GWT.create(TopPanelUiBinder.class);

	@UiField
	Label labPersonOrProjectChoice;

	@UiField
	Label labPersonOrProjectAffectChoice;

	@UiField
	Label labImportFile;

	@UiField
	ListBox personOrProjectChoiceList;

	@UiField
	ListBox personOrProjectAffectChoiceList;

	@UiField
	FormPanel formPanel;

	@UiField
	AbsolutePanel absolutePanel;

	private final int MARGIN_TOP_OF_COMPONENTS = 15;

	private List<Person> personList;
	private List<Project> projectList;
	private List<Project> projectsNotAssignedForPerson;
	private List<Person> personsNotAssignedForProject;

	private Button submitButton = new Button("SUBMIT");

	private static List<EventListener> eventListenerList = new ArrayList<EventListener>();

	/**
	 * Defines a TopPanel
	 */
	public TopPanel() {

		initWidget(ourUiBinder.createAndBindUi(this));
		initTopPanel();

		CenterPanel.addEventListener(this);
		BottomPanel.addEventListener(this);
	}

	private void initTopPanel() {
		initAbsolutePanel();

		boolean isReadOnly = PartagedDataBetweenPanel.isReadOnly;

		labPersonOrProjectAffectChoice.setVisible(!isReadOnly);
		labImportFile.setVisible(!isReadOnly);
		personOrProjectAffectChoiceList.setVisible(!isReadOnly);
		formPanel.setVisible(!isReadOnly);

		refreshPersonOrProjectChoiceList();
		refreshPersonOrProjectAffectChoiceList();
		initListeners();
	}

	private void disabledTopPanel() {
		personOrProjectChoiceList.setEnabled(false);
		personOrProjectAffectChoiceList.setEnabled(false);
		submitButton.setEnabled(false);
	}

	private void enabledTopPanel() {
		personOrProjectChoiceList.setEnabled(true);
		personOrProjectAffectChoiceList.setEnabled(true);
		submitButton.setEnabled(true);
	}

	/**
	 * Adds the specified event listener to receive action events
	 * 
	 * @param eventListener
	 *            The event listener
	 */
	public static void addEventListener(EventListener eventListener) {
		eventListenerList.add(eventListener);
	}

	private void notifyListeners() {
		for (EventListener eventListener : eventListenerList) {
			eventListener.notifyChange(this);
		}
	}

	private void initAbsolutePanel() {
		absolutePanel.setSize("1300px", "50px");
		absolutePanel.setWidgetPosition(labPersonOrProjectChoice, 25,
				MARGIN_TOP_OF_COMPONENTS);
		absolutePanel.setWidgetPosition(personOrProjectChoiceList, 170,
				MARGIN_TOP_OF_COMPONENTS);
		absolutePanel.setWidgetPosition(labPersonOrProjectAffectChoice, 450,
				MARGIN_TOP_OF_COMPONENTS);
		absolutePanel.setWidgetPosition(personOrProjectAffectChoiceList, 590,
				MARGIN_TOP_OF_COMPONENTS);
		absolutePanel.setWidgetPosition(labImportFile, 870,
				MARGIN_TOP_OF_COMPONENTS);
		absolutePanel.setWidgetPosition(formPanel, 1000,
				MARGIN_TOP_OF_COMPONENTS);

		FileUpload fileUpload = new FileUpload();
		fileUpload.setName("uploadFormElement");

		submitButton.setEnabled(false);

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.add(fileUpload);
		horizontalPanel.add(submitButton);

		formPanel.setAction(GWT.getModuleBaseURL() + "SampleUploadServlet");
		formPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
		formPanel.setMethod(FormPanel.METHOD_POST);
		formPanel.setWidget(horizontalPanel);
	}

	private void refreshLabPersonOrProjectChoice() {

		switch (PartagedDataBetweenPanel.viewType) {
		case PERSON_VIEW:
			labPersonOrProjectChoice.setText("Choix de la personne : ");
			break;
		case PROJECT_VIEW:
			labPersonOrProjectChoice.setText("Choix du projet : ");
			break;
		}

	}

	private void setSelectedItemFromPersonOrProjectChoiceList() {
		String txt = "";

		switch (PartagedDataBetweenPanel.viewType) {
		case PERSON_VIEW:
			if (PartagedDataBetweenPanel.currentPerson != null) {
				String firstName = PartagedDataBetweenPanel.currentPerson
						.getFirstName();
				String lastName = PartagedDataBetweenPanel.currentPerson
						.getLastName();
				txt = lastName + " " + firstName;
			}
			break;
		case PROJECT_VIEW:
			if (PartagedDataBetweenPanel.currentProject != null) {
				txt = PartagedDataBetweenPanel.currentProject.getName();
			}

			break;
		}
		boolean isFinded = false;
		int index = 1;

		while (!isFinded && index < personOrProjectChoiceList.getItemCount()) {
			isFinded = personOrProjectChoiceList.getItemText(index).equals(txt);
			index++;
		}

		if (isFinded)
			personOrProjectChoiceList.setSelectedIndex(index - 1);
	}

	private void refreshLabProjectOrPersonAffectChoice() {

		switch (PartagedDataBetweenPanel.viewType) {
		case PERSON_VIEW:
			labPersonOrProjectAffectChoice.setText("Affecter un projet :");
			break;
		case PROJECT_VIEW:
			labPersonOrProjectAffectChoice.setText("Affecter une personne :");
			break;
		}

	}

	private void refreshPersonOrProjectAffectChoiceList() {
		int selectedIndex = personOrProjectChoiceList.getSelectedIndex();

		personOrProjectAffectChoiceList.clear();
		personOrProjectAffectChoiceList.addItem("Selectionnez");

		switch (PartagedDataBetweenPanel.viewType) {
		case PERSON_VIEW:
			if (selectedIndex != 0) {
				projectsNotAssignedForPerson = MiroState
						.getNotAssignedProject(PartagedDataBetweenPanel.currentPerson);

				for (Project projectFromList : projectsNotAssignedForPerson) {
					personOrProjectAffectChoiceList.addItem(projectFromList
							.getName());
				}
			}
			break;
		case PROJECT_VIEW:
			if (selectedIndex != 0) {
				personsNotAssignedForProject = MiroState
						.getPersonNotAssigned(PartagedDataBetweenPanel.currentProject);

				for (Person personFromList : personsNotAssignedForProject) {
					String lastName = personFromList.getLastName();
					String firstName = personFromList.getFirstName();
					String item = lastName + " " + firstName;
					personOrProjectAffectChoiceList.addItem(item);
				}
			}
			break;
		}
	}

	private void initListeners() {
		personOrProjectChoiceList.addChangeHandler(new ChangeHandler() {

			public void onChange(ChangeEvent changeEvent) {
				int selectedIndexPersonChoiceList = personOrProjectChoiceList
						.getSelectedIndex();

				if (selectedIndexPersonChoiceList != 0) {

					switch (PartagedDataBetweenPanel.viewType) {
					case PERSON_VIEW:
						PartagedDataBetweenPanel.currentPerson = personList
								.get(selectedIndexPersonChoiceList - 1);
						break;

					case PROJECT_VIEW:
						PartagedDataBetweenPanel.currentProject = projectList
								.get(selectedIndexPersonChoiceList - 1);
						break;
					}
				} else {

					switch (PartagedDataBetweenPanel.viewType) {
					case PERSON_VIEW:
						PartagedDataBetweenPanel.currentPerson = null;
						break;
					case PROJECT_VIEW:
						PartagedDataBetweenPanel.currentProject = null;
						break;
					}
				}
				refreshPersonOrProjectAffectChoiceList();
				notifyListeners();
			}
		});

		personOrProjectAffectChoiceList.addChangeHandler(new ChangeHandler() {

			public void onChange(ChangeEvent changeEvent) {
				int selectedIndex = personOrProjectAffectChoiceList
						.getSelectedIndex();

				switch (PartagedDataBetweenPanel.viewType) {
				case PERSON_VIEW:
					Person currentPerson = PartagedDataBetweenPanel.currentPerson;

					if (selectedIndex != 0) {

						Project projectToAffect = projectsNotAssignedForPerson
								.get(selectedIndex - 1);
						Assignment assignment = new Assignment(projectToAffect,
								currentPerson);

						MiroState.addAssignment(assignment);

						refreshPersonOrProjectAffectChoiceList();
					}

					break;
				case PROJECT_VIEW:

					Project currentProject = PartagedDataBetweenPanel.currentProject;

					if (selectedIndex != 0) {

						Person personToAffect = personsNotAssignedForProject
								.get(selectedIndex - 1);
						Assignment assignment = new Assignment(currentProject,
								personToAffect);

						MiroState.addAssignment(assignment);

						refreshPersonOrProjectChoiceList();
						refreshPersonOrProjectAffectChoiceList();
					}
					break;
				}
				notifyListeners();
			}
		});

		submitButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				PartagedDataBetweenPanel.isImporting = true;
				disabledTopPanel();
				formPanel.submit();

				notifyListeners();
			}
		});

		formPanel.addSubmitCompleteHandler(new SubmitCompleteHandler() {

			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				PartagedDataBetweenPanel.isImporting = false;

				final AsyncCallback<List<Assignment>> GET_ASSIGNMENTS_CALLBACK = new AsyncCallback<List<Assignment>>() {

					@Override
					public void onFailure(Throwable caught) {
						Window
								.alert("Impossible d'obtenir les assignements ! ");
					}

					@Override
					public void onSuccess(List<Assignment> result) {
						MiroState.updateViewState(result);

						refreshPersonOrProjectChoiceList();
						setSelectedItemFromPersonOrProjectChoiceList();
						refreshPersonOrProjectAffectChoiceList();
						enabledTopPanel();
						notifyListeners();
						Window
								.alert("Mise a jour et rafrachissement effectues");
					}
				};
				MiroAccessDB.getAssignments(GET_ASSIGNMENTS_CALLBACK);
			}
		});
	}

	private void refreshPersonOrProjectChoiceList() {
		personOrProjectChoiceList.clear();
		personOrProjectChoiceList.addItem("Selectionnez");

		switch (PartagedDataBetweenPanel.viewType) {
		case PERSON_VIEW:
			personList = MiroState.getPersonList();

			for (Person person : personList) {
				String firstNameOfPerson = person.getFirstName();
				String lastNameOfPerson = person.getLastName();
				String item = lastNameOfPerson + " " + firstNameOfPerson;

				personOrProjectChoiceList.addItem(item);
			}
			break;
		case PROJECT_VIEW:
			projectList = MiroState.getProjectList();

			for (Project project : projectList) {
				String nameOfProject = project.getName();
				personOrProjectChoiceList.addItem(nameOfProject);
			}
			break;
		}
	}

	/**
	 * Implemented method for all listeners
	 * 
	 * @param widget
	 *            The widget sending the event
	 */
	@Override
	public void notifyChange(Widget widget) {

		if (PartagedDataBetweenPanel.isReadOnly) {
			labPersonOrProjectAffectChoice.setVisible(false);
			labImportFile.setVisible(false);
			personOrProjectAffectChoiceList.setVisible(false);
			formPanel.setVisible(false);
		} else {

			if (widget instanceof BottomPanel) {

				if (PartagedDataBetweenPanel.isLocked) {
					submitButton.setEnabled(true);

					if (PartagedDataBetweenPanel.isSaving) {
						disabledTopPanel();
					}
				} else {
					enabledTopPanel();
					submitButton.setEnabled(false);
				}
			}

		}
		refreshLabPersonOrProjectChoice();
		refreshLabProjectOrPersonAffectChoice();
		refreshPersonOrProjectChoiceList();
		setSelectedItemFromPersonOrProjectChoiceList();
		refreshPersonOrProjectAffectChoiceList();
	}
}