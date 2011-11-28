package edu.ucsd.cs.palmscom.client;

import com.google.gwt.event.shared.GwtEvent;


public class VisualStateChangeEvent extends GwtEvent<VisualStateChangeHandler> {

	private static final Type<VisualStateChangeHandler> TYPE = new Type<VisualStateChangeHandler>();
	private final VisualStateType stateChangeType;
	
	public VisualStateChangeEvent(VisualStateType stateChangeType) {
		this.stateChangeType = stateChangeType;
	}	
	
	public VisualStateType getState() {
		return this.stateChangeType;
	}	
	
	public static Type<VisualStateChangeHandler> getType() {
		return TYPE;
	}		 
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<VisualStateChangeHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(VisualStateChangeHandler handler) {
		handler.onStateChange(this);
	}

}
