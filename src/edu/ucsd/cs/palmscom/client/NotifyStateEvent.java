package edu.ucsd.cs.palmscom.client;

import com.google.gwt.event.shared.GwtEvent;

public class NotifyStateEvent extends GwtEvent<NotifyStateHandler> {

	private static final Type<NotifyStateHandler> TYPE = new Type<NotifyStateHandler>();
	private final NotifyStateType state;
	
	public NotifyStateEvent(NotifyStateType state) {
		this.state = state;
	}	
	
	public NotifyStateType getState() {
		return state;
	}
	
	public static Type<NotifyStateHandler> getType() {
		return TYPE;
	}		 
	
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<NotifyStateHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(NotifyStateHandler handler) {
		handler.onStateChange(this);
	}

}
