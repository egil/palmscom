package edu.ucsd.cs.palmscom.client.events;

import com.google.gwt.event.shared.GwtEvent;

import edu.ucsd.cs.palmscom.shared.Message;

public class ReplyButtonClickEvent extends GwtEvent<ReplyButtonClickHandler> {
	private static final Type<ReplyButtonClickHandler> TYPE = new Type<ReplyButtonClickHandler>();

	private final String nickname;
	
	public ReplyButtonClickEvent(String nickname) {
		this.nickname = nickname;
	}	
	
	public String getNickname() {
		return nickname;
	}
	
	public static Type<ReplyButtonClickHandler> getType() {
		return TYPE;
	}	
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<ReplyButtonClickHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ReplyButtonClickHandler handler) {
		handler.onReplyButtonClick(this);
	}
}
