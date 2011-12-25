package edu.ucsd.cs.palmscom. demo;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;

import edu.ucsd.cs.palmscom.client.widgets.Palmscom;
import edu.ucsd.cs.palmscom.client.widgets.WatermarkTextBox;
import edu.ucsd.cs.palmscom.shared.User;
import edu.ucsd.cs.palmscom.shared.UserType;

public class DemoApp implements EntryPoint {
	
	@Override
	public void onModuleLoad() {
		
		FlowPanel palms = new FlowPanel();
		final WatermarkTextBox userName = new WatermarkTextBox();
		userName.setWatermark("User Name");
		
		final WatermarkTextBox fullName = new WatermarkTextBox();
		fullName.setWatermark("Full Name");
		
		Button login = new Button();
		login.setText("Login to PALMSCom");
		
		login.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// 1150 + 315 = width of palms + width of palmscom
				int collapsPoint = 1150 + 315;
				// Init a user object based on current PALMS user
				User user = new User();
				user.setFullname(fullName.getText());
				user.setUsername(userName.getText());
				// if admin
				//user.setType(UserType.ADMIN);
				
				Palmscom palmscom = new Palmscom(user, collapsPoint);
				palmscom.addStyleDependentName("horizontal");
//				palmscom.addStyleDependentName("vertical");
				RootPanel.get().add(palmscom);		
			}
		});
		
		palms.setStylePrimaryName("palms");		
		RootPanel.get().add(palms);
		FlowPanel loginPanel = new FlowPanel();
		loginPanel.setStylePrimaryName("login");
		palms.add(loginPanel);
		loginPanel.add(userName);
		loginPanel.add(fullName);
		loginPanel.add(login);
		
	}

}
