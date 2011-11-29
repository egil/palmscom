package edu.ucsd.cs.palmscom.client.widgets;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.ScrollPanel;

import edu.ucsd.cs.palmscom.client.ClientServiceHandler;
import edu.ucsd.cs.palmscom.client.Collapsible;
import edu.ucsd.cs.palmscom.client.NewMessagesEvent;
import edu.ucsd.cs.palmscom.client.NewMessagesHandler;
import edu.ucsd.cs.palmscom.client.NotificationStateMachine;
import edu.ucsd.cs.palmscom.client.NotifyStateEvent;
import edu.ucsd.cs.palmscom.client.NotifyStateHandler;
import edu.ucsd.cs.palmscom.client.NotifyStateType;
import edu.ucsd.cs.palmscom.client.PollingServiceHandler;
import edu.ucsd.cs.palmscom.client.VisualStateChangeEvent;
import edu.ucsd.cs.palmscom.client.VisualStateChangeHandler;
import edu.ucsd.cs.palmscom.shared.Message;

public class PalmscomWidget extends Composite implements Collapsible {
	private ClientServiceHandler service;
	private NotificationStateMachine notifyStateMachine;
	private CreateMessageWidget cmw;
	
	// Primary layout controls
	private final DockLayoutPanel layout = new DockLayoutPanel(Unit.PX);
	private ScrollPanel streamContainer;
	
	// Header controls
	private FlowPanel header;

	// Conversation stream controls
	private FlexTable stream;

	public PalmscomWidget() {
		initWidget(layout);
		
		notifyStateMachine = new NotificationStateMachine();
		notifyStateMachine.addStateChangeHandler(new NotifyStateHandler() {			
			@Override
			public void onStateChange(NotifyStateEvent event) {
				transitionToState(event.getState());
			}
		});
		
		service = new PollingServiceHandler();
		service.addNewDataHandler(new NewMessagesHandler() {						
			@Override
			public void onNewMessages(NewMessagesEvent event) {
				for(Message msg : event.getData())
					addMessageToConversationStream(msg);
			}
		});
		
		cmw = new CreateMessageWidget(service);
		
		createUserInterface();		
		getAppData(2);		
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
		layout.setStylePrimaryName("container");		
		
		// create header
		createHeaderUI();
		
		// set collapsed size initially
		layout.addNorth(cmw, cmw.getHeight());
		// set up handler for updating the size of the CMW widget
		cmw.addStateChangeHandler(new VisualStateChangeHandler() {			
			@Override
			public void onStateChange(VisualStateChangeEvent event) {				
				layout.setWidgetSize(cmw, cmw.getHeight());
			}
		});		
		
		// create footer/show list of online users panel
		final OnlineUsersWidget ouw = new OnlineUsersWidget(service);
		layout.addSouth(ouw, ouw.getHeight());
		ouw.addStateChangeHandler(new VisualStateChangeHandler() {			
			@Override
			public void onStateChange(VisualStateChangeEvent event) {
				layout.setWidgetSize(ouw, ouw.getHeight());
			}
		});

		// create the conversation stream
		streamContainer = new ScrollPanel();
		stream = new FlexTable();
		stream.setStylePrimaryName("stream");
		streamContainer.add(stream);
		layout.add(streamContainer);
		
		// add everything to the main root layout panel
		//RootLayoutPanel.get().add(layout);
	}

	private void createHeaderUI() {
		header = new FlowPanel();
		header.setStylePrimaryName("header");
		header.add(new HTML("<h1>PALMSCom</h1>"));
		layout.addNorth(header, 33);
	}
	
	private void addMessageToConversationStream(final Message msg) {
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
		Hyperlink replyLink = new Hyperlink();
		replyLink.setText("reply");
		replyLink.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				cmw.setText(msg.getAuthor().getNickname() + ": ");
			}
		});
		
		stream.setWidget(rowHead, 2, replyLink);		
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

	public void transitionToState(NotifyStateType state) {
		// TODO Create transition code	
		GWT.log(state.toString());
	}

	@Override
	public double getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}
}
