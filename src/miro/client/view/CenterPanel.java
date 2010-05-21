package miro.client.view;

import java.util.ArrayList;
import java.util.List;

import miro.client.db.MiroAccessDB;
import miro.shared.Assignment;
import miro.shared.OfficialInformation;
import miro.shared.Person;
import miro.shared.Project;
import miro.shared.Record;
import miro.shared.Time;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class CenterPanel extends Composite implements EventListener {

	interface GlobalResources extends ClientBundle {
		@CssResource.NotStrict
		@Source("style.css")
		CssResource css();
	}

	interface CenterPanelUiBinder extends UiBinder<ScrollPanel, CenterPanel> {
	}

	private static CenterPanelUiBinder ourUiBinder = GWT
			.create(CenterPanelUiBinder.class);

	private MonthRow monthRow = new MonthRow(OfficialInformation.CURRENT_YEAR);
	private CalcRow calcRowForProjectView = new CalcRow("Total prestations",
			false);

	private TitleRow[] titlesRowArray = new TitleRow[5];
	private CalcRow[] calcRowArray = new CalcRow[8];

	private List<ProjectRow> projectRowList = new ArrayList<ProjectRow>();

	private static List<EventListener> eventListenerList = new ArrayList<EventListener>();
	/*
	 * Permet l'insertion de projets au bon endroit dans le tableau
	 */
	private int indexDeparture;
	private int currentMonth;

	@UiField
	FlexTable personArray;

	public CenterPanel() {
		GWT.<GlobalResources> create(GlobalResources.class).css()
				.ensureInjected();
		initWidget(ourUiBinder.createAndBindUi(this));

		AsyncCallback<String> callback = new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(String result) {
				currentMonth = Integer.parseInt(result);
				refreshCenterPanel();
			}
		};
		MiroAccessDB.getMonthOfDate(callback);
		TopPanel.addEventListener(this);
		BottomPanel.addEventListener(this);
	}

	public static void addEventListener(EventListener eventListener) {
		eventListenerList.add(eventListener);
	}

	public void refreshCenterPanel() {
		if (PartagedDataBetweenPanel.hasChangedView) {
			PartagedDataBetweenPanel.hasChangedView = false;

			initStyleNameOfMonthRow();

			switch (PartagedDataBetweenPanel.viewType) {
			case PERSON_VIEW:
				indexDeparture = 7;
				initTitleRowList();
				initTitleRowListeners();
				initCalcRowList();
				refreshProjectRowList();
				refreshCalcRowList();
				refreshStyleOfTotalPourcentRow();
				break;

			case PROJECT_VIEW:
				indexDeparture = 1;
				refreshProjectRowList();
				refreshCalcRowForProjectView();
				break;
			}
		} else {
			switch (PartagedDataBetweenPanel.viewType) {
			case PERSON_VIEW:
				refreshTitleRowList();
				refreshProjectRowList();
				refreshCalcRowList();
				refreshStyleOfTotalPourcentRow();
				break;
			case PROJECT_VIEW:
				refreshProjectRowList();
				refreshCalcRowForProjectView();
				break;
			}
		}
		refreshArray();
	}

	private void disabledColumnsOfPreviousMonth() {
		for (int i = 0; i < titlesRowArray.length; i++) {
			for (int j = 2; j < currentMonth + 1; j++) {
				((TextBox) (titlesRowArray[i].getElementAt(j)))
						.setReadOnly(true);
			}
		}

		for (int i = 0; i < projectRowList.size(); i++) {
			for (int j = 2; j < currentMonth + 1; j++) {
				((TextBox) (projectRowList.get(i).getElementAt(j)))
						.setReadOnly(true);
			}
		}

		for (int i = 0; i < calcRowArray.length; i++) {
			for (int j = 2; j < currentMonth + 1; j++) {
				((TextBox) (calcRowArray[i].getElementAt(j))).setReadOnly(true);
			}
		}
	}

	private void initTitleRowListeners() {
		for (int i = 0; i < titlesRowArray.length; i++) {
			for (int j = 2; j < titlesRowArray[i].length(); j++) {
				TextBox textboxOfTitleList = (TextBox) titlesRowArray[i]
						.getElementAt(j);
				double valueOfTextbox = Double.valueOf(textboxOfTitleList
						.getText());

				textboxOfTitleList.addChangeHandler(new MyTitleListener(i + 1,
						j, valueOfTextbox));
			}
		}
	}

	private void disabledAllTitleRow() {
		for (int i = 2; i < titlesRowArray.length; i++) {
			for (int j = 1; j < titlesRowArray[i].length(); j++) {
				((TextBox) titlesRowArray[i].getElementAt(j)).setReadOnly(true);
			}
		}
	}

	private void enabledAllTitleRow() {
		for (int i = 2; i < titlesRowArray.length; i++) {
			for (int j = 2; j < titlesRowArray[i].length(); j++) {
				((TextBox) titlesRowArray[i].getElementAt(j))
						.setReadOnly(false);
			}
		}
	}

	private void disabledOrEnabledAllTitleRow() {
		switch (PartagedDataBetweenPanel.viewType) {
		case PERSON_VIEW:
			if (PartagedDataBetweenPanel.currentPerson == null) {
				disabledAllTitleRow();
			} else {
				enabledAllTitleRow();
			}
			break;
		}
	}

	private void refreshCalcRowForProjectView() {
		double sumOfRow = 0;

		((TextBox) calcRowForProjectView.getElementAt(0))
				.setStyleName("titleOfFirstColumn");

		for (int i = 2; i < 14; i++) {
			double sum = 0;

			for (int j = 0; j < projectRowList.size(); j++) {
				TextBox txtBoxOfProjectRowList = (TextBox) (projectRowList
						.get(j).getElementAt(i));
				String txtOfTextBox = txtBoxOfProjectRowList.getText();
				double valueOfCell = Double.valueOf(txtOfTextBox);

				sum += valueOfCell;
			}
			sumOfRow += sum;
			calcRowForProjectView.setElementAt(i, sum);
		}
		calcRowForProjectView.setElementAt(1, sumOfRow);
	}

	private void refreshStyleOfTotalPourcentRow() {

		for (int i = 1; i < calcRowArray[6].length(); i++) {

			TextBox textBoxOfACell = (TextBox) (calcRowArray[6].getElementAt(i));
			String txtNumberOfTextBox = textBoxOfACell.getText();
			txtNumberOfTextBox = txtNumberOfTextBox.substring(0,
					txtNumberOfTextBox.length() - 1);

			double valueOfTextBox = Double.valueOf(txtNumberOfTextBox);

			textBoxOfACell.removeStyleName("green");
			textBoxOfACell.removeStyleName("red");

			if (valueOfTextBox > 100) {
				textBoxOfACell.addStyleName("red");
			}
			else{
				textBoxOfACell.addStyleName("green");
			}
		}
	}

	private void refreshProjectRowList() {
		List<Assignment> assignmentList;

		projectRowList.clear();

		// Obtention de la liste d'assignement qui nous concerne
		switch (PartagedDataBetweenPanel.viewType) {
		case PERSON_VIEW:
			assignmentList = MiroState
					.getAssignments(PartagedDataBetweenPanel.currentPerson);
			break;
		case PROJECT_VIEW:
			assignmentList = MiroState
					.getAssignments(PartagedDataBetweenPanel.currentProject);
			break;
		default:
			assignmentList = new ArrayList<Assignment>();
		}

		// Parcours des assignments pour les mettre dans des ProjectRow
		for (int i = 0; i < assignmentList.size(); i++) {
			Assignment assignment = assignmentList.get(i);
			String personOrProjectName = "";

			switch (PartagedDataBetweenPanel.viewType) {
			case PERSON_VIEW:
				personOrProjectName = assignment.getProject().getName();
				break;
			case PROJECT_VIEW:
				personOrProjectName = assignment.getPerson().getLastName()
						+ " " + assignment.getPerson().getFirstName();
				break;
			}
			ProjectRow projectRow = new ProjectRow(personOrProjectName);

			// TextBox textBox = (TextBox) projectRow.getElementAt(0);
			((TextBox) projectRow.getElementAt(0))
					.setStyleName("titleOfFirstColumn");
			((TextBox) projectRow.getElementAt(0))
					.addStyleName("textbox-nameOfProjectOrPerson");
			((TextBox) projectRow.getElementAt(0))
					.addClickHandler(new switchingViewListener(
							personOrProjectName));

			for (int j = 0; j < 12; j++) {
				Record record = assignment.getPrestation(j);
				int sizeProjectRowList = projectRowList.size();
				MyProjectListener projectListener = new MyProjectListener(
						sizeProjectRowList, j + 1, record.getNumber());
				TextBox textBox2 = (TextBox) projectRow.getElementAt(j + 2);

				textBox2.addChangeHandler(projectListener);
				projectRow.setElementAt(j + 2, record.getNumber());
			}
			double sumOfTheProjectRow = projectRow.sumRow();
			projectRow.setElementAt(1, sumOfTheProjectRow);
			projectRowList.add(projectRow);
		}
	}

	private void initStyleNameOfMonthRow() {
		((TextBox) monthRow.getElementAt(0)).setStyleName("titleOfFirstColumn");

		for (int i = 1; i < monthRow.length(); i++) {
			((TextBox) monthRow.getElementAt(i))
					.removeStyleName("titleOfFirstRow");
			((TextBox) monthRow.getElementAt(i))
					.addStyleName("titleOfFirstRow");
		}
	}

	private void initTitleRowList() {
		Record[] recordOfNumberDaysByMonth = OfficialInformation.numberDaysByMonthArray;
		Record[] recordOfNumberOfficialHolidaysArray = OfficialInformation.numberOfficialHolidaysArray;
		titlesRowArray[0] = new TitleRow("Nombre Total de Jours", false);
		titlesRowArray[1] = new TitleRow("Conges legaux & CIRB", false);
		titlesRowArray[2] = new TitleRow("Conges et Absences", true);
		titlesRowArray[3] = new TitleRow("Formations", true);
		titlesRowArray[4] = new TitleRow("Activites Hors-Projets", true);

		for (int i = 2; i < titlesRowArray[0].length(); i++) {
			titlesRowArray[0].setElementAt(i, recordOfNumberDaysByMonth[i - 2]
					.getNumber());
			titlesRowArray[1].setElementAt(i,
					recordOfNumberOfficialHolidaysArray[i - 2].getNumber());
		}
		titlesRowArray[0].setElementAt(1, titlesRowArray[0].sumRow());
		titlesRowArray[1].setElementAt(1, titlesRowArray[1].sumRow());

		for (int i = 0; i < titlesRowArray.length; i++) {
			((TextBox) titlesRowArray[i].getElementAt(0))
					.setStyleName("titleOfFirstColumn");
		}
	}

	private void initCalcRowList() {
		calcRowArray[0] = new CalcRow("Jours disponibles projets", false);
		calcRowArray[1] = new CalcRow("Total Projets", false);
		calcRowArray[2] = new CalcRow("% Conges & Absences", true);
		calcRowArray[3] = new CalcRow("% Formations", true);
		calcRowArray[4] = new CalcRow("% Activites hors-projets", true);
		calcRowArray[5] = new CalcRow("% Projets", true);
		calcRowArray[6] = new CalcRow("Total en %", true);
		calcRowArray[7] = new CalcRow("KPI - % Affectation Projets", true);

		for (int i = 0; i < calcRowArray.length; i++) {
			((TextBox) calcRowArray[i].getElementAt(0))
					.setStyleName("titleOfFirstColumn");
		}
	}

	private void refreshTitleRowList() {
		// brol();
		for (int i = 2; i < titlesRowArray.length; i++) {
			for (int j = 2; j < titlesRowArray[i].length(); j++) {
				Record record = new Record();
				Time time = new Time(j - 1, OfficialInformation.CURRENT_YEAR);

				if (PartagedDataBetweenPanel.currentPerson != null) {
					switch (i) {
					case 2:
						record = PartagedDataBetweenPanel.currentPerson
								.getHoliday(time);
						break;
					case 3:
						record = PartagedDataBetweenPanel.currentPerson
								.getTraining(time);
						break;
					case 4:
						record = PartagedDataBetweenPanel.currentPerson
								.getOther(time);
						break;
					}
				}
				titlesRowArray[i].setElementAt(j, record.getNumber());
			}
			double sumOfTheTitleRow = titlesRowArray[i].sumRow();
			titlesRowArray[i].setElementAt(1, sumOfTheTitleRow);
		}
	}

	private void manageProjectDaysAvailabilityRow(int i, int j) {
		String numberDaysForMonthTxt = ((TextBox) (titlesRowArray[0]
				.getElementAt(j))).getText();
		String numberOfficialHolidaysForMonthTxt = ((TextBox) (titlesRowArray[1]
				.getElementAt(j))).getText();
		String numberHolidaysForMonthTxt = ((TextBox) (titlesRowArray[2]
				.getElementAt(j))).getText();
		String numberTrainingForMonthTxt = ((TextBox) (titlesRowArray[3]
				.getElementAt(j))).getText();
		String numberOutProjectActivitiesForMonthTxt = ((TextBox) (titlesRowArray[4]
				.getElementAt(j))).getText();

		double numberDaysForMonth = Double.valueOf(numberDaysForMonthTxt);
		double numberOfficialHolidaysForMonth = Double
				.valueOf(numberOfficialHolidaysForMonthTxt);
		double numberHolidaysForMonth = Double
				.valueOf(numberHolidaysForMonthTxt);
		double numberTrainingForMonth = Double
				.valueOf(numberTrainingForMonthTxt);
		double numberOutProjectActivitiesForMonth = Double
				.valueOf(numberOutProjectActivitiesForMonthTxt);

		double numberAvailabilityProjectDays = numberDaysForMonth
				- numberOfficialHolidaysForMonth - numberHolidaysForMonth
				- numberTrainingForMonth - numberOutProjectActivitiesForMonth;

		calcRowArray[i].setElementAt(j, numberAvailabilityProjectDays);
	}

	private void manageProjectTotalRow(int i, int j) {
		double sum = 0;

		for (int index = 0; index < projectRowList.size(); index++) {
			String prestationNumberForTheProject = ((TextBox) projectRowList
					.get(index).getElementAt(j)).getText();
			double valueOfPrestation = Double
					.valueOf(prestationNumberForTheProject);

			sum += valueOfPrestation;
		}
		calcRowArray[i].setElementAt(j, sum);
	}

	private void manageHolidaysPourcentRow(int i, int j) {
		String numberDaysForMonthTxt = ((TextBox) (titlesRowArray[0]
				.getElementAt(j))).getText();
		String numberOfficialHolidaysForMonthTxt = ((TextBox) (titlesRowArray[1]
				.getElementAt(j))).getText();
		String numberHolidaysForMonthTxt = ((TextBox) (titlesRowArray[2]
				.getElementAt(j))).getText();

		double numberDaysForMonth = Double.valueOf(numberDaysForMonthTxt);
		double numberOfficialHolidaysForMonth = Double
				.valueOf(numberOfficialHolidaysForMonthTxt);
		double numberHolidaysForMonth = Double
				.valueOf(numberHolidaysForMonthTxt);
		double holidayPourcent = 0;

		if (numberDaysForMonth > 0) {
			holidayPourcent = (double) (((numberOfficialHolidaysForMonth + numberHolidaysForMonth) / numberDaysForMonth) * 100);
		}
		calcRowArray[i].setElementAt(j, holidayPourcent);
	}

	private void manageTrainingPourcentRow(int i, int j) {
		String numberDaysForMonthTxt = ((TextBox) (titlesRowArray[0]
				.getElementAt(j))).getText();
		String numberTrainingForMonthTxt = ((TextBox) (titlesRowArray[3]
				.getElementAt(j))).getText();

		double numberDaysForMonth = Double.valueOf(numberDaysForMonthTxt);
		double numberTrainingForMonth = Double
				.valueOf(numberTrainingForMonthTxt);
		double trainingPourcent = 0;

		if (numberDaysForMonth > 0) {
			trainingPourcent = (double) ((numberTrainingForMonth / numberDaysForMonth) * 100);
		}
		calcRowArray[i].setElementAt(j, trainingPourcent);
	}

	private void manageOutProjectActivitiesPourcentRow(int i, int j) {
		String numberDaysForMonthTxt = ((TextBox) (titlesRowArray[0]
				.getElementAt(j))).getText();
		String numberOutProjectActivitiesForMonthTxt = ((TextBox) (titlesRowArray[4]
				.getElementAt(j))).getText();

		double numberDaysForMonth = Double.valueOf(numberDaysForMonthTxt);
		double numberOutProjectActivitiesForMonth = Double
				.valueOf(numberOutProjectActivitiesForMonthTxt);
		double outProjectActivitiesPourcent = 0;

		if (numberDaysForMonth > 0) {
			outProjectActivitiesPourcent = (double) ((numberOutProjectActivitiesForMonth / numberDaysForMonth) * 100);
		}
		calcRowArray[i].setElementAt(j, outProjectActivitiesPourcent);
	}

	private void manageProjectPourcentRow(int i, int j) {
		String numberDaysForMonthTxt = ((TextBox) (titlesRowArray[0]
				.getElementAt(j))).getText();
		String sumProjectTxt = ((TextBox) (calcRowArray[1].getElementAt(j)))
				.getText();

		double numberDaysForMonth = Double.valueOf(numberDaysForMonthTxt);
		double sumProject = Double.valueOf(sumProjectTxt);
		double projectPourcent = 0;

		if (numberDaysForMonth > 0) {
			projectPourcent = (double) ((sumProject / numberDaysForMonth) * 100);
		}
		calcRowArray[i].setElementAt(j, projectPourcent);
	}

	private void manageSumOfPourcentRow(int i, int j) {
		String holidayPourcent = ((TextBox) (calcRowArray[2].getElementAt(j)))
				.getText();
		holidayPourcent = holidayPourcent.substring(0,
				holidayPourcent.length() - 1);

		String trainingPourcent = ((TextBox) (calcRowArray[3].getElementAt(j)))
				.getText();
		trainingPourcent = trainingPourcent.substring(0, trainingPourcent
				.length() - 1);

		String outActivitiesProjectPourcent = ((TextBox) (calcRowArray[4]
				.getElementAt(j))).getText();
		outActivitiesProjectPourcent = outActivitiesProjectPourcent.substring(
				0, outActivitiesProjectPourcent.length() - 1);

		String projectPourcent = ((TextBox) (calcRowArray[5].getElementAt(j)))
				.getText();
		projectPourcent = projectPourcent.substring(0,
				projectPourcent.length() - 1);

		double pourcentForHolidays = Double.valueOf(holidayPourcent);
		double pourcentForTraining = Double.valueOf(trainingPourcent);
		double pourcentForOutActivitiesProject = Double
				.valueOf(outActivitiesProjectPourcent);
		double pourcentForProject = Double.valueOf(projectPourcent);

		double sumOfPourcent = (pourcentForHolidays + pourcentForTraining
				+ pourcentForOutActivitiesProject + pourcentForProject);

		calcRowArray[i].setElementAt(j, sumOfPourcent);
	}

	private void managePKIRow(int i, int j) {
		String sumOfProjectTxt = ((TextBox) (calcRowArray[1].getElementAt(j)))
				.getText();
		double sumOfProject = Double.valueOf(sumOfProjectTxt);
		String numberTrainingForMonthTxt = ((TextBox) (titlesRowArray[3]
				.getElementAt(j))).getText();
		String numberOutProjectActivitiesForMonthTxt = ((TextBox) (titlesRowArray[4]
				.getElementAt(j))).getText();
		String numberProjectDaysAvailabilityTxt = ((TextBox) (calcRowArray[0]
				.getElementAt(j))).getText();

		double numberTrainingForMonth = Double
				.valueOf(numberTrainingForMonthTxt);
		double numberOutProjectActivitiesForMonth = Double
				.valueOf(numberOutProjectActivitiesForMonthTxt);
		double numberProjectDaysAvailability = Double
				.valueOf(numberProjectDaysAvailabilityTxt);

		double PKIPourcent = 0;

		if ((numberTrainingForMonth + numberOutProjectActivitiesForMonth + numberProjectDaysAvailability) > 0) {
			PKIPourcent = (double) ((sumOfProject / (numberTrainingForMonth
					+ numberOutProjectActivitiesForMonth + numberProjectDaysAvailability)) * 100);
		}
		calcRowArray[i].setElementAt(j, PKIPourcent);
	}

	private void refreshCalcRowList() {
		for (int i = 0; i < calcRowArray.length; i++) {
			for (int j = 2; j < calcRowArray[i].length(); j++) {
				switch (i) {
				case 0:
					manageProjectDaysAvailabilityRow(i, j);
					break;
				case 1:
					manageProjectTotalRow(i, j);
					break;
				case 2:
					manageHolidaysPourcentRow(i, j);
					break;
				case 3:
					manageTrainingPourcentRow(i, j);
					break;
				case 4:
					manageOutProjectActivitiesPourcentRow(i, j);
					break;
				case 5:
					manageProjectPourcentRow(i, j);
					break;
				case 6:
					manageSumOfPourcentRow(i, j);
					break;
				case 7:
					managePKIRow(i, j);
					break;
				}
				double sumOfTheCalcRow = calcRowArray[i].sumRow();
				calcRowArray[i].setElementAt(1, sumOfTheCalcRow);
			}
		}
	}

	private void refreshArray() {
		clearArray();
		addRowToArray(RowType.MONTHROW);

		switch (PartagedDataBetweenPanel.viewType) {

		case PERSON_VIEW:
			addRowToArray(RowType.TITLEROW);
			addRowToArray(RowType.CALCROW);
			addRowToArray(RowType.PROJECTROW);
			disabledOrEnabledAllTitleRow();
			break;
		case PROJECT_VIEW:
			addRowToArray(RowType.PROJECTROW);
			addRowToArray(RowType.CALCROW);
			break;
		}
		disabledColumnsOfPreviousMonth();
		if (PartagedDataBetweenPanel.isReadOnly
				|| PartagedDataBetweenPanel.isImporting || PartagedDataBetweenPanel.isSaving) {
			disabledAllTitleRow();
			disabledAllProjectRow();
		}
	}

	private void addRowToArray(RowType rowType) {
		TextBox textbox;

		switch (rowType) {

		case MONTHROW:
			for (int i = 0; i < monthRow.length(); i++) {
				textbox = (TextBox) monthRow.getElementAt(i);
				personArray.setWidget(0, i, textbox);
			}
			break;
		case TITLEROW:
			for (int i = 0; i < titlesRowArray.length; i++) {
				for (int j = 0; j < titlesRowArray[i].length(); j++) {
					textbox = (TextBox) titlesRowArray[i].getElementAt(j);
					personArray.setWidget(i + 1, j, textbox);
				}
			}
			break;
		case CALCROW:
			switch (PartagedDataBetweenPanel.viewType) {
			case PERSON_VIEW:
				for (int j = 0; j < calcRowArray[0].length(); j++) {
					textbox = (TextBox) calcRowArray[0].getElementAt(j);
					personArray.setWidget(6, j, textbox);
				}

				for (int i = 1; i < calcRowArray.length; i++) {
					for (int j = 0; j < calcRowArray[i].length(); j++) {
						textbox = (TextBox) calcRowArray[i].getElementAt(j);
						personArray.setWidget(i + 7, j, textbox);
					}
				}
				break;
			case PROJECT_VIEW:
				for (int i = 0; i < calcRowForProjectView.length(); i++) {
					textbox = (TextBox) calcRowForProjectView.getElementAt(i);
					personArray.setWidget(indexDeparture
							+ projectRowList.size(), i, textbox);
				}
				break;
			}

			break;
		case PROJECTROW:
			personArray.insertRow(indexDeparture);

			for (int i = indexDeparture; i < (indexDeparture + projectRowList
					.size()); i++) {
				personArray.insertRow(indexDeparture);

				ProjectRow projectRowFromList = projectRowList.get(i
						- indexDeparture);

				for (int j = 0; j < projectRowFromList.length(); j++) {
					TextBox elementFromProjectRow = (TextBox) projectRowFromList
							.getElementAt(j);
					personArray.setWidget(indexDeparture, j,
							elementFromProjectRow);
				}
			}
			break;
		}
	}

	private void disabledAllProjectRow() {
		for (int i = 0; i < projectRowList.size(); i++) {
			for (int j = 1; j < projectRowList.get(i).length(); j++) {
				((TextBox) projectRowList.get(i).getElementAt(j))
						.setReadOnly(true);
			}
		}
	}

	private void clearArray() {

		while (personArray.getRowCount() != 0) {
			personArray.removeRow(0);
		}
	}

	@Override
	public void notifyChange(Widget widget) {
		refreshCenterPanel();
	}

	private void notifyListeners() {
		for (EventListener eventListener : eventListenerList) {
			eventListener.notifyChange(this);
		}
	}

	private class MyTitleListener implements ChangeHandler {

		int row;
		int column;
		double value = 0;

		public MyTitleListener(int row, int column, double oldValue) {
			this.row = row;
			this.column = column;
			value = oldValue;
		}

		public void onChange(ChangeEvent changeEvent) {
			TextBox textbox = (TextBox) changeEvent.getSource();
			double valueOfTextBox = Double.valueOf(textbox.getText());

			if (row > 0 && row < 6) {
				if (valueOfTextBox < 0) {
					Window.alert("Valeur negative interdite !");
					textbox.setText("" + value);
				} else {
					value = valueOfTextBox;
					manageTitleRow();
					refreshTitleRowList();
					refreshCalcRowList();
					refreshStyleOfTotalPourcentRow();
				}
			}
		}

		private void manageTitleRow() {
			int monthNumber = column - 1;
			Time time = new Time(monthNumber, OfficialInformation.CURRENT_YEAR);
			switch (row) {

			case 3:
				PartagedDataBetweenPanel.currentPerson.getHoliday(time)
						.setNumber(value);
				break;

			case 4:
				PartagedDataBetweenPanel.currentPerson.getTraining(time)
						.setNumber(value);
				break;

			case 5:
				PartagedDataBetweenPanel.currentPerson.getOther(time)
						.setNumber(value);
				break;
			}
		}
	}

	private class MyProjectListener implements ChangeHandler {
		int rowNumber = 0;
		int columnNumber = 0;
		double oldValue = 0;

		public MyProjectListener(int rowNumber, int columnNumber,
				double oldValue) {
			this.rowNumber = rowNumber;
			this.columnNumber = columnNumber;
			this.oldValue = oldValue;
		}

		public void onChange(ChangeEvent changeEvent) {
			TextBox textBoxOfEvent = (TextBox) (changeEvent.getSource());
			String valueOfTextBox = textBoxOfEvent.getText();

			double value = Double.valueOf(valueOfTextBox);

			if (value < 0) {
				Window.alert("Valeur negative interdite !");
				textBoxOfEvent.setText("" + oldValue);
			} else {
				oldValue = value;
				treatmentOfProject();
			}
		}

		private void treatmentOfProject() {
			Time time = new Time(columnNumber, OfficialInformation.CURRENT_YEAR);
			Record record = new Record(oldValue, time);
			Assignment assignment = null;

			ProjectRow projectRow = projectRowList.get(rowNumber);
			String titleOfRow = projectRow.getTitle();

			switch (PartagedDataBetweenPanel.viewType) {
			case PERSON_VIEW:
				assignment = MiroState.getAssignment(
						PartagedDataBetweenPanel.currentPerson, new Project(
								titleOfRow));
				assignment.setPrestation(columnNumber - 1, record);

				refreshCalcRowList();
				refreshStyleOfTotalPourcentRow();
				break;
			case PROJECT_VIEW:
				int indexSpacing = titleOfRow.indexOf(" ");
				String lastName = titleOfRow.substring(0, indexSpacing);
				String firstName = titleOfRow.substring(indexSpacing + 1);

				assignment = MiroState.getAssignment(new Person(lastName,
						firstName), PartagedDataBetweenPanel.currentProject);
				assignment.setPrestation(columnNumber - 1, record);

				refreshCalcRowForProjectView();
				break;
			}
			double sumOfTheProjectRow = projectRow.sumRow();
			projectRow.setElementAt(1, sumOfTheProjectRow);
		}
	}

	private class switchingViewListener implements ClickHandler {
		String nameOfPersonOrProject;

		public switchingViewListener(String nameOfPersonOrProject) {
			this.nameOfPersonOrProject = nameOfPersonOrProject;
		}

		@Override
		public void onClick(ClickEvent event) {
			PartagedDataBetweenPanel.hasChangedView = true;

			switch (PartagedDataBetweenPanel.viewType) {
			case PERSON_VIEW:
				PartagedDataBetweenPanel.currentPerson = null;
				PartagedDataBetweenPanel.currentProject = MiroState
						.getProject(nameOfPersonOrProject);
				PartagedDataBetweenPanel.viewType = ViewType.PROJECT_VIEW;
				break;
			case PROJECT_VIEW:
				int indexSpacing = nameOfPersonOrProject.indexOf(" ");
				String lastName = nameOfPersonOrProject.substring(0,
						indexSpacing);
				String firstName = nameOfPersonOrProject
						.substring(indexSpacing + 1);

				PartagedDataBetweenPanel.currentProject = null;
				PartagedDataBetweenPanel.currentPerson = MiroState.getPerson(
						lastName, firstName);
				PartagedDataBetweenPanel.viewType = ViewType.PERSON_VIEW;
				break;
			}
			refreshCenterPanel();
			notifyListeners();
		}
	}
}