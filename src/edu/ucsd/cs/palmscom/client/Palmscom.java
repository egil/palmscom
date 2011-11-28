package edu.ucsd.cs.palmscom.client;

import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;

import edu.ucsd.cs.palmscom.client.widgets.CreateMessageWidget;
import edu.ucsd.cs.palmscom.client.widgets.OnlineUsersWidget;
import edu.ucsd.cs.palmscom.client.widgets.StateChangeEvent;
import edu.ucsd.cs.palmscom.client.widgets.StateChangeHandler;
import edu.ucsd.cs.palmscom.client.widgets.StateChangeType;
import edu.ucsd.cs.palmscom.shared.Message;
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
	
	@Override
	public void onModuleLoad() {
		service = new PollingServiceHandler();
		notifyStateMachine = new NotificationStateMachine(this);
		
		createUserInterface();		
		getAppData(2);
		
		// NOTE: until addMessageToConversationStream handles ordering
		// of messages, subscribing at this point could lead to a race
		// condition between operations in getAppData(1/2).
		service.subscribe(this);
	}	
	
	private void getAppData(int counter) {
		// We only want to load one item at the time
		// to avoid blocking to many connections at the same time.
		
		// get user settings
		// TODO: No user settings on server side yet!
//		if(counter == 0) {
//			service.getUserSettings(new AsyncCallback<Dictionary>() {				
//				@Override
//				public void onSuccess(Dictionary result) {
//					settings = result;
//					getAppData(1);					
//				}
//				
//				@Override
//				public void onFailure(Throwable caught) {
//					GWT.log("ERROR (getUserSettings): " + caught.getMessage());
//				}
//			});
//			return;
//		}		
		
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
					// TODO Show error message to user
					GWT.log("ERROR (getOnlineUsers): " + caught.getMessage());
				}
			});			
			return;			
		}		
	}

	public void createUserInterface() {
		layout = new DockLayoutPanel(Unit.PX);
		layout.setStylePrimaryName("container");		
		
		// create header
		//createHeaderUI();
		
		// http://stackoverflow.com/questions/3190577/resizing-a-gwt-docklayoutpanels-north-south-east-west-components
		final CreateMessageWidget cmw = new CreateMessageWidget(service);
		// set collapsed size initially
		layout.addNorth(cmw, CreateMessageWidget.COLLAPSED_SIZE);
		// set up handler for updating the size of the CMW widget
		cmw.addStateChangeHandler(new StateChangeHandler() {			
			@Override
			public void onStateChange(StateChangeEvent event) {
				if(event.getState() == StateChangeType.COLLAPSED)
					layout.setWidgetSize(cmw, CreateMessageWidget.COLLAPSED_SIZE);
				else
					layout.setWidgetSize(cmw, CreateMessageWidget.EXPANDED_SIZE);
			}
		});		
		
		// create footer/show list of online users panel
		OnlineUsersWidget ouw = new OnlineUsersWidget(service);
		layout.addSouth(ouw, 30);

		// create the conversation stream
		streamContainer = new ScrollPanel();
		stream = new FlexTable();
		stream.setStylePrimaryName("stream");
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
		int rowHead = 0;
		int rowBody = rowHead + 1;
		String time = DateTimeFormat.getFormat("h:mm:ss a").format(msg.getDate());		
		
		// create message header
		stream.insertRow(rowHead);
		DOM.setElementAttribute(stream.getRowFormatter().getElement(rowHead), "id", msg.getHtmlID());
		if(msg.getIsMessageOfIntrest()) stream.getRowFormatter().addStyleName(rowHead, "moi");
		
		stream.setText(rowHead, 0, time);
		stream.getCellFormatter().addStyleName(rowHead, 0, "time");
		stream.setText(rowHead, 1, msg.getAuthor().getNickname());		
		stream.getCellFormatter().addStyleName(rowHead, 1, "author");
		stream.setText(rowHead, 2, "reply");		
		stream.getCellFormatter().addStyleName(rowHead, 2, "reply");
		
		// create message body
		stream.insertRow(rowBody);
		if(msg.getIsMessageOfIntrest()) stream.getRowFormatter().addStyleName(rowBody, "moi");	
		stream.setHTML(rowBody, 0, msg.getText());
		stream.getCellFormatter().addStyleName(rowBody, 0, "message");
		stream.getFlexCellFormatter().setColSpan(rowBody, 0, 3);
		
		// Trigger transition/notification
		notifyStateMachine.onNewMessage(msg);
	}
	
	private void personalizeMessage(Message msg) {
		// TODO Auto-generated method stub
		
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
		//updateUserList(results);		
	}
}