package edu.ucsd.cs.palmscom.shared;

import java.util.Date;

public class MessageImpl implements Message {
	private static final long serialVersionUID = 3625673327410170481L;
	private int id;
	private Date date;
	private User author;
	private String text;
	private Boolean moi;
	
	@Override
	public int getID() {
		return this.id;
	}

	@Override
	public void setID(int id) {
		this.id = id;
	}

	@Override
	public Date getDate() {
		return this.date;
	}

	@Override
	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public User getAuthor() {
		return this.author;
	}

	@Override
	public void setAuthor(User author) {
		this.author = author;
	}

	@Override
	public String getText() {
		return this.text;
	}

	@Override
	public void setText(String text) {
		this.text = text;
	}

	@Override
	public Boolean isMessageOfIntrest() {
		return this.moi;
	}

	@Override
	public void isMessageOfIntrest(Boolean isMessageOfIntrest) {	
		this.moi = isMessageOfIntrest;
	}

	@Override
	public int compareTo(Message other) {
		return this.getDate().compareTo(other.getDate());
	}
}
