package edu.ucsd.cs.palmscom.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;

import edu.ucsd.cs.palmscom.widgets.CollapsiblePanel.CollapseState;

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
