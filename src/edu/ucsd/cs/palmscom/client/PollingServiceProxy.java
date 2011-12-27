package edu.ucsd.cs.palmscom.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.ucsd.cs.palmscom.client.event.NewMessagesEvent;
import edu.ucsd.cs.palmscom.client.event.UpdateOnlineUserListEvent;
import edu.ucsd.cs.palmscom.shared.Message;
import edu.ucsd.cs.palmscom.shared.User;

public class PollingServiceProxy extends ServiceProxy {

	private Timer refreshMsgs;
	private Timer refreshOnlineUsers;
	
	protected PollingServiceProxy(HandlerManager eventBus) {
		super(eventBus);
	}

	@Override
	public void listen() {
		enableAutoPollingMessages();
		enableAutoPollingUsers();
	}
	
	private void enableAutoPollingMessages() {
		final int pollingInterval = 1000;
		// already running
		if(refreshMsgs != null)	return;
		
		// create callback class
		final AsyncCallback<Message[]> callback = new AsyncCallback<Message[]>() {
			
			@Override
			public void onSuccess(Message[] result) {	
				eventBus.fireEvent(new NewMessagesEvent(result));
				// look updates in 1 sec
				refreshMsgs.schedule(pollingInterval);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("enableAutoPollingMessages(to)", caught);
				// look updates in 1 sec
				refreshMsgs.schedule(pollingInterval);
			}					
		};
		
		refreshMsgs = new Timer() {			
			@Override
			public void run() {
				if(cache.size() > 0) {
					getMessagesTo(cache.getFirst().getDate(), callback);
				} else {
					getMessages(10, callback);
				}
			}
		};
				
		// look updates in 1 sec
		refreshMsgs.schedule(pollingInterval);
	}

	private void enableAutoPollingUsers() {
		final int pollingInterval = 10000;
		
		// already running
		if(refreshOnlineUsers != null)	return;
		
		// create callback class
		final AsyncCallback<User[]> callback = new AsyncCallback<User[]>() {
			
			@Override
			public void onSuccess(User[] result) {	
				eventBus.fireEvent(new UpdateOnlineUserListEvent(result));
				// look updates in 1 sec
				refreshOnlineUsers.schedule(pollingInterval);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("enableAutoPollingUsers(to)", caught);
				// look updates in 1 sec
				refreshOnlineUsers.schedule(pollingInterval);
			}					
		};
		
		refreshOnlineUsers = new Timer() {			
			@Override
			public void run() {
				getOnlineUsers(callback);
			}
		};
				
		// look updates in 1 sec
		refreshOnlineUsers.schedule(pollingInterval);
	}
	
}
