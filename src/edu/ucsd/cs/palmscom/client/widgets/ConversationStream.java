package edu.ucsd.cs.palmscom.client.widgets;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;

import edu.ucsd.cs.palmscom.client.ClientServiceProxy;
import edu.ucsd.cs.palmscom.client.events.NewMessagesEvent;
import edu.ucsd.cs.palmscom.client.events.NewMessagesHandler;
import edu.ucsd.cs.palmscom.shared.Message;
import edu.ucsd.cs.palmscom.shared.Settings;

public abstract class ConversationStream extends Composite {	
	private final ClientServiceProxy svc;	
	private final int initialLoad;
	private boolean hasMessages = false;
	
	protected ConversationStream(ClientServiceProxy service, int initialLoad) {
		this.svc = service;
		this.initialLoad = initialLoad;
	}
	
	public void init(Settings settings) {
		initDataConnection();
	}
	
	private void initDataConnection() {
		svc.addNewDataHandler(new NewMessagesHandler() {						
			@Override
			public void onNewMessages(NewMessagesEvent event) {
				for(Message msg : event.getData()) {
					addMessage(msg, true);
				}
				hasMessages = hasMessages || event.getData().size() > 0;
			}
		});
		
		svc.getMessages(this.initialLoad, new AsyncCallback<List<Message>>() {				
			@Override
			public void onSuccess(List<Message> results) {
				for(Message msg : results) {
					addMessage(msg, false);	
				}
				hasMessages = hasMessages || results.size() > 0;
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Show error message to user
				GWT.log("ERROR (getMessages(" + initialLoad + ")): " + caught.getMessage());
			}
		});		
	}
	
	public boolean hasMessage() {
		return hasMessages;
	}

	protected abstract void addMessage(final Message msg, boolean live);
}
