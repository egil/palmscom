package edu.ucsd.cs.palmscom.shared;

import java.util.Date;
import java.util.LinkedList;
import java.util.Stack;

/*
 * Wrap message cache fields and methods
 * in a class to ensure proper ordering and access
 */
public class MessageCache<M extends Message> {
	/*
	 * Newest messages are stored at the front of the list. 
	 */
	private final LinkedList<M> messages = new LinkedList<M>();
	
	public void add(M message) {		
		// find first message that is older than msg
		int index = 0;
		for (; index < messages.size(); index++) {
			if(messages.get(index).compareTo(message) < 0)
				break;
		}
		messages.add(index, message);
	}
	
	public void add(M[] messages) {
		for (M message : messages) {
			add(message);
		}
	}
	
	public Message[] getTo(int limit) {
		limit = limit < messages.size() ? limit : messages.size();
		Message[] result = new Message[limit];
		
		for (int i = 0; i < result.length; i++) {
			result[i] = messages.get(i);
		}
		
		return result;
	}
	
	public M getLast() {
		return messages.getLast();
	}
	
	public Message[] getFrom(Date from, int limit) {
		// find first message that is older than "from"
		int startIndex = 0;
		for (; startIndex < messages.size(); startIndex++) {
			if(messages.get(startIndex).getDate().compareTo(from) > 0) break;
		}
		
		// if from was at the end of the message list, return an empty array
		if(startIndex == messages.size()) return new Message[0];
		
		// extract messages from list and return
		limit = messages.size() - startIndex < limit ? messages.size() - startIndex : limit;
		Message[] result = new Message[limit];
		for (int i = 0; i < result.length; i++, startIndex++) {
			result[i] = messages.get(startIndex);
		}

		return result;
	}

	public Message[] getTo(Date to) {
		Stack<M> result = new Stack<M>();

		for (M msg : messages) {
			if(msg.getDate().compareTo(to) > 0) {
				result.push(msg);
			}
			else {
				break;
			}
		}
	
		return result.toArray(new Message[0]);
	}
	
	public M getFirst() {
		return messages.getFirst();
	}

	public int size() {
		return messages.size();
	}

}