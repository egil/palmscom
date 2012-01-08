package edu.ucsd.cs.palmscom.shared;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import com.google.gwt.core.client.GWT;

/*
 * Wrap message cache fields and methods
 * in a class to ensure proper ordering and access
 */
public class MessageCache<M extends Message> {
	/*
	 * Newest messages are stored at the front of the list. 
	 */
	private final LinkedList<M> messages = new LinkedList<M>();
	private final HashSet<Integer> existsLookup = new HashSet<Integer>(); 
	
	public void add(M message) {
		if(existsLookup.contains(message.getID())) {
			GWT.log("Message already exists: " + message.getID());
			return;
		}
		// find first message that is older than msg
		int index = 0;
		for (; index < messages.size(); index++) {
			if(messages.get(index).compareTo(message) < 0)
				break;
		}
		messages.add(index, message);
		existsLookup.add(message.getID());
	}
	
	public void add(List<M> messages) {
		// iterate from last to first, since the newest message 
		// is usually first and the oldest is last in the list
		for (int i = messages.size()-1; i >= 0; i--) {
			add(messages.get(i));
		}	
	}
	
	public List<M> getTo(int limit) {
		limit = limit < messages.size() ? limit : messages.size();
		ArrayList<M> result = new ArrayList<M>(limit);
		
		for (int i = 0; i < limit; i++) {
			result.add(messages.get(i));
		}
		
		return result;
	}
	
	public M getLast() {
		return messages.getLast();
	}
	
	public List<M> getFrom(Date from, int limit) {
		// find first message that is older than "from"
		int startIndex = 0;
		for (; startIndex < messages.size(); startIndex++) {
			if(messages.get(startIndex).getDate().compareTo(from) > 0) break;
		}
		
		// if from was at the end of the message list, return an empty array
		if(startIndex == messages.size()) return new ArrayList<M>(0);
		
		// extract messages from list and return
		limit = messages.size() - startIndex < limit ? messages.size() - startIndex : limit;
		List<M> result = new ArrayList<M>(limit);
		for (int i = 0; i < limit; i++, startIndex++) {
			result.add(messages.get(startIndex));
		}

		return result;
	}

	public List<M> getTo(Date to) {
		Stack<M> result = new Stack<M>();

		for (M msg : messages) {
			if(msg.getDate().compareTo(to) > 0) {
				result.push(msg);
			}
			else {
				break;
			}
		}
	
		return result;
	}
	
	public M getFirst() {
		return messages.getFirst();
	}

	public int size() {
		return messages.size();
	}

}