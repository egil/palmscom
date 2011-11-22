package edu.ucsd.cs.palmscom.client;

import java.util.Date;
import java.util.Dictionary;
import java.util.List;

import com.google.gwt.core.client.GWT;

import edu.ucsd.cs.palmscom.shared.Message;
import edu.ucsd.cs.palmscom.shared.ServiceException;
import edu.ucsd.cs.palmscom.shared.User;

public class PollingServiceHandler implements ClientServiceHandler {

	@Override
	public void sendMessage(Message msg) throws ServiceException {
		// TODO Auto-generated method stub
		GWT.log("sendMessage: " + msg.getText());
	}

	@Override
	public void subscribe(ClientServiceCallback callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Message> getMessages(int limit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Message> getMessages(Date to) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Message> getMessages(Date from, Date to) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Message> search(String query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> getOnlineUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Dictionary<String, String> getUserSettings() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveUserSettings(Dictionary<String, String> settings)
			throws ServiceException {
		// TODO Auto-generated method stub
		
	}

}
