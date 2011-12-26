package edu.ucsd.cs.widgets;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowResizeListener;
import com.google.gwt.user.client.ui.ProvidesResize;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.SimplePanel;

@SuppressWarnings("deprecation")
public class CollapsiblePanel extends SimplePanel implements RequiresResize, ProvidesResize {	
	
	public enum CollapseState {
		COLLAPSED,
		EXPANDED
	}
	
	private CollapseState state;
	private int collapsPoint;
	private String collapsedStylePrimaryName = "collapsed";
	private Boolean lastOverCollapsPoint;

	public CollapsiblePanel() {
		this(0);
	}
	
	public CollapsiblePanel(int collapsPoint) {
		this.setCollapsPoint(collapsPoint);
		
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
		state = CollapseState.EXPANDED;
		handleResize();		
	}

	public void toggleVisualState() {		
		setVisualState(this.state == CollapseState.COLLAPSED ? CollapseState.EXPANDED : CollapseState.COLLAPSED);
	}
	
	public CollapseState getVisualState() {
		return state;
	}
	
	public void setVisualState(CollapseState state){
		this.state = state;
		setStyleName(collapsedStylePrimaryName, this.state == CollapseState.COLLAPSED);
		fireEvent(new CollapseStateChangeEvent(this.state));
	}
	
	private void handleResize() {
		boolean overCollapsPoint = Window.getClientWidth() < getCollapsPoint(); 
		
		if(lastOverCollapsPoint == null || overCollapsPoint != lastOverCollapsPoint) {			
			if(overCollapsPoint && state == CollapseState.EXPANDED) {
				setVisualState(CollapseState.COLLAPSED);
			} else if(!overCollapsPoint && state == CollapseState.COLLAPSED) {				
				setVisualState(CollapseState.EXPANDED);
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

	public int getCollapsPoint() {
		return collapsPoint;
	}

	public void setCollapsPoint(int collapsPoint) {
		this.collapsPoint = collapsPoint;
	}

}