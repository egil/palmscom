package edu.ucsd.cs.palmscom.client.widgets;

import org.adamtacy.client.ui.effects.events.EffectCompletedEvent;
import org.adamtacy.client.ui.effects.events.EffectCompletedHandler;
import org.adamtacy.client.ui.effects.impl.ChangeColor;
import org.adamtacy.client.ui.effects.transitionsphysics.EaseInOutTransitionPhysics;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowResizeListener;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ProvidesResize;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;



import edu.ucsd.cs.palmscom.client.ClientServiceProxy;
import edu.ucsd.cs.palmscom.client.Collapsible;
import edu.ucsd.cs.palmscom.client.MessagePreprocessor;
import edu.ucsd.cs.palmscom.client.NotificationStateMachine;
import edu.ucsd.cs.palmscom.client.NotifyStateType;
import edu.ucsd.cs.palmscom.client.PollingServiceProxyImpl;
import edu.ucsd.cs.palmscom.client.VisualStateType;
import edu.ucsd.cs.palmscom.client.events.AddedMessageEvent;
import edu.ucsd.cs.palmscom.client.events.AddedMessageHandler;
import edu.ucsd.cs.palmscom.client.events.NotifyStateEvent;
import edu.ucsd.cs.palmscom.client.events.NotifyStateHandler;
import edu.ucsd.cs.palmscom.client.events.ReplyButtonClickEvent;
import edu.ucsd.cs.palmscom.client.events.ReplyButtonClickHandler;
import edu.ucsd.cs.palmscom.client.events.VisualStateChangeEvent;
import edu.ucsd.cs.palmscom.client.events.VisualStateChangeHandler;

import edu.ucsd.cs.palmscom.shared.Settings;

public class Palmscom extends ResizeComposite {
	// Primary layout controls
	private final CollapsibleFocusPanel root;	
	private final DockLayoutPanel layout = new DockLayoutPanel(Unit.PX);
	
	// 
	private final ClientServiceProxy svc = new PollingServiceProxyImpl();
	private Settings settings;
	private final NotificationStateMachine nsm = new NotificationStateMachine();
	
	// PALMSCom widgets
	private final HeaderWidget phw = new HeaderWidget();
	private final HTML defaultHeaderText = new HTML("<div class=\"message\">PALMSCom</div>");	
	private final OnlineUsersWidget ouw = new OnlineUsersWidget(svc);
	private final ConversationStreamWidget csw = new ConversationStreamWidget(svc);
	private final CreateMessageWidget cmw = new CreateMessageWidget(svc);
	private final CollapsedConversationStreamWidget ccsw = new CollapsedConversationStreamWidget();
	
	public Palmscom() {						
		// 1150 + 315 = width of palms + width of palmscom
		root = new CollapsibleFocusPanel(1150 + 315);		
		initWidget(root);
		
		// setup layout containers
		root.add(layout);
		layout.setStylePrimaryName("layout");		
		
		// add header
		phw.setStylePrimaryName("com-header");				
		phw.set(defaultHeaderText);		
		
		loadUserSettings();				
	}

	private void loadUserSettings() {
		// We only want to load one item at the time
		// to avoid blocking to many connections at the same time.
				
		// get user settings
		svc.getUserSettings(new AsyncCallback<Settings>() {				
			@Override
			public void onSuccess(Settings result) {
				settings = result;
				MessagePreprocessor.init(settings);
				// now we have the settings,
				// then we can configure and init
				// the user interface
				setupWidgets();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("ERROR (getUserSettings): " + caught.getMessage());
			}
		});			
	}
	
	private void setupWidgets() {
		root.addClickHandler(new ClickHandler() {				
			@Override
			public void onClick(ClickEvent event) {
				//nsm.onUserClick();				
			}
		});
		
		root.addFocusHandler(new FocusHandler() {
			
			@Override
			public void onFocus(FocusEvent event) {
				csw.setHasFocus(true);
			}
		});
		
		root.addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				csw.setHasFocus(false);				
			}
		});

		nsm.addStateChangeHandler(new NotifyStateHandler() {			
			@Override
			public void onStateChange(NotifyStateEvent event) {
				ccsw.transition(event.getState());
			}
		});
		
		cmw.addStateChangeHandler(new VisualStateChangeHandler() {			
			@Override
			public void onStateChange(VisualStateChangeEvent event) {				
				layout.setWidgetSize(cmw, cmw.getHeight());
			}
		});		
		csw.addAddedMessageHandler(new AddedMessageHandler() {			
			@Override
			public void onAddedMessage(AddedMessageEvent event) {
				ccsw.setMessage(event.getMessage());
				nsm.onNewMessage(event.getMessage());				
			}
		});
		csw.addReplyButtonClickHandler(new ReplyButtonClickHandler() {			
			@Override
			public void onReplyButtonClick(ReplyButtonClickEvent event) {
				cmw.setText(event.getNickname() + ": ");
			}
		});
		
		phw.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				root.toggleVisualState();
			}
		});
		
		/**
		 * Handle state changes from the FocusPanel
		 * that holds all elements.
		 */
		root.addHandler(new VisualStateChangeHandler() {			
			@Override
			public void onStateChange(VisualStateChangeEvent event) {
				toggleState(event.getState());
			}
		}, VisualStateChangeEvent.getType());
		
		/**
		 * Handle visual state change on the online users widget
		 */
		ouw.addStateChangeHandler(new VisualStateChangeHandler() {			
			@Override
			public void onStateChange(VisualStateChangeEvent event) {
				layout.setWidgetSize(ouw, ouw.getHeight());
			}
		});
		
		//ouw.init();
		csw.init(settings);
		toggleState(root.getVisualState());
	}
	
	private void toggleState(VisualStateType state) {
		if(state == VisualStateType.COLLAPSED) {			
			layout.clear();
			phw.set(ccsw);
			layout.add(phw);
		} else {
			layout.clear();
			phw.set(defaultHeaderText);
			layout.addNorth(phw, 31);
			layout.addNorth(cmw, cmw.getHeight());
			layout.addSouth(ouw, ouw.getHeight());
			layout.add(csw);
			csw.getElement().setAttribute("style", "overflow: auto; position: absolute;");
		}		
	}
}
