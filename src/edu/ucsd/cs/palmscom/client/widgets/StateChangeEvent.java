package edu.ucsd.cs.palmscom.client.widgets;

import com.google.gwt.event.shared.GwtEvent;

public class StateChangeEvent  extends GwtEvent<StateChangeHandler> {

	private static final Type<StateChangeHandler> TYPE = new Type<StateChangeHandler>();
	private final StateChangeType stateChangeType;
	
	public StateChangeEvent(StateChangeType stateChangeType) {
		this.stateChangeType = stateChangeType;
	}
	
	public StateChangeType getState() {
		return this.stateChangeType;
	}
	
	public static Type<StateChangeHandler> getType() {
		return TYPE;
	}
		 
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<StateChangeHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(StateChangeHandler handler) {
		handler.onStateChange(this);
	}

}
