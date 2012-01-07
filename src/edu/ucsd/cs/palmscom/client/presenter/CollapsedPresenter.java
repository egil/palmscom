package edu.ucsd.cs.palmscom.client.presenter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import edu.ucsd.cs.palmscom.client.AppState;
import edu.ucsd.cs.palmscom.client.ServiceProxy;
import edu.ucsd.cs.palmscom.client.event.ToggleButtonClickedEvent;
import edu.ucsd.cs.palmscom.client.presenter.ExpandedPresenter.Display;
import edu.ucsd.cs.palmscom.client.view.CollapsedView;
import edu.ucsd.cs.palmscom.shared.PalmscomServiceAsync;
import edu.ucsd.cs.palmscom.widgets.CollapsiblePanel.CollapseState;

public class CollapsedPresenter extends Presenter<CollapsedPresenter.Display> {
	
	public interface Display extends edu.ucsd.cs.palmscom.client.presenter.Display {
		HasClickHandlers getToggleButton();
		void setContent(String text);
//	    HasClickHandlers getReplyButton();
//	    int getClickedRow(ClickEvent event);
//	    HasClickHandlers getDeleteButton();
//	    HasClickHandlers getList();
	}

	public CollapsedPresenter(ServiceProxy service, HandlerManager eventBus,
			Display view, HasWidgets container) {
		super(service, eventBus, view, container);
		// TODO Auto-generated constructor stub
	}
	
	protected void bind() {
		view.getToggleButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new ToggleButtonClickedEvent());
			}
		});
	}

	@Override
	protected void fetchData() {
		// TODO Auto-generated method stub
		
	}
}
