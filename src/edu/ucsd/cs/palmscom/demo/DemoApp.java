package edu.ucsd.cs.palmscom.demo;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;

import edu.ucsd.cs.palmscom.client.widgets.Palmscom;

public class DemoApp implements EntryPoint {
	
	@Override
	public void onModuleLoad() {
		
		FlowPanel palms = new FlowPanel();
		palms.setStylePrimaryName("palms");		
		RootPanel.get().add(palms);
		
		Palmscom palmscom = new Palmscom();
		palmscom.setStylePrimaryName("palmscom");
		palmscom.addStyleDependentName("horizontal");
//		palmscom.addStyleDependentName("vertical");
		RootPanel.get().add(palmscom);		
	}
}
