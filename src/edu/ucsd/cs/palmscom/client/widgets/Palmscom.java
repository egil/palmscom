package edu.ucsd.cs.palmscom.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.google.gwt.user.client.Window.ClosingHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ResizeComposite;

import edu.ucsd.cs.palmscom.client.ClientServiceProxy;
import edu.ucsd.cs.palmscom.client.MessagePreprocessor;
import edu.ucsd.cs.palmscom.client.NotificationStateMachine;
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
import edu.ucsd.cs.palmscom.shared.User;

public class Palmscom extends ResizeComposite {
	// Primary layout controls
	private final CollapsiblePanel root;	
	private final DockLayoutPanel layout = new DockLayoutPanel(Unit.PX);
	
	// 
	private final ClientServiceProxy svc = new PollingServiceProxyImpl();
	private Settings settings;
	private final User user;
	private final NotificationStateMachine nsm = new NotificationStateMachine();
	
	// PALMSCom widgets
	private final HeaderWidget phw = new HeaderWidget();
	private final HTML defaultHeaderText = new HTML("<div class=\"message\">PALMSCom</div>");	
//	private final OnlineUsersWidget ouw = new OnlineUsersWidget(svc);
//	private final ConversationStreamWidget csw = new ConversationStreamWidget(svc);
//	private final CreateMessageWidget cmw = new CreateMessageWidget(svc);
//	private final CollapsedConversationStreamWidget ccsw = new CollapsedConversationStreamWidget();
	private OnlineUsersWidget ouw;
	private ConversationStreamWidget csw;
	private CreateMessageWidget cmw;
	private CollapsedConversationStreamWidget ccsw;
	
	public Palmscom(User user, int collapsPoint) {						
		root = new CollapsiblePanel(collapsPoint);		
		initWidget(root);
		
		this.user = user;
		
		// set primary style
		this.setStylePrimaryName("palmscom");
		
		// setup layout containers
		root.add(layout);
		layout.setStylePrimaryName("layout");		
		
		// add header
		phw.setStylePrimaryName("com-header");				
		phw.set(defaultHeaderText);		
		
		signIn();
	}

	private void signIn() {
		// We only want to load one item at the time
		// to avoid blocking to many connections at the same time.
				
		// get user settings
		svc.signIn(user, new AsyncCallback<Settings>() {				
			@Override
			public void onSuccess(Settings result) {
				// make sure we sign out when the window is closed
				Window.addWindowClosingHandler(new ClosingHandler() {
					@Override
					public void onWindowClosing(ClosingEvent event) {
						svc.singOut(null);
					}
				});
				
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
		ouw = new OnlineUsersWidget(svc);
		csw = new ConversationStreamWidget(svc);
		cmw = new CreateMessageWidget(svc);
		ccsw = new CollapsedConversationStreamWidget();
		
//		root.addFocusHandler(new FocusHandler() {
//			
//			@Override
//			public void onFocus(FocusEvent event) {
//				csw.setHasFocus(true);
//			}
//		});
//		
//		root.addBlurHandler(new BlurHandler() {
//			
//			@Override
//			public void onBlur(BlurEvent event) {
//				csw.setHasFocus(false);				
//			}
//		});

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
