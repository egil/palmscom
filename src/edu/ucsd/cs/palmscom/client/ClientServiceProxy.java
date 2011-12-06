package edu.ucsd.cs.palmscom.client;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HandlerRegistration;

import edu.ucsd.cs.palmscom.client.events.NewMessagesHandler;
import edu.ucsd.cs.palmscom.client.events.OnlineUsersChangeHandler;
import edu.ucsd.cs.palmscom.shared.PalmscomServiceAsync;

public interface ClientServiceProxy extends PalmscomServiceAsync {
		
	public void addNewDataHandler(NewMessagesHandler newDataHandler);
	public void addOnlineUsersChangeHandler(OnlineUsersChangeHandler onlineUsersChangeHandler);	
}
