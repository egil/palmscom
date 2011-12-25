package edu.ucsd.cs.palmscom.shared;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("service")
public interface PalmscomService extends RemoteService {
	public Settings signIn(User user);
	public void singOut();
	
	/**
	 * Sends a new message to the server
	 * @param msg
	 */
	public void sendMessage(Message msg);
		
	/**
	 * Get the latest message from the server,
	 * limited by the limit param.
	 * @param limit Maximum amount of message to download
	 * @return
	 */
	public List<Message> getMessages(int limit);
	
	/**
	 * Get all messages from server that are newer
	 * than the date specified in the "to" param. 
	 * @param to Messages newer than this date
	 * @return
	 */
	public List<Message> getMessages(Date to);
	
	/**
	 * Get messages from server that are in the 
	 * date range between "from" and "to" param.
	 * @param to Messages newer than this date
	 * @return
	 */
	public List<Message> getMessages(Date from, Date to);
	
	/**
	 * Get all messages that contains the query
	 * @param query
	 * @return
	 */
	public List<Message> search(String query);
	
	/**
	 * Get a list of all online users
	 * @return
	 */
	public User[] getOnlineUsers();
	
	/**
	 * Get a dictionary of user settings associated
	 * with the current logged in user.
	 * @return
	 */
	public Settings getUserSettings();
	
	/**
	 * Save the current user's settings.
	 * @param settings
	 */
	public void saveUserSettings(Settings settings);
}
