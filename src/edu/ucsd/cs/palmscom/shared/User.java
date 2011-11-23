package edu.ucsd.cs.palmscom.shared;

import java.io.Serializable;

public class User implements Serializable {
	private static final long serialVersionUID = 1622463170753612691L;
	private String nickname;
	private UserType type = UserType.USER;
	
	public User() { }
	
	public User(String nickname) {
		this.nickname = nickname;
	}
	
	public String getNickname() {
		return nickname;
	}
	
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	public UserType getType() {
		return type;
	}
	
	public void setType(UserType type) {
		this.type = type;
	}
	
}

