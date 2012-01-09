package edu.ucsd.cs.palmscom.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class ToggleButtonClickedEvent extends GwtEvent<ToggleButtonClickedEventHandler> {
	public static Type<ToggleButtonClickedEventHandler> TYPE = new Type<ToggleButtonClickedEventHandler>();

	@Override
	public Type<ToggleButtonClickedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ToggleButtonClickedEventHandler handler) {
		handler.onToggleButtonClicked(this);
	}

}
