package miro.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;

/**
 * This class represents the person view of the application with its different
 * components
 */
public class PersonView extends Composite {

	interface PersonViewUiBinder extends UiBinder<DockLayoutPanel, PersonView> {
	}

	static PersonViewUiBinder ourUiBinder = GWT
			.create(PersonViewUiBinder.class);

	DockLayoutPanel rootElement = ourUiBinder.createAndBindUi(this);

	/**
	 * Defines a PersonView
	 */
	public PersonView() {
		RootLayoutPanel.get().add(rootElement);
	}
}
