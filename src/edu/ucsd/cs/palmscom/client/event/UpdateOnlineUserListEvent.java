package edu.ucsd.cs.palmscom.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;

import edu.ucsd.cs.palmscom.shared.Message;
import edu.ucsd.cs.palmscom.shared.User;

public class UpdateOnlineUserListEvent  extends GwtEvent<UpdateOnlineUserListEventHandler> {
	public static Type<UpdateOnlineUserListEventHandler> TYPE = new Type<UpdateOnlineUserListEventHandler>();
	
	private User[] users;
	
	public UpdateOnlineUserListEvent(User[] users) {
		this.users = users;
	}
	
	@Override
	public Type<UpdateOnlineUserListEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(UpdateOnlineUserListEventHandler handler) {
		handler.onUpdateUserList(this);
	}

	public User[] getMessages() {
		return users;
	}
}
