package edu.ucsd.cs.palmscom.client.widgets;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;


public class HeaderWidget extends Composite {
	private FlowPanel wrapper = new FlowPanel();
	private SimplePanel content = new SimplePanel();
	private FlowPanel toggler = new FlowPanel();
	
	public HeaderWidget() {
		initWidget(wrapper);
		content.setStylePrimaryName("content");
		toggler.setStylePrimaryName("toggler");
		wrapper.add(content);
		wrapper.add(toggler);
	}
	
	public void set(Widget widget) {
		content.setWidget(widget);
	}
		
	public void addClickHandler(ClickHandler handler) {
		toggler.addDomHandler(handler, ClickEvent.getType());		
	}
}
