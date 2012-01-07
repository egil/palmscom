package edu.ucsd.cs.palmscom.widgets;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FlowPanel;

public class ClickFlowPanel extends FlowPanel implements HasClickHandlers {
	public HandlerRegistration addClickHandler(ClickHandler handler) 
    { 
        return addDomHandler(handler, ClickEvent.getType()); 
    }
} 