package edu.ucsd.cs.palmscom.client;

import edu.ucsd.cs.palmscom.shared.PalmscomServiceAsync;

public interface ClientServiceHandler extends PalmscomServiceAsync {
		
	/**
	 * Subscribes to new messages, etc., from the server
	 * @param callback
	 */
	public void subscribe(ClientServiceCallback callback);
	
}
