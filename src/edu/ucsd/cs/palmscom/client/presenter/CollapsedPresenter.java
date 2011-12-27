package edu.ucsd.cs.palmscom.client.presenter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;

import edu.ucsd.cs.palmscom.client.ServiceProxy;
import edu.ucsd.cs.palmscom.client.event.ToggleButtonClickedEvent;
import edu.ucsd.cs.palmscom.client.presenter.ExpandedPresenter.Display;
import edu.ucsd.cs.palmscom.client.view.CollapsedView;
import edu.ucsd.cs.palmscom.shared.PalmscomServiceAsync;
import edu.ucsd.cs.palmscom.widgets.CollapsiblePanel.CollapseState;

public class CollapsedPresenter implements Presenter {

	public interface Display extends edu.ucsd.cs.palmscom.client.presenter.Display {
		HasClickHandlers getToggleButton();
//	    HasClickHandlers getReplyButton();
//	    int getClickedRow(ClickEvent event);
//	    HasClickHandlers getDeleteButton();
//	    HasClickHandlers getList();
	}
	
	private final ServiceProxy service;
	private final HandlerManager eventBus;
	private final Display view;
	
	public CollapsedPresenter(ServiceProxy service, HandlerManager eventBus, Display view) {
		this.service = service;
		this.eventBus = eventBus;
		this.view = view;
	}
	
	private void bind() {
		view.getToggleButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new ToggleButtonClickedEvent());
			}
		});
	}

	@Override
	public void go(HasWidgets container) {
		// TODO Auto-generated method stub

	}

}
