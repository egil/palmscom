package edu.ucsd.cs.widgets;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.TextBox;

/**
 * 
 * @author igormoochnick
 * {@linkplain http://igorshare.wordpress.com/2009/06/30/adding-watermark-to-gwt-textbox-widget/}
 *
 */
public class WatermarkTextBox extends TextBox implements BlurHandler, FocusHandler
{
	private String watermark;
	private HandlerRegistration blurHandler;
	private HandlerRegistration focusHandler;
	
	public WatermarkTextBox( )
	{
		super();
		this.setStylePrimaryName("textInput");
	}
	
	public WatermarkTextBox(String defaultValue)
	{
		this();
		setText(defaultValue);
	}
	
	public WatermarkTextBox(String defaultValue, String watermark)
	{
		this(defaultValue);
		setWatermark(watermark);
	}
	
	/**
	 * Adds a watermark if the parameter is not NULL or EMPTY
	 * 
	 * @param watermark
	 */
	public void setWatermark(final String watermark)
	{
		this.watermark = watermark;
		
		if ((watermark != null) && (watermark != ""))
		{
			blurHandler = addBlurHandler(this);
			focusHandler = addFocusHandler(this);
			EnableWatermark();
		}
		else
		{
			// Remove handlers
			blurHandler.removeHandler();
			focusHandler.removeHandler();
		}
	}

	@Override
	public void onBlur(BlurEvent event)
	{
		EnableWatermark();
	}
	
	void EnableWatermark()
	{
		String text = super.getText(); 
		if ((text.length() == 0) || (text.equalsIgnoreCase(watermark)))
		{
			// Show watermark
			setText(watermark);
			addStyleDependentName("watermark");
		}
	}

	@Override
	public void onFocus(FocusEvent event)
	{
		removeStyleDependentName("watermark");
		
		if (super.getText().equals(watermark))
		{
			// Hide watermark
			setText("");
		}
	}
	
	@Override
	public String getText(){
		return super.getText().equals(watermark) ? "" : super.getText(); 
	}
}