package edu.ucsd.cs.palmscom.shared;

import java.util.Date;

public class Message {
	private int ID;
	private String text;
	private Date date;
	private User author;
	private Boolean isMessageOfIntrest;
		
	public Message(){ }
	public Message(String text) {
		this.setText(text);
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public int getID() {
		return ID;
	}
	
	public void setID(int iD) {
		ID = iD;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public User getAuthor() {
		return author;
	}
	
	public void setAuthor(User author) {
		this.author = author;
	}
	
	public Boolean getIsMessageOfIntrest() {
		return isMessageOfIntrest;
	}
	
	public void setIsMessageOfIntrest(Boolean isMessageOfIntrest) {
		this.isMessageOfIntrest = isMessageOfIntrest;
	}
	
	public String getHtmlID() {
		return "id-" + this.getID();
	}
}
