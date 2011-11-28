package edu.ucsd.cs.palmscom.client;

import java.util.List;
import com.google.gwt.event.shared.GwtEvent;
import edu.ucsd.cs.palmscom.shared.User;

public class OnlineUsersChangeEvent extends GwtEvent<OnlineUsersChangeHandler> {
	private static final Type<OnlineUsersChangeHandler> TYPE = new Type<OnlineUsersChangeHandler>();

	private final List<User> users;
	
	public OnlineUsersChangeEvent(List<User> users) {
		this.users = users;
	}	
	
	public List<User> getData() {
		return users;
	}
	
	public static Type<OnlineUsersChangeHandler> getType() {
		return TYPE;
	}	
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<OnlineUsersChangeHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(OnlineUsersChangeHandler handler) {
		handler.onOnlineUsersChange(this);
	}

}
