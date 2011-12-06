package edu.ucsd.cs.palmscom.client.widgets;

import java.util.Date;

import org.adamtacy.client.ui.effects.events.EffectCompletedEvent;
import org.adamtacy.client.ui.effects.events.EffectCompletedHandler;
import org.adamtacy.client.ui.effects.impl.Fade;
import org.adamtacy.client.ui.effects.transitionsphysics.EaseInOutTransitionPhysics;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVisibility;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

import edu.ucsd.cs.palmscom.client.ClientServiceProxy;
import edu.ucsd.cs.palmscom.client.NotifyStateType;
import edu.ucsd.cs.palmscom.shared.Message;

public class CollapsedConversationStreamWidget extends Composite {
	private final FlowPanel stream = new FlowPanel();
	private final FlowPanel message = new FlowPanel();
	private NotifyStateType currentState;
	private final Fade activeNotify;
	
	public CollapsedConversationStreamWidget() {
		initWidget(stream);		
		stream.add(message);		
		message.setStylePrimaryName("message");
		
//		HTML defaultText = new HTML("PALMSCom");
//		defaultText.setStylePrimaryName("text");
//		defaultText.addStyleDependentName("default");
//		message.add(defaultText);
//		
		activeNotify = new Fade(message.getElement());
		activeNotify.setDuration(0.5);			
		activeNotify.setTransitionType(new EaseInOutTransitionPhysics());
		activeNotify.setLooping(true);
	}	
	
	public void setMessage(Message msg) {
		String timeauthor = "";
		
		timeauthor += "<span class=\"time\">" + DateTimeFormat.getFormat("h:mm a").format(msg.getDate()) +
				" - </span>";
		
		timeauthor += "<span class=\"author\">" + SafeHtmlUtils.htmlEscape(msg.getAuthor().getNickname()) + 
				": </span>";
				
		final HTML text = new HTML();
		text.setHTML(timeauthor + msg.getText());
		text.setStylePrimaryName("text");
		
		// animate if visible
		if(this.isAttached()) {
			final Fade fade = new Fade(message.getElement());		
			fade.setDuration(0.5);			
			fade.setTransitionType(new EaseInOutTransitionPhysics());
			fade.addEffectCompletedHandler(new EffectCompletedHandler() {
				boolean done = false;
				@Override
				public void onEffectCompleted(EffectCompletedEvent event) {
					if(!done) {
						done = true;
						message.clear();
						message.add(text);
						fade.resumeBackwards();
					}
				}
			});
			fade.play();
		} else {
			message.clear();
			message.add(text);			
		}
	}
	
	@Override
	public void onAttach() { 
		super.onAttach();
		
		if(currentState == NotifyStateType.ACTIVE_NOTIFY  || currentState == NotifyStateType.PASSIVE_NOTIFY)
			transition(NotifyStateType.ACTIVE);
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		transition(NotifyStateType.INACTIVE);
	}
	
	public void transition(NotifyStateType state) {
		GWT.log("transition: " + (new Date().toString()) + " - " + state.toString());
		currentState = state;
		 
		message.setStyleDependentName("notify", currentState == NotifyStateType.PASSIVE_NOTIFY ||
				currentState == NotifyStateType.ACTIVE_NOTIFY);
		
		if(currentState != NotifyStateType.ACTIVE_NOTIFY && activeNotify.isLooped()) {
			activeNotify.cancel();
			activeNotify.setLooping(false);
			activeNotify.resumeBackwards();
		} 
		
		if(currentState == NotifyStateType.ACTIVE_NOTIFY && !activeNotify.isLooped() && this.isVisible()) {
			activeNotify.setLooping(true);
			activeNotify.play();		
		}
	}	
}
