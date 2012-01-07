package edu.ucsd.cs.palmscom.client.view;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import edu.ucsd.cs.palmscom.client.presenter.Display;

public abstract class View extends Composite implements Display {
	public Widget asWidget() {
		return this;
	}
}
