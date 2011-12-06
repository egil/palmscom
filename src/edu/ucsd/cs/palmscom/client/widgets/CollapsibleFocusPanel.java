package edu.ucsd.cs.palmscom.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowResizeListener;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.ProvidesResize;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;

import edu.ucsd.cs.palmscom.client.VisualStateType;
import edu.ucsd.cs.palmscom.client.events.VisualStateChangeEvent;

@SuppressWarnings("deprecation")
public class CollapsibleFocusPanel extends FocusPanel implements RequiresResize, ProvidesResize {	
	private VisualStateType state;
	private int collapsPoint;
	private String collapsedStylePrimaryName = "collapsed";
	private Boolean lastOverCollapsPoint;
	
	public CollapsibleFocusPanel(int collapsPoint) {
		this(collapsPoint, null);
	}

	public CollapsibleFocusPanel(int collapsPoint, Widget child) {
		super(child);
		this.collapsPoint = collapsPoint;
		
		// if the parent class does not support ProvidesResize
		// we have to detect window resizing in the old fashion way
		if(!(getParent() instanceof ProvidesResize)) {
			Window.addWindowResizeListener(new WindowResizeListener() {					
				@Override
				public void onWindowResized(int width, int height) {
					handleResize();
				}
			});
		}
	}

	@Override
	public void onLoad(){ 
		super.onLoad();
		state = VisualStateType.EXPANDED;
		handleResize();		
	}

	public void toggleVisualState() {		
		setVisualState(this.state == VisualStateType.COLLAPSED ? VisualStateType.EXPANDED : VisualStateType.COLLAPSED);
	}
	
	public VisualStateType getVisualState() {
		return state;
	}
	
	public void setVisualState(VisualStateType state){
		this.state = state;
		setStyleName(collapsedStylePrimaryName, this.state == VisualStateType.COLLAPSED);
		fireEvent(new VisualStateChangeEvent(this.state));
	}
	
	private void handleResize() {
		boolean overCollapsPoint = Window.getClientWidth() < collapsPoint; 
		
		if(lastOverCollapsPoint == null || overCollapsPoint != lastOverCollapsPoint) {			
			if(overCollapsPoint && state == VisualStateType.EXPANDED) {
				setVisualState(VisualStateType.COLLAPSED);
			} else if(!overCollapsPoint && state == VisualStateType.COLLAPSED) {				
				setVisualState(VisualStateType.EXPANDED);
			}				
		}		
		
		lastOverCollapsPoint = overCollapsPoint;
	}
	
	@Override
	public void onResize() {
		handleResize();
		if (getWidget() instanceof RequiresResize) {
			((RequiresResize)getWidget()).onResize();
		}
	}

	public void setStyleCollapsedPrimaryName(String style) {
		this.collapsedStylePrimaryName = style;		
	}

}