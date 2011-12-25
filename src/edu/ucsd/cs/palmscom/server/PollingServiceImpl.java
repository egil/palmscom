package edu.ucsd.cs.palmscom.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.ucsd.cs.palmscom.shared.Message;
import edu.ucsd.cs.palmscom.shared.PalmscomService;
import edu.ucsd.cs.palmscom.shared.Settings;
import edu.ucsd.cs.palmscom.shared.User;

public class PollingServiceImpl extends RemoteServiceServlet implements PalmscomService {
	private static final long serialVersionUID = -3337342865130794732L;
	private ArrayList<Message> messages = new ArrayList<Message>();
	private ConcurrentHashMap<String, User> users = new ConcurrentHashMap<String, User>();
	
	private User getSessionUser() {
		HttpSession session = getThreadLocalRequest().getSession();
		String username = session.getAttribute("username").toString(); 
		return users.get(username);
	}
	
	public PollingServiceImpl() { 
	}
	
	// assumes the messages list is sorted
	private int indexOfLargerOrEqual(Date date) {				
		if(messages.size() == 0)
			return -1;
		
		int res = -1;
		
		
		for(int i = 0; i < messages.size(); i++) {
			if(messages.get(i).getDate().compareTo(date) > 0){
				res = i;
				break;
			}
		}
		
		return res;
	}	
	
	@Override
	public void createMessage(Message msg) {
		msg.setDate(new Date());
		msg.setID(messages.size());
		msg.setAuthor(getSessionUser());		
		messages.add(msg);
	}

	@Override
	public Message[] getMessages(int limit) {
		ArrayList<Message> res = new ArrayList<Message>();
		int from = limit < messages.size() ? limit : 0;
		int to = messages.size();
		res.addAll(messages.subList(from, to));
		return res.toArray(new Message[0]);
	}

	@Override
	public Message[] getMessages(Date to) {
		ArrayList<Message> res = new ArrayList<Message>();
		int too = indexOfLargerOrEqual(to);
		if(too >= 0){
			res.addAll(messages.subList(too, messages.size()));
		}
		
		return res.toArray(new Message[0]);
	}
	
	@Override
	public User[] getOnlineUsers() {
		return users.values().toArray(new User[0]);
	}

	@Override
	public Settings getUserSettings() {
		Settings s = new Settings();
		s.setCurrentUser(getSessionUser());
		return s;
	}

	@Override
	public void updateUserSettings(Settings settings) {
		// TODO Auto-generated method stub
	}

	@Override
	public Settings signIn(User user) {
		HttpSession session = getThreadLocalRequest().getSession(true);

		if(session.getAttribute("username") == null) {
			session.setAttribute("username", user.getUsername());
			users.put(user.getUsername(), user);
		}
		
		return getUserSettings();
	}

	@Override
	public void singOut() {
		HttpSession session = getThreadLocalRequest().getSession();
		if(session.getAttribute("username") == null) {
			users.remove(session.getAttribute("username"));
		}
	}
}
