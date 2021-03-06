package edu.ucsd.cs.palmscom.client.event;

import com.google.gwt.event.shared.GwtEvent;

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

	public User[] getUsers() {
		return users;
	}
}
