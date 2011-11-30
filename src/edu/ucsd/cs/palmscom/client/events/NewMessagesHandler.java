package edu.ucsd.cs.palmscom.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface NewMessagesHandler extends EventHandler  {
	void onNewMessages(NewMessagesEvent event);
}
