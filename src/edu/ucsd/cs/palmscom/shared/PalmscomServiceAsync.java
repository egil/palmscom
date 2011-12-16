package edu.ucsd.cs.palmscom.shared;

import java.util.Date;
import java.util.List;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PalmscomServiceAsync {

	void getMessages(int limit, AsyncCallback<List<Message>> callback);
	void getMessages(Date to, AsyncCallback<List<Message>> callback);
	void getMessages(Date from, Date to, AsyncCallback<List<Message>> callback);
	void getOnlineUsers(AsyncCallback<List<User>> callback);
	void search(String query, AsyncCallback<List<Message>> callback);
	void sendMessage(Message msg, AsyncCallback<Void> callback);
	void getUserSettings(AsyncCallback<Settings> callback);
	void saveUserSettings(Settings settings, AsyncCallback<Void> callback);
}
