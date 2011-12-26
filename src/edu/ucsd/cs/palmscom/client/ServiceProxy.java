package edu.ucsd.cs.palmscom.client;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.ucsd.cs.palmscom.shared.Message;
import edu.ucsd.cs.palmscom.shared.MessageCache;
import edu.ucsd.cs.palmscom.shared.PalmscomService;
import edu.ucsd.cs.palmscom.shared.PalmscomServiceAsync;
import edu.ucsd.cs.palmscom.shared.Settings;
import edu.ucsd.cs.palmscom.shared.User;

public abstract class ServiceProxy implements PalmscomServiceAsync {
	protected final PalmscomServiceAsync service = GWT.create(PalmscomService.class);;
	protected final MessageCache<ClientMessageDecorator> cache = new MessageCache<ClientMessageDecorator>();
	protected final HandlerManager eventBus;
	private final ArrayList<User> users = new ArrayList<User>();
	
	protected ServiceProxy(HandlerManager eventBus) {
		this.eventBus = eventBus;
	}
	
	@Override
	public void getMessages(final int limit, final AsyncCallback<Message[]> callback) {	
		if(limit < cache.size()) {
			callback.onSuccess(cache.getTo(limit));
			return;
		}
			
		// load messages from service if cache.size() < limit
		if(cache.size() < limit) {
			int retriveLimit = limit - cache.size();
			
			// if there is no messages in the list,
			// we have not retrived any messages, so we do a regular
			// getMessages(limit), otherwise we get older messages
			if(retriveLimit == limit) {
				service.getMessages(retriveLimit, new AsyncCallback<Message[]>() {
					
					@Override
					public void onSuccess(Message[] result) {
						cache.add(ClientMessageDecorator.decorateMessages(result));
						callback.onSuccess(cache.getTo(limit));
					}
					
					@Override
					public void onFailure(Throwable caught) {
						// TODO Display warning/error message, handle condition
						GWT.log("getMessages(final int limit)", caught);
					}
				});
			} else {
				getMessages(cache.getLast().getDate(), retriveLimit, new AsyncCallback<Message[]>() {
					
					@Override
					public void onSuccess(Message[] result) {
						// NOTE: result message already added to cache in the 
						// getMessages(final Date from, final int limit, ..) method call
						callback.onSuccess(cache.getTo(limit));
					}
					
					@Override
					public void onFailure(Throwable caught) {
						// NOTE: This should never be triggered, as any error is 
						// handled in the getMessages(final Date from, final int limit, ..)
						// method call.
					}
				});
			}
		}
	}
	
	@Override
	public void getMessages(final Date from, final int limit, final AsyncCallback<Message[]> callback) {
		service.getMessages(from, limit, new AsyncCallback<Message[]>() {
			
			@Override
			public void onSuccess(Message[] result) {
				cache.add(ClientMessageDecorator.decorateMessages(result));
				callback.onSuccess(cache.getFrom(from, limit));
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Display warning/error message, handle condition
				GWT.log("getMessages(final Date from, final int limit)", caught);
			}
		});
	}

	@Override
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
				// TODO Auto-generated method stub
				// TODO Display warning/error message, handle condition
				GWT.log("getOnlineUsers()", caught);
			}
		});
	}

	@Override
	public void createMessage(Message msg, AsyncCallback<Void> callback) {
		service.createMessage(msg, callback);
	}

	@Override
	public void signIn(User user, AsyncCallback<Settings> callback) {
		service.signIn(user, callback);
	}

	@Override
	public void signOut(AsyncCallback<Void> callback) {
		service.signOut(callback);
	}
}
