package edu.ucsd.cs.palmscom.client;

import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.ucsd.cs.palmscom.shared.Message;
import edu.ucsd.cs.palmscom.shared.ServiceException;
import edu.ucsd.cs.palmscom.shared.User;

public class PollingServiceHandler implements ClientServiceHandler {

	@Override
	public void sendMessage(Message msg, AsyncCallback<Void> callback) {
		// TODO Auto-generated method stub
		GWT.log("sendMessage: " + msg.getText());
		callback.onSuccess(null);
	}

	@Override
	public void subscribe(ClientServiceCallback callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getMessages(int limit, AsyncCallback<List<Message>> callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getMessages(Date to, AsyncCallback<List<Message>> callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getMessages(Date from, Date to,
			AsyncCallback<List<Message>> callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void search(String query, AsyncCallback<List<Message>> callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getOnlineUsers(AsyncCallback<List<User>> callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getUserSettings(AsyncCallback<Dictionary> callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveUserSettings(Dictionary settings,
			AsyncCallback<Void> callback) {
		// TODO Auto-generated method stub
		
	}

}
