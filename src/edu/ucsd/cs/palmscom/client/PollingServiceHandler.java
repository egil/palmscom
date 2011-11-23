package edu.ucsd.cs.palmscom.client;

import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.ucsd.cs.palmscom.server.PollingServiceImpl;
import edu.ucsd.cs.palmscom.shared.Message;
import edu.ucsd.cs.palmscom.shared.PalmscomService;
import edu.ucsd.cs.palmscom.shared.PalmscomServiceAsync;
import edu.ucsd.cs.palmscom.shared.User;

public class PollingServiceHandler implements ClientServiceHandler {
	private PalmscomServiceAsync svc = GWT.create(PalmscomService.class);
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

	@Override
	public void subscribe(final ClientServiceCallback callback) {		
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
							callback.onLiveMessages(result);
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

//	@Override
//	public void getUserSettings(final AsyncCallback<Dictionary> callback) {
//		svc.getUserSettings(new AsyncCallback<Dictionary>() {
//			
//			@Override
//			public void onSuccess(Dictionary result) {
//				GWT.log("SUCCESS: getUserSettings()");
//				callback.onSuccess(result);					
//			}
//			
//			@Override
//			public void onFailure(Throwable caught) {
//				GWT.log("Failure: getUserSettings()");
//				callback.onFailure(caught);	
//			}			
//		});		
//	}
//
//	@Override
//	public void saveUserSettings(Dictionary settings,
//			AsyncCallback<Void> callback) {
//		// TODO Auto-generated method stub
//		
//	}

}
