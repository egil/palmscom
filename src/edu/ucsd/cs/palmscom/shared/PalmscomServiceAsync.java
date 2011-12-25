package edu.ucsd.cs.palmscom.shared;

import java.util.Date;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PalmscomServiceAsync {

	void getMessages(int limit, AsyncCallback<Message[]> callback);
	void getMessages(Date to, AsyncCallback<Message[]> callback);
	void getOnlineUsers(AsyncCallback<User[]> callback);
	void createMessage(Message msg, AsyncCallback<Void> callback);
	void getUserSettings(AsyncCallback<Settings> callback);
	void updateUserSettings(Settings settings, AsyncCallback<Void> callback);
	void signIn(User user, AsyncCallback<Settings> callback);
	void singOut(AsyncCallback<Void> callback);
}
