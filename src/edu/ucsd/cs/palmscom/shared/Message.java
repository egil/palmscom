package edu.ucsd.cs.palmscom.shared;

import java.io.Serializable;
import java.util.Date;

public interface Message extends Serializable, Comparable<Message> {	
	public int getID();
	public void setID(int iD);	
	public Date getDate();	
	public void setDate(Date date);
	public User getAuthor();
	public void setAuthor(User author);
	public String getText();
	public void setText(String text);
	public Boolean isMessageOfIntrest();
	public void isMessageOfIntrest(Boolean isMessageOfIntrest);
}
