package edu.ucsd.cs.palmscom.client;

import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;

import edu.ucsd.cs.palmscom.shared.Message;
import edu.ucsd.cs.palmscom.shared.ServiceException;
import edu.ucsd.cs.palmscom.shared.User;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Palmscom implements EntryPoint, StateChangeCallback, ClientServiceCallback {
	private ClientServiceHandler service;
	private NotificationStateMachine notifyStateMachine;
	private Dictionary settings;
	
	// Primary layout controls
	private DockLayoutPanel layout;
	private ScrollPanel streamContainer;
	
	// Header controls
	private FlowPanel header;

	// Conversation stream controls
	private FlexTable stream;
	
	// "Input new-message" controls
	private FlowPanel inputPanel;
	private TextBox inputText;
	private Button sendButton;	
	
	// User list
	private ScrollPanel onlineUsersPanel;
	
	@Override
	public void onModuleLoad() {
		service = new PollingServiceHandler();
		notifyStateMachine = new NotificationStateMachine(this);
		
		createUserInterface();		
		getAppData(0);	
		
		// NOTE: until addMessageToConversationStream handles ordering
		// of messages, subscribing at this point could lead to a race
		// condition between operations in getAppData(1/2).
		service.subscribe(this);
	}	
	
	private void getAppData(int counter) {
		// We only want to load one item at the time
		// to avoid blocking to many connections at the same time.
		
		// get user settings
		if(counter == 0) {
			service.getUserSettings(new AsyncCallback<Dictionary>() {				
				@Override
				public void onSuccess(Dictionary result) {
					settings = result;
					getAppData(1);					
				}
				
				@Override
				public void onFailure(Throwable caught) {
					GWT.log("ERROR (getUserSettings): " + caught.getMessage());
				}
			});
			return;
		}
		
		// get online users
		if(counter == 1) {
			service.getOnlineUsers(new AsyncCallback<List<User>>() {				
				@Override
				public void onSuccess(List<User> results) {
					updateUserList(results);
					getAppData(2);	
				}
				
				@Override
				public void onFailure(Throwable caught) {
					GWT.log("ERROR (getOnlineUsers): " + caught.getMessage());
				}
			});
			return;
		}
		
		if(counter == 2) {
			service.getMessages(20, new AsyncCallback<List<Message>>() {				
				@Override
				public void onSuccess(List<Message> results) {
					for(Message msg : results)
						addMessageToConversationStream(msg);
						
					getAppData(3);	
				}
				
				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					GWT.log("ERROR (getOnlineUsers): " + caught.getMessage());
				}
			});			
			return;			
		}		
	}

	public void createUserInterface() {
		layout = new DockLayoutPanel(Unit.PX);
		layout.setWidth("300px");
		
		// create header
		createHeaderUI();
		
		// create input UI
		createInputUI();
		
		// create footer/show list of online users panel
		onlineUsersPanel = new ScrollPanel();		
		onlineUsersPanel.setStylePrimaryName("onlineusers");
		onlineUsersPanel.setStyleName("collapsed", true);
		layout.addSouth(onlineUsersPanel, 30);

		// create the conversation stream
		streamContainer = new ScrollPanel();
		stream = new FlexTable();
		streamContainer.add(stream);
		layout.add(streamContainer);
		
		// add everything to the main root layout panel
		RootLayoutPanel.get().add(layout);
	}

	private void createHeaderUI() {
		header = new FlowPanel();
		header.setStylePrimaryName("input");
		header.add(new HTML("<h1>PALMS Communicator</h1>"));
		layout.addNorth(header, 50);
	}

	private void createInputUI() {
		// create input panel
		inputPanel = new FlowPanel();
		inputPanel.setStylePrimaryName("input");
		inputText = new TextBox();		
		sendButton  = new Button("Send");
		
		// allow users to click send button by hitting
		// while typing a message
		inputText.addKeyPressHandler(new KeyPressHandler() {
			// TODO: This apparently does not work properly in 
			// Firefox, workarounds include using key[up|down] instead,
			// but that has other problems such as repeat keys.
			public void onKeyPress(KeyPressEvent event) {
				if(event.getCharCode() == KeyCodes.KEY_ENTER) {
					sendButton.click();
				}
			}			
		});
		
		// handle click event on button, send 
		// new message to server
		sendButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				sendMessageWorkflow(event);				
			}
		});
		
		inputPanel.add(inputText);
		inputPanel.add(sendButton);
		layout.addNorth(inputPanel, 50);
	}
	
	private void addMessageToConversationStream(Message msg) {
		// Test if message already exists in stream
		if(DOM.getElementById(msg.getHtmlID()) != null) {
			GWT.log("WARNING: The message " + msg.getID() + " was retransmitted, already exists in the conversation stream.");
			return;
		}
		
		// Personalize message, add span tags around words,
		// usernames, make links clickable, etc.
		personalizeMessage(msg);
		
		// Insert message
		// TODO: Search through the current conversation stream and find correct position for message. Setting row = 0 assumes message are added in the right order.
		int row = 0;  
		String time = DateTimeFormat.getFormat("h:mm:ss a").format(msg.getDate());		
		stream.insertRow(row);
		DOM.setElementAttribute(stream.getRowFormatter().getElement(row), "id", msg.getHtmlID());
		stream.setText(row, 0, time);
		stream.getCellFormatter().addStyleName(row, 0, "time");
		stream.setText(row, 1, msg.getAuthor().getNickname());		
		stream.getCellFormatter().addStyleName(row, 1, "user");
		stream.setHTML(row, 2, msg.getText());
		stream.getCellFormatter().addStyleName(row, 2, "message");
		
		// Trigger transition/notification
		notifyStateMachine.onNewMessage(msg);
	}
	
	private void personalizeMessage(Message msg) {
		// TODO Auto-generated method stub
		
	}

	private void updateUserList(List<User> onlineUsers) {		
		Element admins = Document.get().createULElement();		
		Element supporters = Document.get().createULElement();
		Element users = Document.get().createULElement();
				
		for(User user : onlineUsers) {
			Element li = Document.get().createLIElement();
			li.addClassName(user.getType().toString().toLowerCase());			
			switch (user.getType()) {
				case ADMIN:
					admins.appendChild(li);
					li.setInnerText(user.getNickname() + " (" + user.getType().toString().toLowerCase() + ")");
					break;
				case SUPPORTER:
					supporters.appendChild(li);
					li.setInnerText(user.getNickname() + " (" + user.getType().toString().toLowerCase() + ")");
					break;
				case USER:
					users.appendChild(li);				
					li.setInnerText(user.getNickname());
					break;
			}			
		}
		// remove existing user list
		onlineUsersPanel.clear();
		// add new user lists
		onlineUsersPanel.getElement().appendChild(admins);
		onlineUsersPanel.getElement().appendChild(supporters);
		onlineUsersPanel.getElement().appendChild(users);		
	}
	
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
	private void sendMessageWorkflow(ClickEvent event){		
		String msgtxt = inputText.getText().trim();
		
		// 2. Lock input, show sending indicator: goto 3
		setSendMessageState();		
		
		// 3. Validate input: valid ? goto 4 : goto e1
		if(msgtxt.isEmpty()) {
			// TODO: Show warning message: goto 6
			
			// 6. Unlock input, remove sending indicator, set focus to inputText: goto 1
			setInputMessageState();
			
			return;
		}
			
		// 4. Send to server: success ? goto 5 : goto e2
		Message msg = new Message(msgtxt);
		service.sendMessage(msg, new AsyncCallback<Void>() {					
			@Override
			public void onSuccess(Void result) {
				GWT.log("INFO: Message saved succesfully.");

				// 5. Clear input: goto 6
				inputText.setText("");
				
				// 6. Unlock input, remove sending indicator, set focus to inputText: goto 1
				setInputMessageState();					
			}				
			
			@Override
			public void onFailure(Throwable caught) {
				GWT.log(caught.getMessage());

				// TODO: Show error message: goto 6
				
				// 6. Unlock input, remove sending indicator, set focus to inputText: goto 1
				setInputMessageState();
			}
		});		
	}

	private void setInputMessageState() {
		inputText.setEnabled(true);
		sendButton.setEnabled(true);
		// TODO: Remove sending indicator
		inputText.setFocus(true);
	}

	private void setSendMessageState() {
		inputText.setEnabled(false);
		sendButton.setEnabled(false);
		// TODO: Add sending indicator
	}

	@Override
	public void onStateChange(NotifyState state) {
		// TODO Create transition code
		
	}

	@Override
	public void onLiveMessages(List<Message> results) {
		for(Message msg : results)
			addMessageToConversationStream(msg);
	}

	@Override
	public void onLiveOnlineUserList(List<User> results) {
		updateUserList(results);		
	}
}