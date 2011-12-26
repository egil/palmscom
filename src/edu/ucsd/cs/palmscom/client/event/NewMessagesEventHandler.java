package edu.ucsd.cs.palmscom.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface NewMessagesEventHandler extends EventHandler {
	void onNewMessages(NewMessagesEvent event);
}
