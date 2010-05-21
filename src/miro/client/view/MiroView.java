package miro.client.view;

import java.util.List;

import miro.client.db.MiroAccessDB;
import miro.shared.Assignment;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

public class MiroView extends Composite implements EventListener {
	PersonView personView;
	AuthentificationBox authentificationBox;

	public MiroView() {
		authentificationBox = new AuthentificationBox();
		
		RootLayoutPanel.get().add(authentificationBox);

		AuthentificationBox.addEventListener(this);
	}

	@Override
	public void notifyChange(Widget widget) {

		final AsyncCallback<List<Assignment>> GET_ASSIGNMENTS_CALLBACK = new AsyncCallback<List<Assignment>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Impossible d'obtenir la liste des assignements!");
			}

			@Override
			public void onSuccess(List<Assignment> result) {
				MiroState.updateViewState(result);

				RootLayoutPanel.get().remove(authentificationBox);
				
				personView = new PersonView();
			}
		};
		MiroAccessDB.getAssignments(GET_ASSIGNMENTS_CALLBACK);
	}
}
