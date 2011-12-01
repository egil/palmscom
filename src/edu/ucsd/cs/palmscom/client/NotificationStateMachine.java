package edu.ucsd.cs.palmscom.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Timer;

import edu.ucsd.cs.palmscom.client.events.NotifyStateEvent;
import edu.ucsd.cs.palmscom.client.events.NotifyStateHandler;
import edu.ucsd.cs.palmscom.shared.Message;

public class NotificationStateMachine {
	private static int ACTIVE_NOTIFY_TIME = 10000; // 10 seconds
	private static int INACTIVE_NOTIFY_TIME = 1000 * 60 * 5; // 5 minutes
	private final HandlerManager handlerManager = new HandlerManager(this);
	private NotifyStateType state;
	private Timer activeNotifyTimer;
	private Timer inactiveTimer;
	
	public NotificationStateMachine() {
		this.state = NotifyStateType.INACTIVE;		
		
		activeNotifyTimer = new Timer() {			
			@Override
			public void run() {
				state = NotifyStateType.PASSIVE_NOTIFY;
				handlerManager.fireEvent(new NotifyStateEvent(state));
			}
		};
		
		inactiveTimer = new Timer() {			
			@Override
			public void run() {
				if(state == NotifyStateType.ACTIVE) {
					GWT.log("inactive timer");
					state = NotifyStateType.INACTIVE;
					handlerManager.fireEvent(new NotifyStateEvent(state));
				}
			}
		};
	}
	
	public void onNewMessage(Message msg) {
		// if the state is inactive, we will at least
		// switch to an active state
		if(state == NotifyStateType.INACTIVE) {
			state = NotifyStateType.ACTIVE;
			handlerManager.fireEvent(new NotifyStateEvent(state));
		}
		
		// if it is a MoI, we always jump to 
		// active notify state no matter the current sate
		if(msg.getIsMessageOfIntrest()) {
			state = NotifyStateType.ACTIVE_NOTIFY;
			handlerManager.fireEvent(new NotifyStateEvent(state));
			// restart activeNotifyTImer
			activeNotifyTimer.cancel();
			activeNotifyTimer.schedule(ACTIVE_NOTIFY_TIME);
		}
	
		// restart inactiveTimer;		
		inactiveTimer.cancel();
		inactiveTimer.schedule(INACTIVE_NOTIFY_TIME);
		
		//handlerManager.fireEvent(new NotifyStateEvent(state));
	}
	
	public void onUserClick() {
		state = NotifyStateType.ACTIVE;
		handlerManager.fireEvent(new NotifyStateEvent(state));
	}
	
	public void addStateChangeHandler(NotifyStateHandler handler) {
		handlerManager.addHandler(NotifyStateEvent.getType(), handler);  
	}
}