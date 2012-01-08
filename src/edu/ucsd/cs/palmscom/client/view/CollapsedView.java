package edu.ucsd.cs.palmscom.client.view;

import java.util.Date;

import org.adamtacy.client.ui.effects.events.EffectCompletedEvent;
import org.adamtacy.client.ui.effects.events.EffectCompletedHandler;
import org.adamtacy.client.ui.effects.impl.Fade;
import org.adamtacy.client.ui.effects.transitionsphysics.EaseInOutTransitionPhysics;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;

import edu.ucsd.cs.palmscom.client.ClientMessageDecorator;
import edu.ucsd.cs.palmscom.client.NotifyStateType;
import edu.ucsd.cs.palmscom.client.presenter.CollapsedPresenter;
import edu.ucsd.cs.palmscom.widgets.ClickFlowPanel;

public class CollapsedView extends View implements CollapsedPresenter.Display {

	private final FlowPanel container = new FlowPanel();
	private final HTML toggler = new HTML();
	private final ClickFlowPanel message = new ClickFlowPanel();
	private NotifyStateType currentState;
	private final Fade activeNotify;
	
	public CollapsedView() {
		initWidget(container);
		
		// configure and setup
		container.setStylePrimaryName("header");
		toggler.setStylePrimaryName("toggler");
		container.add(toggler);
		
		message.setStyleName("message");
		message.add(new HTML("PALMSCom"));
		container.add(message);

		activeNotify = new Fade(message.getElement());
		activeNotify.setDuration(0.5);			
		activeNotify.setTransitionType(new EaseInOutTransitionPhysics());
		activeNotify.setLooping(true);
	}
	
	@Override
	public HasClickHandlers getToggleButton() {
		return toggler;
	}

	@Override
	public void addMessage(ClientMessageDecorator msg, boolean isVisible) {
		String timeauthor = "";

		timeauthor += "<span class=\"time\">" + DateTimeFormat.getFormat("h:mm a").format(msg.getDate()) +
				" - </span>";

		timeauthor += "<span class=\"author\">" + SafeHtmlUtils.htmlEscape(msg.getAuthor().getFullname()) + 
				": </span>";

		final HTML text = new HTML();
		text.setHTML(timeauthor + msg.getText());
		text.setStylePrimaryName("text");

		// animate if visible
		if(isVisible) {
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
	public void setTransition(NotifyStateType state) {
		GWT.log("transition: " + (new Date().toString()) + " - " + state.toString());
		currentState = state;

		message.setStyleDependentName("notify", currentState == NotifyStateType.PASSIVE_NOTIFY || currentState == NotifyStateType.ACTIVE_NOTIFY);

		if(currentState != NotifyStateType.ACTIVE_NOTIFY && activeNotify.isLooped()) {
			activeNotify.cancel();
			activeNotify.setLooping(false);
			activeNotify.resumeBackwards();
		} 

		if(currentState == NotifyStateType.ACTIVE_NOTIFY && !activeNotify.isLooped()) {
			activeNotify.setLooping(true);
			activeNotify.play();		
		}
	}

	@Override
	public HasClickHandlers getMessageArea() {
		return message;
	}

}
