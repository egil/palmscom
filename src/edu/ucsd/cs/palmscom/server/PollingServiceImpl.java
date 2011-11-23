package edu.ucsd.cs.palmscom.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.ucsd.cs.palmscom.shared.Message;
import edu.ucsd.cs.palmscom.shared.PalmscomService;
import edu.ucsd.cs.palmscom.shared.User;
import edu.ucsd.cs.palmscom.shared.UserType;

public class PollingServiceImpl extends RemoteServiceServlet implements PalmscomService {
	private static final long serialVersionUID = -3337342865130794732L;
	private static ArrayList<Message> messages = new ArrayList<Message>();
	private static ArrayList<User> users = new ArrayList<User>();
	
	public PollingServiceImpl() { 
		User adm = new User("Barry");
		adm.setType(UserType.ADMIN);
		users.add(adm);
		
		User sup = new User("Suneeth");
		sup.setType(UserType.SUPPORTER);
		users.add(sup);
		
		User homer = new User("Homer");
		users.add(homer);
	}
	
	// assumes the messages list is sorted
	private static int indexOfLargerOrEqual(Date date) {				
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
		User currentUser = new User();
		currentUser.setNickname("Homer");
		
		msg.setDate(new Date());
		msg.setID(messages.size());
		msg.setAuthor(currentUser);
		
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

//	@Override
//	public Dictionary getUserSettings() {
//		Dictionary res = Dictionary.getDictionary("settings");
//		
//		return res;
//	}
//
//	@Override
//	public void saveUserSettings(Dictionary settings) {
//		// TODO Auto-generated method stub
//		
//	}

}
