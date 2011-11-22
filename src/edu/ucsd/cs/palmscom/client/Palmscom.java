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
public class Palmscom implements EntryPoint {
	private ClientServiceHandler service;
	private FlexTable stream;
	private ScrollPanel streamContainer;
	private DockLayoutPanel layout;
	private FlowPanel header;
	
	// "Input new-message" controls
	private FlowPanel inputPanel;
	private TextBox inputText;
	private Button sendButton;	
	
	// User list
	private ScrollPanel onlineUsersPanel;
	
	@Override
	public void onModuleLoad() {
		// init new service handler
		service = new PollingServiceHandler();
		
		createUserInterface();
		
		loadInitialData();
	}	
	
	private void loadInitialData() {
		// TODO Auto-generated method stub
		
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
				doSendMessageWorkflow(event);				
			}
		});
		
		inputPanel.add(inputText);
		inputPanel.add(sendButton);
		layout.addNorth(inputPanel, 50);
	}
	
	private void doUpdateUserList(List<User> onlineUsers) {		
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
		onlineUsersPanel.clear();
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
	private void doSendMessageWorkflow(ClickEvent event){		
		String msgtxt = inputText.getText().trim();
		
		// 2. Lock input, show sending indicator: goto 3
		inputText.setEnabled(false);
		sendButton.setEnabled(false);
		// TODO: add sending indicator
		
		
		// 3. Validate input: valid ? goto 4 : goto e1
		if(!msgtxt.isEmpty()) {
			
			// 4. Send to server: success ? goto 5 : goto e2
			Message msg = new Message(msgtxt);
			try {
				service.sendMessage(msg);
				
				// 5. Clear input: goto 6
				inputText.setText("");					
			} catch (ServiceException e) {
				// TODO: e2. Show error message: goto 6
				GWT.log(e.getMessage());							
			}
			
		} else {
			// TODO: e1. Show warning message: goto 6			
		}
				
		// 6. Unlock input, remove sending indicator, set focus to inputText: goto 1
		inputText.setEnabled(true);
		sendButton.setEnabled(true);
		// TODO: remove sending indicator
		inputText.setFocus(true);		
	}
}