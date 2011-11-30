package edu.ucsd.cs.palmscom.client;

import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.ucsd.cs.palmscom.client.events.NewMessagesEvent;
import edu.ucsd.cs.palmscom.client.events.NewMessagesHandler;
import edu.ucsd.cs.palmscom.client.events.OnlineUsersChangeEvent;
import edu.ucsd.cs.palmscom.client.events.OnlineUsersChangeHandler;
import edu.ucsd.cs.palmscom.server.PollingServiceImpl;
import edu.ucsd.cs.palmscom.shared.Message;
import edu.ucsd.cs.palmscom.shared.PalmscomService;
import edu.ucsd.cs.palmscom.shared.PalmscomServiceAsync;
import edu.ucsd.cs.palmscom.shared.Settings;
import edu.ucsd.cs.palmscom.shared.User;

public class PollingServiceProxyImpl implements ClientServiceProxy {
	private final HandlerManager handlerManager = new HandlerManager(this);
	private final PalmscomServiceAsync svc = GWT.create(PalmscomService.class);
	private Timer refreshMsgs;	
	private Date lastRefresh;
	
	@Override
	public void sendMessage(Message msg, final AsyncCallback<Void> callback) {
		
		svc.sendMessage(msg, new AsyncCallback<Void>() {			
			@Override
			public void onSuccess(Void result) {
				GWT.log("SUCCESS: sendMessage");
				callback.onSuccess(null);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Failure: sendMessage");
				callback.onFailure(caught);
			}
		});		
	}

	public void enableAutoPolling() {
		// already running
		if(refreshMsgs != null)
			return;
		
		lastRefresh = new Date();
		refreshMsgs = new Timer() {			
			@Override
			public void run() {
				svc.getMessages(lastRefresh, new AsyncCallback<List<Message>>() {
					
					@Override
					public void onSuccess(List<Message> result) {
						//GWT.log("SUCCESS: subscribe -> getMessages(" + lastRefresh.toString() + ")");
						if(result.size() > 0) { 
							GWT.log("SUCCESS: subscribe -> new messages(" + result.size() + ")");
							// set lastRefresh to newest message
							lastRefresh = result.get(result.size() - 1).getDate();

							// tell subscriber about new messages
							handlerManager.fireEvent(new NewMessagesEvent(result));
						}
						
						// look updates in 1 sec
						refreshMsgs.schedule(1000);
					}
					
					@Override
					public void onFailure(Throwable caught) {
						GWT.log("Failure: subscribe -> getMessages(" + lastRefresh.toString() + "). " + caught.getMessage());

						// look updates in 1 sec
						refreshMsgs.schedule(1000);
					}					
				});
			}
		};
				
		// look updates in 1 sec
		refreshMsgs.schedule(1000);
	}

	@Override
	public void getMessages(int limit, final AsyncCallback<List<Message>> callback) {
		svc.getMessages(limit, new AsyncCallback<List<Message>>() {
				
			@Override
			public void onSuccess(List<Message> result) {
				GWT.log("SUCCESS: getMessages(limit)");
				callback.onSuccess(result);					
			}
			
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Failure: getMessages(limit)");
				callback.onFailure(caught);	
			}
			
		});
	}

	@Override
	public void getMessages(Date to, final AsyncCallback<List<Message>> callback) {
		svc.getMessages(to, new AsyncCallback<List<Message>>() {
			
			@Override
			public void onSuccess(List<Message> result) {
				GWT.log("SUCCESS: getMessages(to)");
				callback.onSuccess(result);					
			}
			
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Failure: getMessages(to)");
				callback.onFailure(caught);	
			}
			
		});
		
	}

	@Override
	public void getMessages(Date from, Date to, AsyncCallback<List<Message>> callback) {
		// TODO Auto-generated method stub	
	}

	@Override
	public void search(String query, AsyncCallback<List<Message>> callback) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void getOnlineUsers(final AsyncCallback<List<User>> callback) {
		svc.getOnlineUsers(new AsyncCallback<List<User>>() {
			
			@Override
			public void onSuccess(List<User> result) {
				GWT.log("SUCCESS: getOnlineUsers()");
				callback.onSuccess(result);					
			}
			
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Failure: getOnlineUsers()");
				callback.onFailure(caught);	
			}			
		});		
	}

	@Override
	public void addNewDataHandler(NewMessagesHandler handler) {
		handlerManager.addHandler(NewMessagesEvent.getType(), handler);
		enableAutoPolling();
	}

	@Override
	public void addOnlineUsersChangeHandler(OnlineUsersChangeHandler handler) {
		handlerManager.addHandler(OnlineUsersChangeEvent.getType(), handler);		
	}

	@Override
	public void getUserSettings(final AsyncCallback<Settings> callback) {
		svc.getUserSettings(new AsyncCallback<Settings>() {
			
			@Override
			public void onSuccess(Settings settings) {
				GWT.log("SUCCESS: getUserSettings()");
				callback.onSuccess(settings);					
			}
			
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Failure: getUserSettings()");
				callback.onFailure(caught);	
			}			
		});		
	}

	@Override
	public void saveUserSettings(Settings settings, final AsyncCallback<Void> callback) {
		svc.saveUserSettings(settings, new AsyncCallback<Void>() {
			
			@Override
			public void onSuccess(Void result) {
				GWT.log("SUCCESS: saveUserSettings()");
				callback.onSuccess(result);	
			}
			
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Failure: saveUserSettings()");
				callback.onFailure(caught);					
			}		
		});	
	}

}
