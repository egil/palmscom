package edu.ucsd.cs.palmscom.shared;

public class User {
	private String nickname;
	private UserType type;
	
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

