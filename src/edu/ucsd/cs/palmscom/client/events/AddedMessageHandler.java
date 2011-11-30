package edu.ucsd.cs.palmscom.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface AddedMessageHandler extends EventHandler {
	void onAddedMessage(AddedMessageEvent event);
}

