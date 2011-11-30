package edu.ucsd.cs.palmscom.client.events;

import com.google.gwt.event.shared.GwtEvent;

import edu.ucsd.cs.palmscom.shared.Message;

public class AddedMessageEvent extends GwtEvent<AddedMessageHandler> {
	private static final Type<AddedMessageHandler> TYPE = new Type<AddedMessageHandler>();

	private final Message message;
	
	public AddedMessageEvent(Message message) {
		this.message = message;
	}	
	
	public Message getMessage() {
		return message;
	}
	
	public static Type<AddedMessageHandler> getType() {
		return TYPE;
	}	
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<AddedMessageHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(AddedMessageHandler handler) {
		handler.onAddedMessage(this);
	}
}
