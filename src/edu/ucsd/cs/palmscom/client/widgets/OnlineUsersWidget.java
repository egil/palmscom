package edu.ucsd.cs.palmscom.client.widgets;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ScrollPanel;

import edu.ucsd.cs.palmscom.client.ClientServiceHandler;
import edu.ucsd.cs.palmscom.shared.User;

public class OnlineUsersWidget extends Composite {
	private final ClientServiceHandler svc;	
	private final ScrollPanel onlineUsersList = new ScrollPanel();
	private final FlowPanel panel = new FlowPanel();
	
	public OnlineUsersWidget(ClientServiceHandler svc){
		this.svc = svc;
		
		// Sets the widget to be wrapped by the composite. 		
		initWidget(panel);						
		
		// configure panel
		panel.setStylePrimaryName("online-users");
	
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
		
		HTML summary = new HTML();
		summary.setHTML("Users online (" + usrs + ") Supporters online (" + (adms + sups) + ")");
		panel.add(summary);
		
//		Element admins = Document.get().createULElement();		
//		Element supporters = Document.get().createULElement();
//		Element users = Document.get().createULElement();
//				
//		for(User user : onlineUsers) {
//			Element li = Document.get().createLIElement();
//			li.addClassName(user.getType().toString().toLowerCase());			
//			switch (user.getType()) {
//				case ADMIN:
//					admins.appendChild(li);
//					li.setInnerText(user.getNickname() + " (" + user.getType().toString().toLowerCase() + ")");
//					break;
//				case SUPPORTER:
//					supporters.appendChild(li);
//					li.setInnerText(user.getNickname() + " (" + user.getType().toString().toLowerCase() + ")");
//					break;
//				case USER:
//					users.appendChild(li);				
//					li.setInnerText(user.getNickname());
//					break;
//			}			
//		}
//		// remove existing user list
//		onlineUsersPanel.clear();
//		// add new user lists
//		onlineUsersPanel.getElement().appendChild(admins);
//		onlineUsersPanel.getElement().appendChild(supporters);
//		onlineUsersPanel.getElement().appendChild(users);		
	}
}
