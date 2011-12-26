package edu.ucsd.cs.palmscom.shared;

import java.util.Date;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PalmscomServiceAsync {
	void createMessage(Message msg, AsyncCallback<Void> callback);

	void getMessages(int limit, AsyncCallback<Message[]> callback);
	void getMessagesFrom(Date from, int limit, AsyncCallback<Message[]> asyncCallback);
	void getMessagesTo(Date to, AsyncCallback<Message[]> callback);
	
	void getOnlineUsers(AsyncCallback<User[]> callback);
	
	void signIn(User user, AsyncCallback<Settings> callback);
	void signOut(AsyncCallback<Void> callback);

}
