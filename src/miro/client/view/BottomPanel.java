package miro.client.view;

import java.util.List;

import miro.client.db.MiroAccessDB;
import miro.shared.Assignment;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.HasHandlers;
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


public class BottomPanel extends Composite implements EventListener,HasHandlers{


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

	private TopPanel topPanel;
	private CenterPanel centerPanel;

	private final AsyncCallback<List<Assignment>> CALLBACK2 = new AsyncCallback<List<Assignment>>() {

		@Override
		public void onFailure(Throwable caught) {
		}

		@Override
		public void onSuccess(List<Assignment> result) {
			MiroState.updateViewState(result);
			topPanel.refreshPersonOrProjectAffectChoiceList();
			centerPanel.refreshCenterPanel();

			MiroAccessDB.setLocked(false, CALLBACK3);
		}
	};
	
	private final AsyncCallback CALLBACK3 = new AsyncCallback() {

		@Override
		public void onFailure(Throwable caught) {
		}

		@Override
		public void onSuccess(Object result) {
			lockButton.setEnabled(true);
			saveButton.setEnabled(false);
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
		TopPanel.addEventListener(this);
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
			}
		};

		MiroAccessDB.setLocked(true, callback);
	}

	private void initAbsolutePanel() {
		absolutePanel.setSize("900px", "50px");
		absolutePanel.setWidgetPosition(lockButton, 25, 0);
		absolutePanel.setWidgetPosition(saveButton, 90, 0);
	}

	void setCenterPanel(CenterPanel centerPanel) {
		this.centerPanel = centerPanel;
	}

	void setTopPanel(TopPanel topPanel) {
		this.topPanel = topPanel;
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

	@Override
	public void notifyChange() {
		if(PartagedDataBetweenPanel.isImporting){
			lockButton.setEnabled(false);
			saveButton.setEnabled(false);
		}
		else{
			lockButton.setEnabled(true);
		}
	}

}
