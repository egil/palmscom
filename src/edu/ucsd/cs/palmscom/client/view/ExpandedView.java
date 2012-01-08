package edu.ucsd.cs.palmscom.client.view;

import org.adamtacy.client.ui.effects.events.EffectCompletedEvent;
import org.adamtacy.client.ui.effects.events.EffectCompletedHandler;
import org.adamtacy.client.ui.effects.impl.Fade;
import org.adamtacy.client.ui.effects.transitionsphysics.EaseInOutTransitionPhysics;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;

import edu.ucsd.cs.palmscom.client.ClientMessageDecorator;
import edu.ucsd.cs.palmscom.client.presenter.ExpandedPresenter;
import edu.ucsd.cs.palmscom.shared.User;
import edu.ucsd.cs.palmscom.widgets.ClickFlowPanel;
import edu.ucsd.cs.palmscom.widgets.WatermarkTextArea;

public class ExpandedView extends View implements ExpandedPresenter.Display {
	// base layout
	private final DockLayoutPanel layout = new DockLayoutPanel(Unit.PX);
	
	// header panel
	private final ClickFlowPanel headerPanel = new ClickFlowPanel();
	private final HTML headerToggler = new HTML();
	private final HTML headerText =  new HTML("PALMSCom");

	// create message 
	private static final double CREATE_MESSAGE_COLLAPSED_SIZE = 53;
	private static final double CREATE_MESSAGE_EXPANDED_SIZE = 90;
	private final FlowPanel createMessagePanel = new FlowPanel();
	private final WatermarkTextArea createMessageText = new WatermarkTextArea();
	private final Button createMessageButton = new Button("Send");
	private boolean hasFocus = false;
	
	// message stream
	private final ScrollPanel messageStreamScrollPanel = new ScrollPanel();
	private final FlowPanel messageStreamPanel = new FlowPanel();
	
	// online users list
	private final FocusPanel onlineUsersContainerPanel = new FocusPanel();
	private final FlowPanel onlineUsersPanel = new FlowPanel();
	private final ScrollPanel onlineUsersList = new ScrollPanel();
	private final HTML onlineUsersSummary = new HTML();
	private static final double ONLINE_USERS_COLLAPSED_SIZE = 30;
	private int onlineUsersCount = 1;
	
	public ExpandedView() {
		initWidget(layout);
		layout.setStylePrimaryName("layout");
		layout.addNorth(headerPanel, 31);
		layout.addNorth(createMessagePanel, CREATE_MESSAGE_COLLAPSED_SIZE);
		layout.addSouth(onlineUsersContainerPanel, ONLINE_USERS_COLLAPSED_SIZE);
		layout.add(messageStreamScrollPanel);
		
		// configure and setup
		initHeader();
		
		// create message
		initCreateMessage();
		
		// message stream
		initMessageStream();		
		
		// online users list
		initOnlineUsersList();
	}

	private void initOnlineUsersList() {
		onlineUsersContainerPanel.setStylePrimaryName("online-users");
		onlineUsersContainerPanel.add(onlineUsersPanel);
		onlineUsersPanel.add(onlineUsersSummary);
		onlineUsersPanel.add(onlineUsersList);
		onlineUsersList.setVisible(false); // default is hidden
		// the click handler should ideally be in the presenter,
		// but since this has no side effects on the rest of the view
		// it seems more clean to keep it here.
		onlineUsersContainerPanel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				onlineUsersSummary.setVisible(!onlineUsersSummary.isVisible());
				onlineUsersList.setVisible(!onlineUsersList.isVisible());

				if(onlineUsersSummary.isVisible())
					layout.setWidgetSize(onlineUsersContainerPanel, ONLINE_USERS_COLLAPSED_SIZE);
				else
					layout.setWidgetSize(onlineUsersContainerPanel, onlineUsersCount * 10 + ONLINE_USERS_COLLAPSED_SIZE);
			}
		});
	}

	private void initMessageStream() {
		messageStreamScrollPanel.add(messageStreamPanel);
		messageStreamScrollPanel.setStyleName("stream");
	}

	private void initCreateMessage() {
		createMessagePanel.setStylePrimaryName("create-message");
		createMessageText.setWatermark("Write a new message . . .");
		createMessageButton.setVisible(false);
		createMessagePanel.add(createMessageText);
		createMessagePanel.add(createMessageButton);
		
		createMessageText.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					createMessageButton.click();
				} else if (event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE) {
					createMessageText.setText("");
					createMessageText.setFocus(false);
				}
			}
		});

		createMessageText.addFocusHandler(new FocusHandler() {			
			@Override
			public void onFocus(FocusEvent event) {
				createMessageButton.setVisible(true);
				layout.setWidgetSize(createMessagePanel, CREATE_MESSAGE_EXPANDED_SIZE);
				hasFocus = true;
			}
		});

		createMessageText.addBlurHandler(new BlurHandler() {			
			@Override
			public void onBlur(BlurEvent event) {		
				if(createMessageText.getText().isEmpty()) {
					createMessageButton.setVisible(false);
					layout.setWidgetSize(createMessagePanel, CREATE_MESSAGE_COLLAPSED_SIZE);
					hasFocus = false;
				}
			}
		});
	}

	private void initHeader() {
		headerPanel.setStylePrimaryName("header");
		headerToggler.setStylePrimaryName("toggler");
		headerText.setStylePrimaryName("message");
		headerPanel.add(headerToggler);
		headerPanel.add(headerText);
	}

	@Override
	public HasClickHandlers getToggleButton() {
		return headerToggler;
	}

	@Override
	public void updateOnlineUsersList(User[] onlineUsers) {
		int adms = 0, sups = 0, usrs = 0;

		for(User u : onlineUsers) {
			switch (u.getType()) {
			case ADMIN:
				adms++;
				break;
			case SUPPORTER:
				sups++;
				break;
			case USER:
				usrs++;
				break;
			}
		}

		onlineUsersSummary.setHTML("Users online (" + usrs + ") Supporters online (" + (adms + sups) + ")");

		Element admins = Document.get().createULElement();		
		Element supporters = Document.get().createULElement();
		Element users = Document.get().createULElement();

		for(User user : onlineUsers) {
			Element li = Document.get().createLIElement();
			li.addClassName(user.getType().toString().toLowerCase());			
			switch (user.getType()) {
				case ADMIN:
					admins.appendChild(li);
					li.setInnerText(user.getType().toString().toLowerCase() + ": " + user.getFullname() + " (" + user.getUsername() + ")");
					break;
				case SUPPORTER:
					supporters.appendChild(li);
					li.setInnerText(user.getType().toString().toLowerCase() + ": " + user.getFullname() + " (" + user.getUsername() + ")");
					break;
				case USER:
					users.appendChild(li);				
					li.setInnerText(user.getFullname() + " (" + user.getUsername() + ")");
					break;
			}			
		}
		// remove existing user list
		onlineUsersList.clear();
		// add new user lists
		onlineUsersList.getElement().appendChild(admins);
		onlineUsersList.getElement().appendChild(supporters);
		onlineUsersList.getElement().appendChild(users);
		onlineUsersCount = onlineUsers.length;
	}

	@Override
	public HasClickHandlers getCreateMessageButton() {
		return createMessageButton;
	}

	@Override
	public HasText getCreateMessageText() {
		return createMessageText;
	}

	@Override
	public void lockCreateMessageInput() {
		createMessageText.setEnabled(false);
		createMessageButton.setEnabled(false);		
	}

	@Override
	public void unlockCreatemessageInput() {
		createMessageText.setEnabled(true);
		createMessageButton.setEnabled(true);
		createMessageText.setFocus(true);
	}

	@Override
	public HasClickHandlers addMessage(ClientMessageDecorator msg, boolean isLive) {
		// create message header
		final ClickFlowPanel row = new ClickFlowPanel();
		row.setStylePrimaryName("message");

		// set id and optional classes
		row.getElement().setAttribute("id", msg.getHtmlID());
		if(msg.getIsMessageOfIntrest()) 
			row.addStyleName("moi");
		if(msg.isOwnMessage())
			row.addStyleName("own");

		// create header 
		FlowPanel header = new FlowPanel();
		header.setStylePrimaryName("header");
		row.add(header);

		// Author
		HTML author = new HTML(SafeHtmlUtils.htmlEscape(msg.getAuthor().getFullname()));
		author.setStylePrimaryName("author");
		header.add(author);

		// Time
		Label time = new Label(); 
		time.setStylePrimaryName("time");
		time.setText(DateTimeFormat.getFormat("h:mm a").format(msg.getDate()));
		// time.setText(TimeUtils.dateToLongDHMS(msg.getDate()) + " ago");
		header.add(time);

		// Reply link
		Hyperlink replyLink = null;
		if(!msg.isOwnMessage()) {
			replyLink = new Hyperlink();
			replyLink.setText("(reply)");
			replyLink.setStylePrimaryName("reply");
			header.add(replyLink);
		}

		// create message body
		HTML body = new HTML(msg.getText());
		body.setStylePrimaryName("text");	
		row.add(body);

		// TODO: Search through the current conversation stream and find correct position for message. Setting row = 0 assumes message are added in the right order.	
		messageStreamPanel.insert(row, 0);

		if(isLive && !hasFocus && !msg.isOwnMessage()) {
			blinkMessage(row.getElement(), msg.getIsMessageOfIntrest());
		}

		return row;
	}

	private void blinkMessage(Element element, final Boolean moi) {
		final Fade flash = new Fade(element);
		flash.setDuration(0.5);			
		flash.setTransitionType(new EaseInOutTransitionPhysics());
		flash.addEffectCompletedHandler(new EffectCompletedHandler() {
			int flashCount = 2;
			@Override
			public void onEffectCompleted(EffectCompletedEvent event) {
				flashCount--;
				if(0 < flashCount) {
					flash.play();
				} else if(0 == flashCount) {						
					flash.resumeBackwards();						
				}
			}
		});
		flash.play();
	}

	@Override
	public Focusable getCreateMessageArea() {
		return createMessageText;
	}

	@Override
	public HasClickHandlers getHeaderArea() {
		return headerPanel;
	}
}
