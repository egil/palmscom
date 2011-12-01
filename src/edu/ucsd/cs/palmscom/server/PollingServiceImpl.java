package edu.ucsd.cs.palmscom.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.http.HttpSession;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.ucsd.cs.palmscom.shared.Message;
import edu.ucsd.cs.palmscom.shared.PalmscomService;
import edu.ucsd.cs.palmscom.shared.Settings;
import edu.ucsd.cs.palmscom.shared.User;
import edu.ucsd.cs.palmscom.shared.UserType;

public class PollingServiceImpl extends RemoteServiceServlet implements PalmscomService {
	private static final long serialVersionUID = -3337342865130794732L;
	private ArrayList<Message> messages = new ArrayList<Message>();
	private ArrayList<User> users = new ArrayList<User>();
	private int userCounter = 0;
	
	private User getSessionUser() {
		HttpSession session = getThreadLocalRequest().getSession(true);

		if(session.getAttribute("user") == null) {
			session.setAttribute("user", users.get(userCounter));
			userCounter = (userCounter + 1) % 2;		
		}
		
		return (User)session.getAttribute("user");
	}
	
	public PollingServiceImpl() { 
		User adm = new User("Barry");
		adm.setType(UserType.ADMIN);
		users.add(adm);
		
		User sup = new User("Suni");
		sup.setType(UserType.SUPPORTER);
		users.add(sup);
		
		User homer = new User("Egil");
		users.add(homer);
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
	public void sendMessage(Message msg) {
		// get real user name from logged in user
		
		msg.setDate(new Date());
		msg.setID(messages.size());
		msg.setAuthor(getSessionUser());
		
		messages.add(msg);		
	}

	@Override
	public List<Message> getMessages(int limit) {
		ArrayList<Message> res = new ArrayList<Message>();
		int from = limit < messages.size() ? limit : 0;
		int to = messages.size();
		res.addAll(messages.subList(from, to));
		return res;
	}

	@Override
	public List<Message> getMessages(Date to) {
		ArrayList<Message> res = new ArrayList<Message>();
		int too = indexOfLargerOrEqual(to);
		if(too >= 0){
			res.addAll(messages.subList(too, messages.size()));
		}
		
		return res;
	}

	@Override
	public List<Message> getMessages(Date from, Date to) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Message> search(String query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> getOnlineUsers() {		
		return users;
	}

	@Override
	public Settings getUserSettings() {
		Settings s = new Settings();
		
		s.setCurrentUser(getSessionUser());
		
		if(s.getCurrentUser().getType() != UserType.USER) {
			s.getKeywords().add("PALMS");
			s.getKeywords().add("Dataset");
			s.getKeywords().add("help");
		}
		
		return s;
	}

	@Override
	public void saveUserSettings(Settings settings) {
		// TODO Auto-generated method stub
		
	}

}
