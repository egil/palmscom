package edu.ucsd.cs.palmscom.client;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.RootPanel;

import edu.ucsd.cs.palmscom.shared.PalmscomServiceAsync;
import edu.ucsd.cs.palmscom.shared.User;

public class Palmscom {

	public Palmscom(User currentUser, int collapsPoint) {
		// TODO Auto-generated method stub
	    PalmscomServiceAsync service = new PollingServiceProxy(); //GWT.create(ContactsService.class);
	    HandlerManager eventBus = new HandlerManager(null);
	    AppController appViewer = new AppController(service, eventBus);
	    appViewer.go(RootPanel.get(), currentUser, collapsPoint);
	}

}
