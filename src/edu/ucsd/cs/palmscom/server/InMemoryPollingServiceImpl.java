package edu.ucsd.cs.palmscom.server;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.ucsd.cs.palmscom.shared.Message;
import edu.ucsd.cs.palmscom.shared.MessageCache;
import edu.ucsd.cs.palmscom.shared.PalmscomService;
import edu.ucsd.cs.palmscom.shared.Settings;
import edu.ucsd.cs.palmscom.shared.User;

public class InMemoryPollingServiceImpl extends RemoteServiceServlet implements PalmscomService {
	private static final long serialVersionUID = -3337342865130794732L;
	private final MessageCache<Message> messageCache = new MessageCache<Message>(); 
	private ConcurrentHashMap<String, User> users = new ConcurrentHashMap<String, User>();
	
	private User getSessionUser() {
		HttpSession session = getThreadLocalRequest().getSession();
		String username = session.getAttribute("username").toString(); 
		return users.get(username);
	}
	
	public InMemoryPollingServiceImpl() { 
	}
	
	@Override
	public void createMessage(Message msg) {
		msg.setDate(new Date());
		msg.setID(messageCache.size());
		msg.setAuthor(getSessionUser());		
		messageCache.add(msg);
	}

	@Override
	public Message[] getMessages(int limit) {
		return messageCache.getTo(limit);
	}

	@Override
	public Message[] getMessagesFrom(Date from, int limit) {
		return messageCache.getFrom(from, limit);
	}
	
	@Override
	public Message[] getMessagesTo(Date to) {
		return messageCache.getTo(to);
	}

	
	@Override
	public User[] getOnlineUsers() {
		return users.values().toArray(new User[0]);
	}

//	@Override
//	public Settings getUserSettings() {
//		Settings s = new Settings();
//		s.setCurrentUser(getSessionUser());
//		return s;
//	}
//
//	@Override
//	public void updateUserSettings(Settings settings) {
//		// TODO Auto-generated method stub
//	}

	@Override
	public Settings signIn(User user) {
		HttpSession session = getThreadLocalRequest().getSession(true);

		if(session.getAttribute("username") == null) {
			session.setAttribute("username", user.getUsername());
			users.put(user.getUsername(), user);
		}
		
		return new Settings();
	}

	@Override
	public void signOut() {
		HttpSession session = getThreadLocalRequest().getSession();
		if(session.getAttribute("username") == null) {
			users.remove(session.getAttribute("username"));
		}
	}

}