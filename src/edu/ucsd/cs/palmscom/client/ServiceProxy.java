package edu.ucsd.cs.palmscom.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.ucsd.cs.palmscom.shared.Message;
import edu.ucsd.cs.palmscom.shared.MessageCache;
import edu.ucsd.cs.palmscom.shared.PalmscomService;
import edu.ucsd.cs.palmscom.shared.PalmscomServiceAsync;
import edu.ucsd.cs.palmscom.shared.Settings;
import edu.ucsd.cs.palmscom.shared.User;

public abstract class ServiceProxy {
	protected final PalmscomServiceAsync service = GWT.create(PalmscomService.class);;
	protected final MessageCache<ClientMessageDecorator> cache = new MessageCache<ClientMessageDecorator>();
	protected final HandlerManager eventBus;
	private final ArrayList<User> users = new ArrayList<User>();
	
	protected ServiceProxy(HandlerManager eventBus) {
		this.eventBus = eventBus;
	}
	
	public abstract void listen();
	
	public void getMessages(final int limit, final AsyncCallback<List<ClientMessageDecorator>> callback) {	
		if(limit < cache.size()) {
			callback.onSuccess(cache.getTo(limit));
			return;
		}
			
		// load messages from service if cache.size() < limit
		if(cache.size() < limit) {
			int retriveLimit = limit - cache.size();
			
			// if there is no messages in the list,
			// we have not retrieved any messages, so we do a regular
			// getMessages(limit), otherwise we get older messages
			if(retriveLimit == limit) {
				service.getMessages(retriveLimit, new AsyncCallback<Message[]>() {
					
					@Override
					public void onSuccess(Message[] result) {
						cache.add(Arrays.asList(ClientMessageDecorator.decorateMessages(result)));
						callback.onSuccess(cache.getTo(limit));
					}
					
					@Override
					public void onFailure(Throwable caught) {
						// TODO Display warning/error message, handle condition
						GWT.log("getMessages(final int limit)", caught);
						callback.onFailure(caught);
					}
				});
			} else {
				getMessagesFrom(cache.getLast().getDate(), retriveLimit, new AsyncCallback<List<ClientMessageDecorator>>() {
					
					@Override
					public void onSuccess(List<ClientMessageDecorator> result) {
						// NOTE: result message already added to cache in the 
						// getMessages(final Date from, final int limit, ..) method call
						callback.onSuccess(cache.getTo(limit));
					}
					
					@Override
					public void onFailure(Throwable caught) {
						// NOTE: This should never be triggered, as any error is 
						// handled in the getMessages(final Date from, final int limit, ..)
						// method call.
						callback.onFailure(caught);
					}
				});
			}
		}
	}
	
	public void getMessagesFrom(final Date from, final int limit, final AsyncCallback<List<ClientMessageDecorator>> callback) {
		service.getMessagesFrom(from, limit, new AsyncCallback<Message[]>() {
			
			@Override
			public void onSuccess(Message[] result) {
				cache.add(Arrays.asList(ClientMessageDecorator.decorateMessages(result)));
				callback.onSuccess(cache.getFrom(from, limit));
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Display warning/error message, handle condition
				GWT.log("getMessages(final Date from, final int limit)", caught);
				callback.onFailure(caught);
			}
		});
	}
	
	public void getMessagesTo(final Date to, final AsyncCallback<List<ClientMessageDecorator>> callback) {
		service.getMessagesTo(to, new AsyncCallback<Message[]>() {
			
			@Override
			public void onSuccess(Message[] result) {
				cache.add(Arrays.asList(ClientMessageDecorator.decorateMessages(result)));
				callback.onSuccess(cache.getTo(to));
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Display warning/error message, handle condition
				GWT.log("getMessages(final Date from, final int limit)", caught);
				callback.onFailure(caught);
			}
		});
	}

	public void getOnlineUsers(final AsyncCallback<User[]> callback) {
		service.getOnlineUsers(new AsyncCallback<User[]>() {
		
			@Override
			public void onSuccess(User[] result) {
				users.clear();
				for (User user : result) {
					users.add(user);
				}
				callback.onSuccess(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Display warning/error message, handle condition
				GWT.log("getOnlineUsers()", caught);
				callback.onFailure(caught);
			}
		});
	}

	public void createMessage(Message msg, final AsyncCallback<Void> callback) {
		service.createMessage(msg, new AsyncCallback<Void>() {
			
			@Override
			public void onSuccess(Void result) {
				callback.onSuccess(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Display warning/error message, handle condition
				GWT.log("createMessage(Message msg)", caught);
			}
		});
	}

	public void signIn(User user, final AsyncCallback<Settings> callback) {
		service.signIn(user, new AsyncCallback<Settings>() {
			
			@Override
			public void onSuccess(Settings result) {
				callback.onSuccess(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Display warning/error message, handle condition
				GWT.log("signIn(User user)", caught);
			}
		});
	}

	public void signOut() {
		service.signOut(new AsyncCallback<Void>() {
			
			@Override
			public void onSuccess(Void result) { 
				GWT.log("signOut() success");
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Display warning/error message, handle condition
				GWT.log("signOut()", caught);
			}
		});
	}
}
