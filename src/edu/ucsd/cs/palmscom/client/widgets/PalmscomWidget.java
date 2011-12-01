package edu.ucsd.cs.palmscom.client.widgets;

import org.adamtacy.client.ui.effects.events.EffectCompletedEvent;
import org.adamtacy.client.ui.effects.events.EffectCompletedHandler;
import org.adamtacy.client.ui.effects.impl.ChangeColor;
import org.adamtacy.client.ui.effects.transitionsphysics.EaseInOutTransitionPhysics;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.LayoutPanel;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;



import edu.ucsd.cs.palmscom.client.ClientServiceProxy;
import edu.ucsd.cs.palmscom.client.Collapsible;
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

public class PalmscomWidget extends Composite implements Collapsible {
	private final ClientServiceProxy svc = new PollingServiceProxyImpl();
	private final NotificationStateMachine nsm = new NotificationStateMachine();
	private VisualStateType state = VisualStateType.EXPANDED;
	private Boolean hasFocus = false; 
	private Settings settings;
	
	// Primary layout controls
	private final DockLayoutPanel layout = new DockLayoutPanel(Unit.PX);

	
	// Widgets
	private final FlowPanel header = new FlowPanel();
	private final CreateMessageWidget cmw = new CreateMessageWidget(svc);
	private final OnlineUsersWidget ouw = new OnlineUsersWidget(svc);
	private final ConversationStreamWidget csw = new ConversationStreamWidget(svc);
	
	public PalmscomWidget() {								
		initWidget(layout);
		getUserSettings();		
	}	
	
	private void getUserSettings() {
		// We only want to load one item at the time
		// to avoid blocking to many connections at the same time.
				
		// get user settings
		svc.getUserSettings(new AsyncCallback<Settings>() {				
			@Override
			public void onSuccess(Settings result) {
				settings = result;
				
				// now we have the settings,
				// then we can configure and init
				// the user interface
				configureUserInterface();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("ERROR (getUserSettings): " + caught.getMessage());
			}
		});			
	}

	public void configureUserInterface() {
		// set style and wrap layout in focus panel
		layout.setStylePrimaryName("container");
		
		nsm.addStateChangeHandler(new NotifyStateHandler() {			
			@Override
			public void onStateChange(NotifyStateEvent event) {
				transitionToState(event.getState());
			}
		});
		
		// create header
		createHeaderUI();
		
		// set collapsed size initially		
		layout.addNorth(cmw, cmw.getHeight());
		// set up handler for updating the size of the CMW widget
		cmw.addStateChangeHandler(new VisualStateChangeHandler() {			
			@Override
			public void onStateChange(VisualStateChangeEvent event) {				
				layout.setWidgetSize(cmw, cmw.getHeight());
				hasFocus = event.getState() == VisualStateType.EXPANDED;
				csw.setHasFocus(hasFocus);
			}
		});		
		
		// create footer/show list of online users panel		
		layout.addSouth(ouw, ouw.getHeight());
		ouw.addStateChangeHandler(new VisualStateChangeHandler() {			
			@Override
			public void onStateChange(VisualStateChangeEvent event) {
				layout.setWidgetSize(ouw, ouw.getHeight());
			}
		});

		// add the conversation stream
		csw.addAddedMessageHandler(new AddedMessageHandler() {			
			@Override
			public void onAddedMessage(AddedMessageEvent event) {
				nsm.onNewMessage(event.getMessage());				
			}
		});
		csw.addReplyButtonClickHandler(new ReplyButtonClickHandler() {			
			@Override
			public void onReplyButtonClick(ReplyButtonClickEvent event) {
				cmw.setText(event.getNickname() + ": ");
			}
		});
		layout.add(csw);
		csw.Init(settings);
	}
	
	private void createHeaderUI() {
		header.setStylePrimaryName("header");
		header.add(new HTML("<h1>PALMSCom</h1>"));
		layout.addNorth(header, 33);
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
