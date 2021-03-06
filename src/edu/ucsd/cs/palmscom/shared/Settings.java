package edu.ucsd.cs.palmscom.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Settings implements Serializable {
	public enum CollapsedType {
		HORIZONTAL,
		VERTICAL
	}
	
	private static final long serialVersionUID = 2570513206625266081L;
	private User currentUser;
	private List<String> keywords;
	private CollapsedType collapsedType = CollapsedType.HORIZONTAL;
	
	public User getCurrentUser() {
		return currentUser;
	}
	
	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}
	
	public List<String> getKeywords() {
		if(keywords == null){
			keywords = new ArrayList<String>();
		}
		return keywords;
	}
	
	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	public CollapsedType getCollapsedType() {
		return collapsedType;
	}

	public void setCollapsedType(CollapsedType collapsedType) {
		this.collapsedType = collapsedType;
	}
}
