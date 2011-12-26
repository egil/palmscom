package edu.ucsd.cs.palmscom.shared;

import java.util.Date;

public abstract class MessageDecorator extends MessageImpl implements Message {
	private static final long serialVersionUID = -5959028169195178557L;
	protected Message decoratedMessage;
	
	public MessageDecorator(Message message) {
		this.decoratedMessage = message;
	}
	
	@Override
	public int getID() {
		return decoratedMessage.getID();
	}

	@Override
	public void setID(int id) {
		decoratedMessage.setID(id);
	}

	@Override
	public Date getDate() {
		return decoratedMessage.getDate();
	}

	@Override
	public void setDate(Date date) {
		decoratedMessage.setDate(date);
	}

	@Override
	public User getAuthor() {
		return decoratedMessage.getAuthor();
	}

	@Override
	public void setAuthor(User author) {
		decoratedMessage.setAuthor(author);
	}

	@Override
	public String getText() {
		return decoratedMessage.getText();
	}

	@Override
	public void setText(String text) {
		decoratedMessage.setText(text);
	}

	@Override
	public Boolean getIsMessageOfIntrest() {
		return decoratedMessage.getIsMessageOfIntrest();
	}

	@Override
	public void setIsMessageOfIntrest(Boolean isMessageOfIntrest) {
		decoratedMessage.setIsMessageOfIntrest(isMessageOfIntrest);
	}

}
