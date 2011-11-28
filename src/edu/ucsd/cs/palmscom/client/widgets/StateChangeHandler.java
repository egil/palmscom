package edu.ucsd.cs.palmscom.client.widgets;

import com.google.gwt.event.shared.EventHandler;

public interface StateChangeHandler extends EventHandler {
	void onStateChange(StateChangeEvent event);
}
