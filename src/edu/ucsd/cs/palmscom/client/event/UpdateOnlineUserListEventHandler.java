package edu.ucsd.cs.palmscom.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface UpdateOnlineUserListEventHandler extends EventHandler {
	void onUpdateUserList(UpdateOnlineUserListEvent event);
}
