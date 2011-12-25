package edu.ucsd.cs.palmscom.client;

import java.util.Date;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.ucsd.cs.palmscom.shared.Message;
import edu.ucsd.cs.palmscom.shared.PalmscomServiceAsync;
import edu.ucsd.cs.palmscom.shared.Settings;
import edu.ucsd.cs.palmscom.shared.User;

public class PollingServiceProxy implements PalmscomServiceAsync {

	@Override
	public void getMessages(int limit, AsyncCallback<Message[]> callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getMessages(Date to, AsyncCallback<Message[]> callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getOnlineUsers(AsyncCallback<User[]> callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createMessage(Message msg, AsyncCallback<Void> callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getUserSettings(AsyncCallback<Settings> callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateUserSettings(Settings settings,
			AsyncCallback<Void> callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void signIn(User user, AsyncCallback<Settings> callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void singOut(AsyncCallback<Void> callback) {
		// TODO Auto-generated method stub
		
	}

}
