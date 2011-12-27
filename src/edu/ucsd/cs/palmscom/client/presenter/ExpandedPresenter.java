package edu.ucsd.cs.palmscom.client.presenter;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import edu.ucsd.cs.palmscom.client.ServiceProxy;
import edu.ucsd.cs.palmscom.client.event.ToggleButtonClickedEvent;
import edu.ucsd.cs.palmscom.client.presenter.ExpandedPresenter.Display;
import edu.ucsd.cs.palmscom.client.view.ExpandedView;
import edu.ucsd.cs.palmscom.shared.PalmscomServiceAsync;

public class ExpandedPresenter implements Presenter {

	public interface Display extends edu.ucsd.cs.palmscom.client.presenter.Display {
		HasClickHandlers getToggleButton();
//	    HasClickHandlers getReplyButton();
//	    int getClickedRow(ClickEvent event);
//	    HasClickHandlers getDeleteButton();
//	    HasClickHandlers getList();
	    
//	    List<Integer> getSelectedRows();
	}
	
	private final ServiceProxy service;
	private final HandlerManager eventBus;
	private final Display view;
	
	public ExpandedPresenter(ServiceProxy service, HandlerManager eventBus, Display view) {
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
