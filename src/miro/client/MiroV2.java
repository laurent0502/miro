package miro.client;

import java.util.List;

import miro.client.db.MiroAccessDB;
import miro.client.view.MiroState;
import miro.client.view.PersonView;
import miro.shared.*;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
//import com.google.appengine.api.datastore.dev
/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class MiroV2 implements EntryPoint {

	private PersonView personView;

	private final AsyncCallback<List<Assignment>> CALLBACK = new AsyncCallback<List<Assignment>>() {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert(caught.getMessage());
		}

		@Override
		public void onSuccess(List<Assignment> result) {
			MiroState.updateViewState(result);
			personView = new PersonView();
		}
	};
	
	public void onModuleLoad() {	
		MiroAccessDB.getAssignments(CALLBACK);
	}
}
