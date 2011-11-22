package edu.ucsd.cs.palmscom.client;

import java.util.Date;
import java.util.Dictionary;
import java.util.List;

import edu.ucsd.cs.palmscom.shared.Message;
import edu.ucsd.cs.palmscom.shared.ServiceException;
import edu.ucsd.cs.palmscom.shared.User;

public interface ClientServiceHandler {
	/**
	 * Sends a new message to the server
	 * @param msg
	 * @throws ServiceException
	 */
	public void sendMessage(Message msg) throws ServiceException;
	
	/**
	 * Subscribes to new messages, etc., from the server
	 * @param callback
	 */
	public void subscribe(ClientServiceCallback callback);
	
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
	public List<User> getOnlineUsers();
	
	/**
	 * Get a dictionary of user settings associated
	 * with the current logged in user.
	 * @return
	 */
	public Dictionary<String, String> getUserSettings();
	
	/**
	 * Save the current user's settings.
	 * @param settings
	 * @throws ServiceException
	 */
	public void saveUserSettings(Dictionary<String, String> settings) throws ServiceException;
}
