package edu.ucsd.cs.palmscom.client.presenter;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasWidgets;

import edu.ucsd.cs.palmscom.client.ClientMessageDecorator;
import edu.ucsd.cs.palmscom.client.ServiceProxy;
import edu.ucsd.cs.palmscom.client.SimpleAsyncCallback;
import edu.ucsd.cs.palmscom.client.event.NewMessagesEvent;
import edu.ucsd.cs.palmscom.client.event.NewMessagesEventHandler;
import edu.ucsd.cs.palmscom.client.event.ToggleButtonClickedEvent;
import edu.ucsd.cs.palmscom.client.event.UpdateOnlineUserListEvent;
import edu.ucsd.cs.palmscom.client.event.UpdateOnlineUserListEventHandler;
import edu.ucsd.cs.palmscom.shared.Message;
import edu.ucsd.cs.palmscom.shared.MessageImpl;
import edu.ucsd.cs.palmscom.shared.User;

public class ExpandedPresenter extends Presenter<ExpandedPresenter.Display> {

	public interface Display extends edu.ucsd.cs.palmscom.client.presenter.Display {
		HasClickHandlers getToggleButton();
		void updateOnlineUsersList(User[] onlineUsers);
		HasClickHandlers getCreateMessageButton();
		HasClickHandlers getHeaderArea();
		Focusable getCreateMessageArea();
		HasText getCreateMessageText();
		void lockCreateMessageInput();
		void unlockCreatemessageInput();
		HasClickHandlers addMessage(ClientMessageDecorator message, boolean isLive);
	}
	
	public ExpandedPresenter(ServiceProxy service, HandlerManager eventBus, Display view, HasWidgets container) {
		super(service, eventBus, view, container);
	}

	protected void bind() {
		view.getToggleButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new ToggleButtonClickedEvent());
			}
		});
		view.getHeaderArea().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new ToggleButtonClickedEvent());
			}
		});
		eventBus.addHandler(UpdateOnlineUserListEvent.TYPE, new UpdateOnlineUserListEventHandler() {
			@Override
			public void onUpdateUserList(UpdateOnlineUserListEvent event) {
				view.updateOnlineUsersList(event.getUsers());
			}
		});
		eventBus.addHandler(NewMessagesEvent.TYPE, new NewMessagesEventHandler() {
			@Override
			public void onNewMessages(NewMessagesEvent event) {
				List<ClientMessageDecorator> msgs = event.getMessages();
				// iterate from last to first, since the newest message is at
				// the first position in the array
				for (int i = msgs.size()-1; i >= 0; i--) {
					addMessage(msgs.get(i), true);
				}
			}
		});
		
		// handle click event on button, send 
		// new message to server
		view.getCreateMessageButton().addClickHandler(new ClickHandler() {

			/**
			 * SEND MESSAGE WORKFLOW:
			 * 
			 *  1. Input state: onClick, goto 2
			 *  2. Lock input, show sending indicator: goto 3
			 *  3. Validate input: valid ? goto 4 : goto e1
			 *  4. Send to server: success ? goto 5 : goto e2
			 *  5. Clear input: goto 6
			 *  6. Unlock input, remove sending indicator, set focus to inputText: goto 1
			 *  e1. Show warning message: goto 6
			 *  e2. Show error message: goto 6  
			 */
			public void onClick(ClickEvent event) {
				String msgtxt = view.getCreateMessageText().getText().trim();

				// 2. Lock input, show sending indicator: goto 3
				view.lockCreateMessageInput();

				// 3. Validate input: valid ? goto 4 : goto e1
				if(msgtxt.isEmpty()) {
					// TODO: Show warning message: goto 6

					// 6. Unlock input, remove sending indicator, set focus to inputText: goto 1
					view.unlockCreatemessageInput();

					return;
				}
				
				Message msg = new MessageImpl();
				msg.setText(msgtxt);
				service.createMessage(msg, new AsyncCallback<Void>() {					
					@Override
					public void onSuccess(Void result) {
						GWT.log("INFO: Message saved succesfully.");

						// 5. Clear input: goto 6
						view.getCreateMessageText().setText("");

						// 6. Unlock input, remove sending indicator, set focus to inputText: goto 1
						view.unlockCreatemessageInput();					
					}				

					@Override
					public void onFailure(Throwable caught) {
						GWT.log(caught.getMessage());

						// TODO: Show error message: goto 6

						// 6. Unlock input, remove sending indicator, set focus to inputText: goto 1
						view.unlockCreatemessageInput();
					}
				});
			}
		});
	}

	@Override
	protected void fetchData() {
		service.getOnlineUsers(new SimpleAsyncCallback<User[]>() {
			@Override
			public void onSuccess(User[] result) {
				view.updateOnlineUsersList(result);
			}
		});
		
		service.getMessages(20, new SimpleAsyncCallback<List<ClientMessageDecorator>>() {
			@Override
			public void onSuccess(List<ClientMessageDecorator> result) {	
				// iterate from last to first, since the newest message is at
				// the first position in the array
				for (int i = result.size()-1; i >= 0; i--) {
					addMessage(result.get(i), false);
				}
			}
		});
	}	
	
	private void addMessage(final ClientMessageDecorator msg, boolean isLive) {
		HasClickHandlers hch = view.addMessage(msg, isLive);
		
		// trigger clicking on reply button/msg
		if(!msg.isOwnMessage()) {
			hch.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					view.getCreateMessageText().setText(msg.getAuthor().getFullname() + ": ");
					view.getCreateMessageArea().setFocus(true);
				}
			});
		}
	}
}