package edu.ucsd.cs.palmscom.client;

import java.util.List;

import edu.ucsd.cs.palmscom.shared.Message;
import edu.ucsd.cs.palmscom.shared.User;

public interface ClientServiceCallback {
	void onLiveMessages(List<Message> results);
	void onLiveOnlineUserList(List<User> results);
}
