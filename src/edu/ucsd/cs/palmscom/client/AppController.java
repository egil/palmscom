package edu.ucsd.cs.palmscom.client;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.google.gwt.user.client.Window.ClosingHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

import edu.ucsd.cs.palmscom.client.event.ToggleButtonClickedEvent;
import edu.ucsd.cs.palmscom.client.event.ToggleButtonClickedEventHandler;
import edu.ucsd.cs.palmscom.client.presenter.CollapsedPresenter;
import edu.ucsd.cs.palmscom.client.presenter.ExpandedPresenter;
import edu.ucsd.cs.palmscom.client.presenter.Presenter;
import edu.ucsd.cs.palmscom.client.view.CollapsedView;
import edu.ucsd.cs.palmscom.client.view.ExpandedView;
import edu.ucsd.cs.palmscom.shared.Settings;
import edu.ucsd.cs.palmscom.shared.User;
import edu.ucsd.cs.palmscom.widgets.CollapseStateChangeEvent;
import edu.ucsd.cs.palmscom.widgets.CollapseStateChangeEventHandler;
import edu.ucsd.cs.palmscom.widgets.CollapsiblePanel;
import edu.ucsd.cs.palmscom.widgets.CollapsiblePanel.CollapseState;

public class AppController {
	private final ServiceProxy service;
	private final HandlerManager eventBus;
	private final CollapsiblePanel container;

	public AppController(ServiceProxy service, HandlerManager eventBus) {
		this.service = service;
		this.eventBus = eventBus;
		this.container = new CollapsiblePanel();
		bind();
	}

	public void go(final RootPanel rootPanel, User currentUser, int collapsPoint) {
		container.setCollapsPoint(collapsPoint);
		AppState.getInstance().setUser(currentUser);
		
		// sign in to service, get users settings
		service.signIn(currentUser, new AsyncCallback<Settings>() {
			
			@Override
			public void onSuccess(Settings settings) {
				// make sure we sign out when the window is closed
				Window.addWindowClosingHandler(new ClosingHandler() {
					@Override
					public void onWindowClosing(ClosingEvent event) {
						service.signOut(null);
					}
				});
				
				AppState.getInstance().setSettings(settings);
				
				// set main stylesheets
				container.setStylePrimaryName("palmscom");
				
				// set palmscom collapsed orientation
				container.addStyleDependentName(AppState.getInstance().getSettings()
						.getCollapsedType().toString().toLowerCase());
				
				// Start listening for new messages or changes in users list
				service.listen();
				
				rootPanel.add(container);	
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO: show pretty login error message to user
			}
		});
		
	}
	
	private void bind() {
		// Browser width change 
		container.addHandler(new CollapseStateChangeEventHandler() {
			@Override
			public void onChange(CollapseStateChangeEvent event) {
				onStateChange();
			}
		}, CollapseStateChangeEvent.TYPE);
		
		// Bind to user click
		eventBus.addHandler(ToggleButtonClickedEvent.TYPE, new ToggleButtonClickedEventHandler() {			
			@Override
			public void onToggleButtonClicked(ToggleButtonClickedEvent event) {
				container.toggleVisualState();
				onStateChange();
			}
		});
	}
	
	private void onStateChange() {
		Presenter presenter;
		
		if(container.getVisualState() == CollapseState.COLLAPSED) {
			presenter = new CollapsedPresenter(service, eventBus, new CollapsedView());
		} else {
			presenter = new ExpandedPresenter(service, eventBus, new ExpandedView());			
		}
		
		presenter.go(container);
	}

}
