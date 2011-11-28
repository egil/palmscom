package edu.ucsd.cs.palmscom.client;

import java.util.List;
import com.google.gwt.event.shared.GwtEvent;

import edu.ucsd.cs.palmscom.shared.Message;

public class NewMessagesEvent extends GwtEvent<NewMessagesHandler> {
	private static final Type<NewMessagesHandler> TYPE = new Type<NewMessagesHandler>();

	private final List<Message> messages;
	
	public NewMessagesEvent(List<Message> messages) {
		this.messages = messages;
	}	
	
	public List<Message> getData() {
		return messages;
	}
	
	public static Type<NewMessagesHandler> getType() {
		return TYPE;
	}	
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<NewMessagesHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(NewMessagesHandler handler) {
		handler.onNewMessages(this);
	}

}
