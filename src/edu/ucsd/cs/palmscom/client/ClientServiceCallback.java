package edu.ucsd.cs.palmscom.client;

import java.util.List;

import edu.ucsd.cs.palmscom.shared.Message;

public interface ClientServiceCallback {
	void onLiveMessages(List<Message> result);
}
