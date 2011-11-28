package edu.ucsd.cs.palmscom.client;

import com.google.gwt.event.shared.EventHandler;

public interface NewMessagesHandler extends EventHandler  {
	void onNewMessages(NewMessagesEvent event);
}
