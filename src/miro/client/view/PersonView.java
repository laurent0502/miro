package miro.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;


public class PersonView {
	interface PersonViewUiBinder extends UiBinder<DockLayoutPanel, PersonView> {
    }
	//
    static PersonViewUiBinder ourUiBinder = GWT.create(PersonViewUiBinder.class);

    //TabLayoutPanel rootElement = ourUiBinder.createAndBindUi(this);
    DockLayoutPanel rootElement = ourUiBinder.createAndBindUi(this);

    @UiField
    TopPanel topPanel;
    @UiField
    CenterPanel centerPanel;
    @UiField
    BottomPanel bottomPanel;

    @UiField
    DockLayoutPanel dockPanel;
    
    public PersonView(){
        RootLayoutPanel root = RootLayoutPanel.get();
        root.add(rootElement);
    }
}
