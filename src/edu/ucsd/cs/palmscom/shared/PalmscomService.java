package edu.ucsd.cs.palmscom.shared;

import java.util.Date;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("service")
public interface PalmscomService extends RemoteService {
	public Settings signIn(User user);
	public void singOut();
	
	public void createMessage(Message msg);
	public Message[] getMessages(int limit);
	public Message[] getMessages(Date to);
	
	public User[] getOnlineUsers();
	
	public Settings getUserSettings();
	public void updateUserSettings(Settings settings);
}