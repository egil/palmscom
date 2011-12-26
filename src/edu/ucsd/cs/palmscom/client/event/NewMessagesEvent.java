package edu.ucsd.cs.palmscom.client.event;

import com.google.gwt.event.shared.GwtEvent;

import edu.ucsd.cs.palmscom.client.ClientMessageDecorator;
import edu.ucsd.cs.palmscom.shared.Message;

public class NewMessagesEvent extends GwtEvent<NewMessagesEventHandler> {
	public static Type<NewMessagesEventHandler> TYPE = new Type<NewMessagesEventHandler>();
	
	private Message[] messages;
	
	public NewMessagesEvent(Message[] messages) {
		this.messages = messages;
	}
	
	@Override
	public Type<NewMessagesEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(NewMessagesEventHandler handler) {
		handler.onNewMessages(this);
	}

	public Message[] getMessages() {
		return messages;
	}
}