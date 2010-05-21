package miro.client.view;

import java.util.ArrayList;
import java.util.List;

import miro.client.db.MiroAccessDB;
import miro.shared.Assignment;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.google.gwt.user.client.Window.ClosingHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class BottomPanel extends Composite implements EventListener {

	private static BottomPanelUiBinder uiBinder = GWT
			.create(BottomPanelUiBinder.class);

	interface BottomPanelUiBinder extends UiBinder<AbsolutePanel, BottomPanel> {
	}

	@UiField
	AbsolutePanel absolutePanel;

	@UiField
	Button saveButton;

	@UiField
	Button lockButton;

	private static List<EventListener> listenersList = new ArrayList<EventListener>();

	private final AsyncCallback<List<Assignment>> GET_ASSIGNMENT_CALLBACK = new AsyncCallback<List<Assignment>>() {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Impossible d'obtenir les informations !");
			
			PartagedDataBetweenPanel.isLocked = true;
			PartagedDataBetweenPanel.isSaving = false;
		}

		@Override
		public void onSuccess(List<Assignment> result) {
			Window.alert("Les donnees ont ete sauvegardes avec succes ! ");
			
			PartagedDataBetweenPanel.isLocked = false;
			PartagedDataBetweenPanel.isSaving = false;
			
			lockButton.setEnabled(true);
			saveButton.setEnabled(false);

			MiroState.updateViewState(result);
			notifyListeners();
		}
	};

	private final AsyncCallback UPDATE_ASSIGNMENT_CALLBACK = new AsyncCallback() {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Impossible de sauver les informations !");
		}

		@Override
		public void onSuccess(Object result) {
			MiroAccessDB.getAssignments(GET_ASSIGNMENT_CALLBACK);
		}
	};

	public BottomPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		initAbsolutePanel();
		initWindowCloseListener();

		TopPanel.addEventListener(this);
	}

	public static void addEventListener(EventListener eventListener) {
		listenersList.add(eventListener);
	}

	private void ajustButtons() {
		if (PartagedDataBetweenPanel.isReadOnly) {
			lockButton.setVisible(false);
			saveButton.setVisible(false);
		}
	}

	private void notifyListeners() {
		for (EventListener eventListener : listenersList) {
			eventListener.notifyChange(this);
		}
	}

	@UiHandler("saveButton")
	void onClick(ClickEvent e) {
		//PartagedDataBetweenPanel.isLocked = false;
		PartagedDataBetweenPanel.isSaving = true;
		notifyListeners();
		MiroAccessDB.updateAssignments(MiroState.getPersonList(), MiroState
				.getAssignmentList(), UPDATE_ASSIGNMENT_CALLBACK);
	}

	@UiHandler("lockButton")
	void onClick1(ClickEvent e) {

		final AsyncCallback SET_LOCKED_CALLBACK = new AsyncCallback() {

			@Override
			public void onFailure(Throwable caught) {
				PartagedDataBetweenPanel.isLocked = false;
				Window.alert(caught.getMessage());
			}

			@Override
			public void onSuccess(Object result) {
				PartagedDataBetweenPanel.isLocked = true;
				lockButton.setEnabled(false);
				saveButton.setEnabled(true);
				notifyListeners();
			}
		};
		MiroAccessDB.setLocked(true, SET_LOCKED_CALLBACK);
	}

	private void initAbsolutePanel() {
		absolutePanel.setSize("900px", "50px");
		absolutePanel.setWidgetPosition(lockButton, 25, 0);
		absolutePanel.setWidgetPosition(saveButton, 90, 0);

		ajustButtons();
	}

	private void initWindowCloseListener() {
		final AsyncCallback CLOSING_WINDOWS_CALLBACK = new AsyncCallback() {

			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(Object result) {
			}
		};

		Window.addWindowClosingHandler(new ClosingHandler() {

			@Override
			public void onWindowClosing(ClosingEvent event) {
				/*
				 * Si le lock est actif lorsqu'on ferme l'application,il faut le
				 * rendre inactif
				 */
				if (!lockButton.isEnabled()) {
					MiroAccessDB.setLocked(false, CLOSING_WINDOWS_CALLBACK);
				}
			}
		});
	}

	@Override
	public void notifyChange(Widget widget) {

		if (!lockButton.isEnabled() && !PartagedDataBetweenPanel.isReadOnly)
			saveButton.setEnabled(!PartagedDataBetweenPanel.isImporting);
	}
}
