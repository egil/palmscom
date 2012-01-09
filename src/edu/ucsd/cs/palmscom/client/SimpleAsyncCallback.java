package edu.ucsd.cs.palmscom.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class SimpleAsyncCallback<T> implements AsyncCallback<T> {

	@Override
	public void onFailure(Throwable caught) {
		GWT.log("Error in async call.", caught);
	}
}
