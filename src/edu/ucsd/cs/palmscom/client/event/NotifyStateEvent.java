package edu.ucsd.cs.palmscom.client.event;

import com.google.gwt.event.shared.GwtEvent;

import edu.ucsd.cs.palmscom.client.NotifyStateType;

public class NotifyStateEvent extends GwtEvent<NotifyStateHandler> {
	public static Type<NotifyStateHandler> TYPE = new Type<NotifyStateHandler>();
	
	private final NotifyStateType state;

	public NotifyStateEvent(NotifyStateType state) {
		this.state = state;
	}	

	public NotifyStateType getState() {
		return state;
	} 

	@Override
	public Type<NotifyStateHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(NotifyStateHandler handler) {
		handler.onStateChange(this);
	}

}