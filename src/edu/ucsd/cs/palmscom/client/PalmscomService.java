package edu.ucsd.cs.palmscom.client;

import java.util.Date;
import java.util.List;

import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.ucsd.cs.palmscom.shared.Message;
import edu.ucsd.cs.palmscom.shared.ServiceException;
import edu.ucsd.cs.palmscom.shared.User;

public interface ClientServiceHandler {
	/**
	 * Sends a new message to the server
	 * @param msg
	 */
	public void sendMessage(Message msg, AsyncCallback<Void> callback);
	
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
	public void getMessages(int limit, AsyncCallback<List<Message>> callback);
	
	/**
	 * Get all messages from server that are newer
	 * than the date specified in the "to" param. 
	 * @param to Messages newer than this date
	 * @return
	 */
	public void getMessages(Date to, AsyncCallback<List<Message>> callback);
	
	/**
	 * Get messages from server that are in the 
	 * date range between "from" and "to" param.
	 * @param to Messages newer than this date
	 * @return
	 */
	public void getMessages(Date from, Date to, AsyncCallback<List<Message>> callback);
	
	/**
	 * Get all messages that contains the query
	 * @param query
	 * @return
	 */
	public void search(String query, AsyncCallback<List<Message>> callback);
	
	/**
	 * Get a list of all online users
	 * @return
	 */
	public void getOnlineUsers(AsyncCallback<List<User>> callback);
	
	/**
	 * Get a dictionary of user settings associated
	 * with the current logged in user.
	 * @return
	 */
	public void getUserSettings(AsyncCallback<Dictionary> callback);
	
	/**
	 * Save the current user's settings.
	 * @param settings
	 */
	public void saveUserSettings(Dictionary settings, AsyncCallback<Void> callback);
}
