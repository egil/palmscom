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
import edu.ucsd.cs.palmscom.client.TimeUtils;
import edu.ucsd.cs.palmscom.client.events.AddedMessageEvent;
import edu.ucsd.cs.palmscom.client.events.AddedMessageHandler;
import edu.ucsd.cs.palmscom.client.events.NewMessagesEvent;
import edu.ucsd.cs.palmscom.client.events.NewMessagesHandler;
import edu.ucsd.cs.palmscom.client.events.ReplyButtonClickEvent;
import edu.ucsd.cs.palmscom.client.events.ReplyButtonClickHandler;
import edu.ucsd.cs.palmscom.shared.Message;
import edu.ucsd.cs.palmscom.shared.Settings;

public class ConversationStreamWidget extends Composite {
	private final HandlerManager handlerManager = new HandlerManager(this);
	private final ClientServiceProxy svc;
	private Settings settings;
	private Boolean hasFocus = false;
	private final ScrollPanel streamContainer = new ScrollPanel();
	private final FlowPanel stream = new FlowPanel();
	private RegExp userMatcher;
	private RegExp keywordMatcher;
	
	public ConversationStreamWidget(ClientServiceProxy service) {
		this.svc = service;
		initWidget(streamContainer);
		streamContainer.add(stream);
		stream.setStyleName("stream");
	}
	
	public void Init(Settings settings) {
		this.settings = settings;



		//((^|\s+)(search strings)([\W]*?)(\s+|$))
		//
		//Options: case insensitive; ^ and $ match at line breaks
		//
		//Match the regular expression below and capture its match into backreference number 1 «((^|\s+)(search strings)([\W]*?)(\s+|$))»
		//   Match the regular expression below and capture its match into backreference number 2 «(^|\s+)»
		//      Match either the regular expression below (attempting the next alternative only if this one fails) «^»
		//         Assert position at the beginning of a line (at beginning of the string or after a line break character) «^»
		//      Or match regular expression number 2 below (the entire group fails if this one fails to match) «\s+»
		//         Match a single character that is a “whitespace character” (spaces, tabs, and line breaks) «\s+»
		//            Between one and unlimited times, as many times as possible, giving back as needed (greedy) «+»
		//   Match the regular expression below and capture its match into backreference number 3 «(search strings)»
		//      Match the characters “search strings” literally «search strings»
		//   Match the regular expression below and capture its match into backreference number 4 «([\W]*?)»
		//      Match a single character that is a “non-word character” «[\W]*?»
		//         Between zero and unlimited times, as few times as possible, expanding as needed (lazy) «*?»
		//   Match the regular expression below and capture its match into backreference number 5 «(\s+|$)»
		//      Match either the regular expression below (attempting the next alternative only if this one fails) «\s+»
		//         Match a single character that is a “whitespace character” (spaces, tabs, and line breaks) «\s+»
		//            Between one and unlimited times, as many times as possible, giving back as needed (greedy) «+»
		//      Or match regular expression number 2 below (the entire group fails if this one fails to match) «$»
		//         Assert position at the end of a line (at the end of the string or before a line break character) «$»
		//
		userMatcher = RegExp.compile("((^|\\s+)(" + settings.getCurrentUser().getNickname() + ")([\\W]*?)(\\s+|$))", "gi");
		
		String keywords = "";
		for(int i = 0; i < settings.getKeywords().size(); i++) {
			keywords += i == 0 ? settings.getKeywords().get(i) : "|" + settings.getKeywords().get(i);
		}
		keywordMatcher = RegExp.compile("((^|\\s+)(" + keywords + ")([\\W]*?)(\\s+|$))", "gi");
		
		initDataConnection();
	}

	private void initDataConnection() {
		svc.addNewDataHandler(new NewMessagesHandler() {						
			@Override
			public void onNewMessages(NewMessagesEvent event) {
				for(Message msg : event.getData())
					addMessageToConversationStream(msg, true);
			}
		});
		
		svc.getMessages(20, new AsyncCallback<List<Message>>() {				
			@Override
			public void onSuccess(List<Message> results) {
				for(Message msg : results)
					addMessageToConversationStream(msg, false);					
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Show error message to user
				GWT.log("ERROR (getOnlineUsers): " + caught.getMessage());
			}
		});		
	}
	
	@SuppressWarnings("deprecation")
	private void addMessageToConversationStream(final Message msg, Boolean live) {
		// Test if message already exists in stream
		if(DOM.getElementById(msg.getHtmlID()) != null) {
			GWT.log("WARNING: The message " + msg.getID() + " was retransmitted, already exists in the conversation stream.");
			return;
		}
		
		// Personalize message, add span tags around words,
		// usernames, make links clickable, etc.
		personalizeMessage(msg);
			
		// create message header
		final FlowPanel row = new FlowPanel();
		row.setStylePrimaryName("message");

		// set id and optional classes
		row.getElement().setAttribute("id", msg.getHtmlID());
		if(msg.getIsMessageOfIntrest()) 
			row.addStyleName("moi");
		if(msg.isOwnMessage(settings.getCurrentUser()))
			row.addStyleName("own");
		
		// create header 
		FlowPanel header = new FlowPanel();
		header.setStylePrimaryName("header");
		row.add(header);
		
		// Author
		HTML author = new HTML("<h2>" + SafeHtmlUtils.htmlEscape(msg.getAuthor().getNickname()) + "</h2>");
		author.setStylePrimaryName("author");
		header.add(author);
		
		// Time
		Label time = new Label(); 
		time.setStylePrimaryName("time");
		time.setText(DateTimeFormat.getFormat("h:mm:ss a").format(msg.getDate()));
		// time.setText(TimeUtils.dateToLongDHMS(msg.getDate()) + " ago");
		header.add(time);
		
		// Reply link
		if(!msg.isOwnMessage(settings.getCurrentUser())) {
			Hyperlink replyLink = new Hyperlink();
			replyLink.setText("(reply)");
			replyLink.setStylePrimaryName("reply");
			replyLink.addClickHandler(new ClickHandler() {			
				@Override
				public void onClick(ClickEvent event) {
					handlerManager.fireEvent(new ReplyButtonClickEvent(msg.getAuthor().getNickname()));
				}
			});
			header.add(replyLink);
		}
				
		// create message body
		HTML body = new HTML("<p>" + msg.getText() + "</p>");
		body.setStylePrimaryName("text");	
		row.add(body);
		
		// TODO: Search through the current conversation stream and find correct position for message. Setting row = 0 assumes message are added in the right order.	
		stream.insert(row, 0);
		
		if(live && !hasFocus && !msg.isOwnMessage(settings.getCurrentUser())) {
			highlightMessage(row.getElement(), msg.getIsMessageOfIntrest());
		}
		
		// Trigger transition/notification
		handlerManager.fireEvent(new AddedMessageEvent(msg));
	}
	
	private void highlightMessage(Element element, final Boolean moi) {
		// Add highlight effect to new message
		
//		final ChangeColor flash = new ChangeColor(element);			
//		flash.setStartColor("rgb(255,255,255)");
//		flash.setEndColor("rgb(255,255,216)");
//		flash.setDuration(0.5);			
//		flash.setTransitionType(new EaseInOutTransitionPhysics());
//		flash.addEffectCompletedHandler(new EffectCompletedHandler() {
//			int flashCount = 2;
//			@Override
//			public void onEffectCompleted(EffectCompletedEvent event) {
//				flashCount--;
//				if(0 < flashCount) {
//					flash.play();
//				} else if(0 == flashCount && !moi) {						
//					flash.resumeBackwards();						
//				}
//			}
//		});

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

	private void personalizeMessage(Message msg) {		
		String text = msg.getText();
		text = SafeHtmlUtils.htmlEscape(text);

		// only highlight message if it is from somebody else
		if(!msg.isOwnMessage(settings.getCurrentUser())) {
			if(userMatcher.test(text))
				msg.setIsMessageOfIntrest(true);
			
			if(keywordMatcher.test(text))
				msg.setIsMessageOfIntrest(true);
			
			text = userMatcher.replace(text, "$2<span class=\"user\">$3</span>$4$5");
		}

		// highlight keywords
		if(settings.getKeywords().size() > 0) {
			text = keywordMatcher.replace(text, "$2<span class=\"keyword\">$3</span>$4$5");
		}
		
		msg.setText(text);		
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
}
