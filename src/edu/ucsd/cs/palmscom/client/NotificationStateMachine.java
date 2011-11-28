package edu.ucsd.cs.palmscom.client;

import com.google.gwt.user.client.Timer;

import edu.ucsd.cs.palmscom.shared.Message;

public class NotificationStateMachine {
	private static int ACTIVE_NOTIFY_TIME = 10000; // 10 seconds
	private static int INACTIVE_NOTIFY_TIME = 1000 * 60 * 5; // 5 minutes
	private NotifyState state;
	final private StateChangeCallback callback;
	private Timer activeNotifyTimer;
	private Timer inactiveTimer;
	
	public NotificationStateMachine(final StateChangeCallback callback) {
		this.state = NotifyState.INACTIVE;		
		this.callback = callback;
		
		activeNotifyTimer = new Timer() {			
			@Override
			public void run() {
				state = NotifyState.PASSIVE_NOTIFY;
				callback.onStateChange(state);
			}
		};
		
		inactiveTimer = new Timer() {			
			@Override
			public void run() {
				state = NotifyState.INACTIVE;
				callback.onStateChange(state);
			}
		};
	}	
	
	public void onNewMessage(Message msg) {
		// if the state is inactive, we will at least
		// switch to an active state
		if(state == NotifyState.INACTIVE) {
			state = NotifyState.ACTIVE;
		}
		
		// if it is a MoI, we always jump to 
		// active notify state no matter the current sate
		if(msg.getIsMessageOfIntrest()) {
			state = NotifyState.ACTIVE_NOTIFY;
			// restart activeNotifyTImer
			activeNotifyTimer.cancel();
			activeNotifyTimer.schedule(ACTIVE_NOTIFY_TIME);
		}
	
		// restart inactiveTimer;		
		inactiveTimer.cancel();
		inactiveTimer.schedule(INACTIVE_NOTIFY_TIME);
		
		callback.onStateChange(state);
	}
	
	public void onUserClick() {
		state = NotifyState.ACTIVE;
		callback.onStateChange(state);
	}
}