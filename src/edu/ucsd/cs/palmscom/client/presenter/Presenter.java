package edu.ucsd.cs.palmscom.client.presenter;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;

import edu.ucsd.cs.palmscom.client.ServiceProxy;

public abstract class Presenter<T extends edu.ucsd.cs.palmscom.client.presenter.Display> {

	protected final ServiceProxy service;
	protected final HandlerManager eventBus;
	protected final HasWidgets container;
	protected T view;
	protected boolean isVisible;
	
	public Presenter(ServiceProxy service, HandlerManager eventBus, T view, HasWidgets container) {
		this.service = service;
		this.eventBus = eventBus;
		this.view = view;
		this.container = container;
		bind();
		fetchData();
	}
	
	public void setVisible(boolean visible) {
		if(!isVisible() && visible) {
			container.clear();
			container.add(view.asWidget());
		}
		this.isVisible = visible;
	}
	
	public boolean isVisible() {
		return this.isVisible;
	}

	protected abstract void bind();
	protected abstract void fetchData();
}
