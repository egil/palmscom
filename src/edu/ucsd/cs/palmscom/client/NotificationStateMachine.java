package edu.ucsd.cs.palmscom.client;


import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Timer;

import edu.ucsd.cs.palmscom.client.event.NotifyStateEvent;
import edu.ucsd.cs.palmscom.shared.Message;

public class NotificationStateMachine {
	private static int ACTIVE_NOTIFY_TIME = 10000; // 10 seconds
	private static int INACTIVE_NOTIFY_TIME = 1000 * 60 * 5; // 5 minutes
	private final HandlerManager eventBus;
	private NotifyStateType state;
	private Timer activeNotifyTimer;
	private Timer inactiveTimer;

	public NotificationStateMachine(HandlerManager eb) {
		this.eventBus = eb;
		this.state = NotifyStateType.INACTIVE;

		activeNotifyTimer = new Timer() {			
			@Override
			public void run() {
				state = NotifyStateType.PASSIVE_NOTIFY;
				eventBus.fireEvent(new NotifyStateEvent(state));
			}
		};

		inactiveTimer = new Timer() {			
			@Override
			public void run() {
				if(state == NotifyStateType.ACTIVE) {
					GWT.log("inactive timer");
					state = NotifyStateType.INACTIVE;
					eventBus.fireEvent(new NotifyStateEvent(state));
				}
			}
		};
	}

	public void onNewMessage(Message msg) {
		// if the state is inactive, we will at least
		// switch to an active state
		if(state == NotifyStateType.INACTIVE) {
			state = NotifyStateType.ACTIVE;
			eventBus.fireEvent(new NotifyStateEvent(state));
		}

		// if it is a MoI, we always jump to 
		// active notify state no matter the current sate
		if(msg.isMessageOfIntrest()) {
			state = NotifyStateType.ACTIVE_NOTIFY;
			eventBus.fireEvent(new NotifyStateEvent(state));
			// restart activeNotifyTImer
			activeNotifyTimer.cancel();
			activeNotifyTimer.schedule(ACTIVE_NOTIFY_TIME);
		}

		// restart inactiveTimer;		
		inactiveTimer.cancel();
		inactiveTimer.schedule(INACTIVE_NOTIFY_TIME);
	}

	public void onUserClick() {
		state = NotifyStateType.ACTIVE;
		eventBus.fireEvent(new NotifyStateEvent(state));
	}
	
	public NotifyStateType getState() {
		return state;
	}
}
