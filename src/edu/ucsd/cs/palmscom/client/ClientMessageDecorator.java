package edu.ucsd.cs.palmscom.client;

import java.util.Date;
import com.google.gwt.core.client.GWT;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

import edu.ucsd.cs.palmscom.shared.Message;
import edu.ucsd.cs.palmscom.shared.MessageDecorator;
import edu.ucsd.cs.palmscom.shared.Settings;
import edu.ucsd.cs.palmscom.shared.User;

public class ClientMessageDecorator extends MessageDecorator {
	private static final long serialVersionUID = 7122037700195534649L;
	private boolean hasPreprocessed;
	private Boolean isOwnMessage;
	
	public ClientMessageDecorator(Message message) {		
		super(message);
	}
	
	public boolean isOwnMessage() {
		if(isOwnMessage == null) {
			isOwnMessage = AppState.getInstance().getUser().compareTo(this.getAuthor()) == 0;
		}
		return isOwnMessage;
	}
	
	public String getHtmlID() {
		return "id-" + this.getID();
	}
	
	@Override
	public Boolean isMessageOfIntrest() {
		if(!hasPreprocessed) {
			Preprocessor.Execute(this);
		}
		return super.isMessageOfIntrest();
	}
	
	@Override 
	public String getText() {
		if(!hasPreprocessed) {
			Preprocessor.Execute(this);
		}
		return super.getText();
	}
	
	public static ClientMessageDecorator[] decorateMessages(Message[] messages) {
		ClientMessageDecorator[] result = new ClientMessageDecorator[messages.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = new ClientMessageDecorator(messages[i]);
		}
		return result;
	}
	
	private static class Preprocessor {
		private static RegExp userMatcher;
		private static RegExp keywordMatcher;
		
		public static void Execute(ClientMessageDecorator msg) {
			if(userMatcher == null) {
				Preprocessor.init();
			}
			// set this flag here to avoid endless loops
			msg.hasPreprocessed = true;
			
			String text = msg.getText().trim();
			text = SafeHtmlUtils.htmlEscape(text);
			
			// set moi to false by default (otherwise it is null)
			msg.isMessageOfIntrest(false);

			Settings settings = AppState.getInstance().getSettings();
			
			// only highlight message if it is from somebody else
			if(!msg.isOwnMessage()) {
				if(userMatcher.test(text)) {
					msg.isMessageOfIntrest(true);
				}
				userMatcher.setLastIndex(0);

				if(settings.getKeywords().size() > 0) {
					if(keywordMatcher.test(text)) {
						msg.isMessageOfIntrest(true);
					}
					keywordMatcher.setLastIndex(0);
				}
				
				text = userMatcher.replace(text, "$2<span class=\"user\">$3</span>$4$5");
				userMatcher.setLastIndex(0);
			}

			if(settings.getKeywords().size() > 0) {
				text = keywordMatcher.replace(text, "$2<span class=\"keyword\">$3</span>$4$5");
				keywordMatcher.setLastIndex(0);
			}
			
			GWT.log("personalizeMessage: " + (new Date().toString()) + " moi = " + msg.isMessageOfIntrest());
			
			msg.setText(text);
		}
		
		private static void init() {
			// build user name/full name detection. We look for both username,
			// first name, last name, and full name.
			User user = AppState.getInstance().getUser();
			String options = user.getUsername() + "|" + user.getFullname();
			String[] parts = user.getFullname().split("\\s+");
			if(parts.length > 1) {
				for (String part : parts) {
					options += "|" + part;
				}
			}
	
			userMatcher = RegExp.compile("((^|\\s+)(" + options + ")([\\W]*?)(\\s+|$))", "gi");
	
			// create keywords options string
			Settings settings = AppState.getInstance().getSettings();
			options = "";
			for(int i = 0; i < settings.getKeywords().size(); i++) {
				options += i == 0 ? settings.getKeywords().get(i) : "|" + settings.getKeywords().get(i);
			}
			if(!options.isEmpty()) {
				keywordMatcher = RegExp.compile("((^|\\s+)(" + options + ")([\\W]*?)(\\s+|$))", "gi");
			}
		}
	}
}
