package edu.ucsd.cs.palmscom.client;

import edu.ucsd.cs.palmscom.shared.Message;

public interface ClientServiceCallback {
	void messages(Message[] result);	
}
