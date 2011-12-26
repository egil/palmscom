package edu.ucsd.cs.palmscom.client;

import edu.ucsd.cs.palmscom.shared.Settings;
import edu.ucsd.cs.palmscom.shared.User;

/*
 * AppState is implemented using the Singleton
 * pattern to prevent multiple instances
 */
public class AppState {
	private static final AppState instance = new AppState();
	private User user;
	private Settings settings;
	
    // Private constructor prevents instantiation from other classes
    private AppState() { }
    
    public static AppState getInstance() {
    	return instance;
    }

    public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}
}
