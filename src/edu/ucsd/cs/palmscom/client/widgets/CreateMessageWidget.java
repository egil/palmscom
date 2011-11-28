package edu.ucsd.cs.palmscom.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;

import edu.ucsd.cs.palmscom.client.ClientServiceHandler;
import edu.ucsd.cs.palmscom.client.Collapsible;
import edu.ucsd.cs.palmscom.client.VisualStateChangeEvent;
import edu.ucsd.cs.palmscom.client.VisualStateChangeHandler;
import edu.ucsd.cs.palmscom.client.VisualStateType;
import edu.ucsd.cs.palmscom.shared.Message;

public class CreateMessageWidget extends Composite implements Collapsible {
	private static final double COLLAPSED_SIZE = 53;
	private static final double EXPANDED_SIZE = 90;	
	private VisualStateType state = VisualStateType.COLLAPSED;
	
	private final HandlerManager handlerManager = new HandlerManager(this);
	private final FlowPanel panel = new FlowPanel();
	private final WatermarkTextArea text = new WatermarkTextArea();
	private final Button button = new Button("Send");		
	private final ClientServiceHandler svc;	
	
	public CreateMessageWidget(ClientServiceHandler svc) {
		this.svc = svc;
		
		// Sets the widget to be wrapped by the composite. 		
		initWidget(panel);					
		
		// configure panel
		panel.setStylePrimaryName("create-message");

		// configure textbox
		text.setWatermark("Click here to ask a question . . .");	
		
		// configure button
		button.setVisible(false);
		
		// set up bindings
		setupBindings();
		
		// add widgets
		panel.add(text);
		panel.add(button);
	}
	
	private void setupBindings() {
		// allow users to click send button by hitting
		// while typing a message. Does not work with Firefox it seems.
		text.addKeyPressHandler(new KeyPressHandler() {
			// TODO: This apparently does not work properly in 
			// Firefox, workarounds include using key[up|down] instead,
			// but that has other problems such as repeat keys.
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if(event.getCharCode() == KeyCodes.KEY_ENTER) {
					button.click();
				}
			}
		});
		
		text.addFocusHandler(new FocusHandler() {			
			@Override
			public void onFocus(FocusEvent event) {
				button.setVisible(true);
				state = VisualStateType.EXPANDED;
				handlerManager.fireEvent(new VisualStateChangeEvent(state));
			}
		});
		
		text.addBlurHandler(new BlurHandler() {			
			@Override
			public void onBlur(BlurEvent event) {
				if(text.getText().isEmpty()) {
					button.setVisible(false);
					state = VisualStateType.COLLAPSED;
					handlerManager.fireEvent(new VisualStateChangeEvent(state));
				}
			}
		});
				
		// handle click event on button, send 
		// new message to server
		button.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				sendMessage(event);				
			}
		});
	}
	
	/**
	 * SEND MESSAGE WORKFLOW:
	 * 
	 *  1. Input state: onClick, goto 2
	 *  2. Lock input, show sending indicator: goto 3
	 *  3. Validate input: valid ? goto 4 : goto e1
	 *  4. Send to server: success ? goto 5 : goto e2
	 *  5. Clear input: goto 6
	 *  6. Unlock input, remove sending indicator, set focus to inputText: goto 1
	 *  e1. Show warning message: goto 6
	 *  e2. Show error message: goto 6  
	 */
	private void sendMessage(ClickEvent event){		
		String msgtxt = text.getText().trim();
		
		// 2. Lock input, show sending indicator: goto 3
		setSendMessageState();		
		
		// 3. Validate input: valid ? goto 4 : goto e1
		if(msgtxt.isEmpty()) {
			// TODO: Show warning message: goto 6
			
			// 6. Unlock input, remove sending indicator, set focus to inputText: goto 1
			setInputMessageState();
			
			return;
		}
			
		// 4. Send to server: success ? goto 5 : goto e2
		Message msg = new Message();
		msg.setText(msgtxt);
		svc.sendMessage(msg, new AsyncCallback<Void>() {					
			@Override
			public void onSuccess(Void result) {
				GWT.log("INFO: Message saved succesfully.");

				// 5. Clear input: goto 6
				text.setText("");
				
				// 6. Unlock input, remove sending indicator, set focus to inputText: goto 1
				setInputMessageState();					
			}				
			
			@Override
			public void onFailure(Throwable caught) {
				GWT.log(caught.getMessage());

				// TODO: Show error message: goto 6
				
				// 6. Unlock input, remove sending indicator, set focus to inputText: goto 1
				setInputMessageState();
			}
		});		
	}
	
	private void setInputMessageState() {
		text.setEnabled(true);
		button.setEnabled(true);
		// TODO: Remove sending indicator
		text.setFocus(true);
	}

	private void setSendMessageState() {
		text.setEnabled(false);
		button.setEnabled(false);
		// TODO: Add sending indicator
	}
	
	public void addStateChangeHandler(VisualStateChangeHandler handler) {
		handlerManager.addHandler(VisualStateChangeEvent.getType(), handler);  
	}
	
	@Override
	public double getHeight() {
		return state == VisualStateType.COLLAPSED ? COLLAPSED_SIZE : EXPANDED_SIZE;
	}

	@Override
	public double getWidth() {
		return 0;
	}
}


