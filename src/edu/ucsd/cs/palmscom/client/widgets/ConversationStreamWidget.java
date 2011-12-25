package edu.ucsd.cs.palmscom.client.widgets;

import java.util.List;

import org.adamtacy.client.ui.effects.events.EffectCompletedEvent;
import org.adamtacy.client.ui.effects.events.EffectCompletedHandler;
import org.adamtacy.client.ui.effects.impl.ChangeColor;
import org.adamtacy.client.ui.effects.impl.Fade;
import org.adamtacy.client.ui.effects.impl.Highlight;
import org.adamtacy.client.ui.effects.transitionsphysics.BounceTransitionPhysics;
import org.adamtacy.client.ui.effects.transitionsphysics.EaseInOutTransitionPhysics;
import org.adamtacy.client.ui.effects.transitionsphysics.ElasticTransitionPhysics;
import org.adamtacy.client.ui.effects.transitionsphysics.LinearTransitionPhysics;
import org.apache.commons.lang.StringUtils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;

import edu.ucsd.cs.palmscom.client.ClientServiceProxy;
import edu.ucsd.cs.palmscom.client.NotifyStateType;
import edu.ucsd.cs.palmscom.client.TimeUtils;
import edu.ucsd.cs.palmscom.client.VisualStateType;
import edu.ucsd.cs.palmscom.client.events.AddedMessageEvent;
import edu.ucsd.cs.palmscom.client.events.AddedMessageHandler;
import edu.ucsd.cs.palmscom.client.events.NewMessagesEvent;
import edu.ucsd.cs.palmscom.client.events.NewMessagesHandler;
import edu.ucsd.cs.palmscom.client.events.ReplyButtonClickEvent;
import edu.ucsd.cs.palmscom.client.events.ReplyButtonClickHandler;
import edu.ucsd.cs.palmscom.client.events.VisualStateChangeEvent;
import edu.ucsd.cs.palmscom.client.events.VisualStateChangeHandler;
import edu.ucsd.cs.palmscom.shared.Message;
import edu.ucsd.cs.palmscom.shared.Settings;

public class ConversationStreamWidget extends ConversationStream {
	private final HandlerManager handlerManager = new HandlerManager(this);
	private boolean hasFocus = false;
	private VisualStateType state = VisualStateType.EXPANDED;
	private final FlowPanel layout = new FlowPanel();
	private final ScrollPanel streamContainer = new ScrollPanel();
	private final FlowPanel stream = new FlowPanel();
	
	public ConversationStreamWidget(ClientServiceProxy service) {
		super(service, 20);
		initWidget(layout);
		layout.add(streamContainer);
		streamContainer.setStyleName("stream");
		streamContainer.add(stream);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected void addMessage(final Message msg, boolean live) {
		// Test if message already exists in stream
		if(DOM.getElementById(msg.getHtmlID()) != null) {
			GWT.log("WARNING: The message " + msg.getID() + " was retransmitted, already exists in the conversation stream.");
			return;
		}
					
		// create message header
		final FlowPanel row = new FlowPanel();
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
		if(!msg.isOwnMessage()) {
			Hyperlink replyLink = new Hyperlink();
			replyLink.setText("(reply)");
			replyLink.setStylePrimaryName("reply");
			replyLink.addClickHandler(new ClickHandler() {			
				@Override
				public void onClick(ClickEvent event) {
					handlerManager.fireEvent(new ReplyButtonClickEvent(msg.getAuthor().getFullname()));
				}
			});
			header.add(replyLink);
		}
				
		// create message body
		HTML body = new HTML(msg.getText());
		body.setStylePrimaryName("text");	
		row.add(body);
		
		// TODO: Search through the current conversation stream and find correct position for message. Setting row = 0 assumes message are added in the right order.	
		stream.insert(row, 0);
		
		if(live && !hasFocus && !msg.isOwnMessage()) {
			blinkMessage(row.getElement(), msg.getIsMessageOfIntrest());
		}
		
		// Trigger transition/notification
		handlerManager.fireEvent(new AddedMessageEvent(msg));
	}
	
	private void animateBackgroundMessage(Element element, final Boolean moi) {		
		final ChangeColor flash = new ChangeColor(element);			
		flash.setStartColor("rgb(255,255,255)");
		flash.setEndColor("rgb(255,255,216)");
		flash.setDuration(0.5);			
		flash.setTransitionType(new EaseInOutTransitionPhysics());
		flash.addEffectCompletedHandler(new EffectCompletedHandler() {
			int flashCount = 2;
			@Override
			public void onEffectCompleted(EffectCompletedEvent event) {
				flashCount--;
				if(0 < flashCount) {
					flash.play();
				} else if(0 == flashCount && !moi) {						
					flash.resumeBackwards();						
				}
			}
		});

		flash.play();
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
	
	public void setHasFocus(Boolean hasFocus){
		this.hasFocus = hasFocus;
	}
	
	public void addAddedMessageHandler(AddedMessageHandler handler) {
		handlerManager.addHandler(AddedMessageEvent.getType(), handler);  
	}
	
	public void addReplyButtonClickHandler(ReplyButtonClickHandler handler) {
		handlerManager.addHandler(ReplyButtonClickEvent.getType(), handler);  
	}

	public void addVisualStateChangeHandler(VisualStateChangeHandler handler) {
		handlerManager.addHandler(VisualStateChangeEvent.getType(), handler);  
	}
}
