package edu.ucsd.cs.palmscom.client;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.RootPanel;

import edu.ucsd.cs.palmscom.shared.PalmscomServiceAsync;
import edu.ucsd.cs.palmscom.shared.User;

public class AppController {
	private final User currentUser;
	private final PalmscomServiceAsync svc;
	private final HandlerManager bus;
	

	public AppController(PalmscomServiceAsync service, HandlerManager eventBus, User currentUser, int collapsPoint) {
		this.currentUser = currentUser;
		this.svc = service;
		this.bus = eventBus;
		// TODO Auto-generated constructor stub
	}

	private void bind() {
		
	}
	
	public void go(RootPanel rootPanel) {
		// TODO Auto-generated method stub
		
	}

}
