package edu.ucsd.cs.palmscom.shared;

import java.io.Serializable;

public class User implements Serializable, Comparable<User> {
	private static final long serialVersionUID = 1622463170753612691L;
	private String fullname;
	private String username;
	private UserType type = UserType.USER;
	
	public User() { }
	
	public String getFullname() {
		return fullname;
	}
	
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	
	public UserType getType() {
		return type;
	}
	
	public void setType(UserType type) {
		this.type = type;
	}

	@Override
	public int compareTo(User o) {		
		return this.username.compareTo(o.username);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}

