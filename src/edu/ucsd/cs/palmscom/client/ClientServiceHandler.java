package edu.ucsd.cs.palmscom.client;

import edu.ucsd.cs.palmscom.shared.PalmscomServiceAsync;

public interface ClientServiceHandler extends PalmscomServiceAsync {
		
	public void addNewDataHandler(NewMessagesHandler newDataHandler);
	public void addOnlineUsersChangeHandler(OnlineUsersChangeHandler onlineUsersChangeHandler);
}
