package edu.ucsd.cs.palmscom.client;

import com.google.gwt.event.shared.EventHandler;

public interface NotifyStateHandler extends EventHandler {
	void onStateChange(NotifyStateEvent event);
}
