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

public class BottomPanel extends Composite {

	//
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

	private final AsyncCallback<List<Assignment>> CALLBACK2 = new AsyncCallback<List<Assignment>>() {

		@Override
		public void onFailure(Throwable caught) {
		}

		@Override
		public void onSuccess(List<Assignment> result) {

			lockButton.setEnabled(true);
			saveButton.setEnabled(false);
			
			MiroState.updateViewState(result);
			Window.alert("message");
			BottomPanel.this.notifyListeners();
		}
	};
	
	private final AsyncCallback CALLBACK = new AsyncCallback() {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert(caught.getMessage());
		}

		@Override
		public void onSuccess(Object result) {
			MiroAccessDB.getAssignments(CALLBACK2);
		}
	};

	public BottomPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		initAbsolutePanel();
		initWindowCloseListener();
	}

	public static void addEventListener(EventListener eventListener){
		listenersList.add(eventListener);
	}
	
	private void notifyListeners(){
		for(EventListener eventListener : listenersList){
			Window.alert("passe dedans");
			eventListener.notifyChange(this);
		}
	}
	
	@UiHandler("saveButton")
	void onClick(ClickEvent e) {
		
		MiroAccessDB.updateAssignments(MiroState.getPersonList(),
				MiroState.getAssignmentList(), CALLBACK);
	}

	@UiHandler("lockButton")
	void onClick1(ClickEvent e) {

		AsyncCallback callback = new AsyncCallback() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
			}

			@Override
			public void onSuccess(Object result) {
				lockButton.setEnabled(false);
				saveButton.setEnabled(true);
				BottomPanel.this.notifyListeners();
				//BottomPanel.this.fireEvent(new LockedEvent());
			}
		};
		MiroAccessDB.setLocked(true, callback);
	}

	private void initAbsolutePanel() {
		absolutePanel.setSize("900px", "50px");
		absolutePanel.setWidgetPosition(lockButton, 25, 0);
		absolutePanel.setWidgetPosition(saveButton, 90, 0);
	}

	private void initWindowCloseListener() {
		final AsyncCallback callback = new AsyncCallback() {

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
				 * Si lorsqu'on ferme l'application,le lock est actif,il faut le
				 * rendre inactif
				 */
				if (!lockButton.isEnabled()) {
					MiroAccessDB.setLocked(false, callback);
				}
			}
		});
	}
}
