package edu.ucsd.cs.palmscom.client.view;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;

import edu.ucsd.cs.palmscom.client.presenter.CollapsedPresenter;

public class CollapsedView extends View implements CollapsedPresenter.Display {

	private final FlowPanel header = new FlowPanel();
	private final HTML toggler = new HTML();
	private final HTML content = new HTML();
	
	public CollapsedView() {
		initWidget(header);
		
		// configure and setup
		header.setStylePrimaryName("header");
		toggler.setStylePrimaryName("toggler");
		header.add(toggler);
	}

	@Override
	public HasClickHandlers getToggleButton() {
		return toggler;
	}

	@Override
	public void setContent(String text) {
		// TODO Auto-generated method stub
		
	}

}
