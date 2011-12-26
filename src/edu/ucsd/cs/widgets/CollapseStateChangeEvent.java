package edu.ucsd.cs.widgets;

import com.google.gwt.event.shared.GwtEvent;

import edu.ucsd.cs.widgets.CollapsiblePanel.CollapseState;

public class CollapseStateChangeEvent extends GwtEvent<CollapseStateChangeEventHandler> {
	
	public static final Type<CollapseStateChangeEventHandler> TYPE = new Type<CollapseStateChangeEventHandler>();

	private CollapseState state;
	
	public CollapseStateChangeEvent(CollapseState state) {
		this.state = state;
	}
	
	@Override
	public Type<CollapseStateChangeEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(CollapseStateChangeEventHandler handler) {
	    handler.onChange(this);
	}

	public CollapseState getState() {
		return state;
	}

}