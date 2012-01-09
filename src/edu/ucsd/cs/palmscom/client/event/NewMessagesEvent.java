package edu.ucsd.cs.palmscom.client.event;

import java.util.List;

import com.google.gwt.event.shared.GwtEvent;

import edu.ucsd.cs.palmscom.client.ClientMessageDecorator;

public class NewMessagesEvent extends GwtEvent<NewMessagesEventHandler> {
	public static Type<NewMessagesEventHandler> TYPE = new Type<NewMessagesEventHandler>();
	
	private List<ClientMessageDecorator> messages;
	
	public NewMessagesEvent(List<ClientMessageDecorator> result) {
		this.messages = result;
	}
	
	@Override
	public Type<NewMessagesEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(NewMessagesEventHandler handler) {
		handler.onNewMessages(this);
	}

	public List<ClientMessageDecorator> getMessages() {
		return messages;
	}
}