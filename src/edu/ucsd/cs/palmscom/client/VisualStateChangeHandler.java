package edu.ucsd.cs.palmscom.client;

import com.google.gwt.event.shared.EventHandler;


public interface VisualStateChangeHandler extends EventHandler {
	void onStateChange(VisualStateChangeEvent event);
}
