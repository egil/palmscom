package edu.ucsd.cs.palmscom.client.widgets;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ScrollPanel;

import edu.ucsd.cs.palmscom.client.ClientServiceProxy;
import edu.ucsd.cs.palmscom.client.Collapsible;
import edu.ucsd.cs.palmscom.client.VisualStateType;
import edu.ucsd.cs.palmscom.client.events.VisualStateChangeEvent;
import edu.ucsd.cs.palmscom.client.events.VisualStateChangeHandler;
import edu.ucsd.cs.palmscom.shared.User;

public class OnlineUsersWidget extends Composite implements Collapsible {
	private static final double COLLAPSED_SIZE = 30;
	private VisualStateType state = VisualStateType.COLLAPSED;

	private int onlineUserCount = 0;
	private final HandlerManager handlerManager = new HandlerManager(this);
	private final ClientServiceProxy svc;	
	private final ScrollPanel onlineUsersList = new ScrollPanel();
	private final HTML summary = new HTML();
	private final FlowPanel panel = new FlowPanel();
	private final FocusPanel fpanel = new FocusPanel();
	
	public OnlineUsersWidget(ClientServiceProxy svc){
		this.svc = svc;
		
		// Sets the widget to be wrapped by the composite. 		
		initWidget(fpanel);						
		
		// configure panel
		fpanel.setStylePrimaryName("online-users");		
	
		// add content to parents
		fpanel.add(panel);
		panel.add(summary);
		panel.add(onlineUsersList);
		onlineUsersList.setVisible(false);
		
		// set handler
		fpanel.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// update state
				state = state == VisualStateType.COLLAPSED ? VisualStateType.EXPANDED : VisualStateType.COLLAPSED;
				
				// show full user list
				onlineUsersList.setVisible(true);
				
				handlerManager.fireEvent(new VisualStateChangeEvent(state));				
			}
		});		
		
		// get online users list
		getOnlineUsers();
	}

	private void getOnlineUsers() {
		svc.getOnlineUsers(new AsyncCallback<List<User>>() {				
			@Override
			public void onSuccess(List<User> results) {
				updateUserList(results);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Show error message to user
				GWT.log("ERROR (OnlineUsersWidget->getOnlineUsers): " + caught.getMessage());
			}
		});
	}
		
	private void updateUserList(List<User> onlineUsers) {	
		int adms = 0, sups = 0, usrs = 0;
		
		for(User u : onlineUsers) {
			switch (u.getType()) {
			case ADMIN:
				adms++;
				break;
			case SUPPORTER:
				sups++;
				break;
			case USER:
				usrs++;
				break;
			}
		}
				
		summary.setHTML("Users online (" + usrs + ") Supporters online (" + (adms + sups) + ")");
				
		Element admins = Document.get().createULElement();		
		Element supporters = Document.get().createULElement();
		Element users = Document.get().createULElement();
				
		for(User user : onlineUsers) {
			Element li = Document.get().createLIElement();
			li.addClassName(user.getType().toString().toLowerCase());			
			switch (user.getType()) {
				case ADMIN:
					admins.appendChild(li);
					li.setInnerText(user.getNickname() + " (" + user.getType().toString().toLowerCase() + ")");
					break;
				case SUPPORTER:
					supporters.appendChild(li);
					li.setInnerText(user.getNickname() + " (" + user.getType().toString().toLowerCase() + ")");
					break;
				case USER:
					users.appendChild(li);				
					li.setInnerText(user.getNickname());
					break;
			}			
		}
		// remove existing user list
		onlineUsersList.clear();
		// add new user lists
		onlineUsersList.getElement().appendChild(admins);
		onlineUsersList.getElement().appendChild(supporters);
		onlineUsersList.getElement().appendChild(users);
		
		// update user count and trigger visual change notification
		onlineUserCount = onlineUsers.size();
		handlerManager.fireEvent(new VisualStateChangeEvent(state));
	}
	
	public void addStateChangeHandler(VisualStateChangeHandler handler) {
		handlerManager.addHandler(VisualStateChangeEvent.getType(), handler);  
	}

	@Override
	public double getHeight() {
		if(state == VisualStateType.COLLAPSED)
			return COLLAPSED_SIZE;		
		double res = onlineUserCount * 10 + COLLAPSED_SIZE + 25; // 10px = line height		
		return res;
	}

	@Override
	public double getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}
}
