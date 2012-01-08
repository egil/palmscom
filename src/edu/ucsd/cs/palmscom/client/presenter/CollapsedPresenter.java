package edu.ucsd.cs.palmscom.client.presenter;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import edu.ucsd.cs.palmscom.client.AppState;
import edu.ucsd.cs.palmscom.client.ClientMessageDecorator;
import edu.ucsd.cs.palmscom.client.NotificationStateMachine;
import edu.ucsd.cs.palmscom.client.NotifyStateType;
import edu.ucsd.cs.palmscom.client.ServiceProxy;
import edu.ucsd.cs.palmscom.client.SimpleAsyncCallback;
import edu.ucsd.cs.palmscom.client.event.NewMessagesEvent;
import edu.ucsd.cs.palmscom.client.event.NewMessagesEventHandler;
import edu.ucsd.cs.palmscom.client.event.NotifyStateEvent;
import edu.ucsd.cs.palmscom.client.event.NotifyStateHandler;
import edu.ucsd.cs.palmscom.client.event.ToggleButtonClickedEvent;
import edu.ucsd.cs.palmscom.client.presenter.ExpandedPresenter.Display;
import edu.ucsd.cs.palmscom.client.view.CollapsedView;
import edu.ucsd.cs.palmscom.shared.PalmscomServiceAsync;
import edu.ucsd.cs.palmscom.widgets.CollapsiblePanel.CollapseState;

public class CollapsedPresenter extends Presenter<CollapsedPresenter.Display> {
	
	public interface Display extends edu.ucsd.cs.palmscom.client.presenter.Display {
		HasClickHandlers getToggleButton();
		HasClickHandlers getMessageArea();
		void addMessage(ClientMessageDecorator message, boolean isLive);
		void setTransition(NotifyStateType currentState);
	}
	
	private final NotificationStateMachine nsm;

	public CollapsedPresenter(ServiceProxy service, HandlerManager eventBus, Display view, HasWidgets container) {
		super(service, eventBus, view, container);
		nsm = new NotificationStateMachine(eventBus);
	}
	
	protected void bind() {
		view.getToggleButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				nsm.onUserClick();
				eventBus.fireEvent(new ToggleButtonClickedEvent());
			}
		});
		view.getMessageArea().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				nsm.onUserClick();
				eventBus.fireEvent(new ToggleButtonClickedEvent());
			}
		});
		eventBus.addHandler(NewMessagesEvent.TYPE, new NewMessagesEventHandler() {
			@Override
			public void onNewMessages(NewMessagesEvent event) {
				List<ClientMessageDecorator> msgs = event.getMessages();
				// iterate from last to first, since the newest message is at
				// the first position in the array
				for (int i = msgs.size()-1; i >= 0; i--) {
					view.addMessage(msgs.get(i), isVisible());
					nsm.onNewMessage(msgs.get(i));
				}
			}
		});
		eventBus.addHandler(NotifyStateEvent.TYPE, new NotifyStateHandler() {			
			@Override
			public void onStateChange(NotifyStateEvent event) {
				if(event.getState() != NotifyStateType.ACTIVE_NOTIFY || isVisible())
					view.setTransition(event.getState());
			}
		});
	}
	
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		nsm.onUserClick();
	}

	@Override
	protected void fetchData() {
		service.getMessages(1, new SimpleAsyncCallback<List<ClientMessageDecorator>>() {
			@Override
			public void onSuccess(List<ClientMessageDecorator> result) {
				if(result.size() == 1) view.addMessage(result.get(0), false);
			}
		});
	}
}
