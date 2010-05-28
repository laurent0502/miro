package miro.client.view;

import java.util.ArrayList;
import java.util.List;

import miro.client.db.MiroAccessDB;
import miro.shared.Assignment;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * This class represents the panel containing the south components of the
 * application.Its contains two buttons : save and lock
 */
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

			PartagedDataBetweenPanel.isLocked = false;
			PartagedDataBetweenPanel.isSaving = false;

			lockButton.setEnabled(true);
			saveButton.setEnabled(false);

			MiroState.updateViewState(result);
			notifyListeners();

			Window.alert("Les donnees ont ete sauvegardes avec succes ! ");
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

	/**
	 * Defines a BottomPanel
	 */
	public BottomPanel() {
		initWidget(uiBinder.createAndBindUi(this));

		/*
		 * Initialize the absolute panel which position children absolutely
		 */
		initAbsolutePanel();

		initWindowCloseListener();

		TopPanel.addEventListener(this);
	}

	/**
	 * Adds the specified event listener
	 * 
	 * @param eventListener
	 *            The event listener
	 */
	public static void addEventListener(EventListener eventListener) {
		listenersList.add(eventListener);
	}

	private void notifyListeners() {
		for (EventListener eventListener : listenersList) {
			eventListener.notifyChange(this);
		}
	}

	@UiHandler("saveButton")
	void onClick(ClickEvent e) {
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

		if (PartagedDataBetweenPanel.isReadOnly) {
			lockButton.setVisible(false);
			saveButton.setVisible(false);
		}
	}

	private void initWindowCloseListener() {
		final AsyncCallback CLOSING_WINDOWS_CALLBACK = new AsyncCallback() {

			@Override
			public void onFailure(Throwable caught) {
				Window
						.alert("Impossible de deverrouiller la base de donnees !");
			}

			@Override
			public void onSuccess(Object result) {
			}
		};

		Window.addCloseHandler(new CloseHandler() {

			@Override
			public void onClose(CloseEvent event) {
				/*
				 * If the lock button is disabled while the application is
				 * closing,it must be enabled
				 */
				if (!lockButton.isEnabled()) {
					MiroAccessDB.setLocked(false, CLOSING_WINDOWS_CALLBACK);
				}
			}
		});
	}

	/**
	 * Implemented method by all listeners
	 * 
	 * @param widget
	 *            The widget sending the event
	 */
	@Override
	public void notifyChange(Widget widget) {

		if (!lockButton.isEnabled() && !PartagedDataBetweenPanel.isReadOnly)
			saveButton.setEnabled(!PartagedDataBetweenPanel.isImporting);
	}
}
