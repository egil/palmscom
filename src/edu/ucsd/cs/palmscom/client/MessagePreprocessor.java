package edu.ucsd.cs.palmscom.client;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

import edu.ucsd.cs.palmscom.shared.Message;
import edu.ucsd.cs.palmscom.shared.Settings;
import edu.ucsd.cs.palmscom.shared.User;

public class MessagePreprocessor {	
	//((^|\s+)(search strings)([\W]*?)(\s+|$))
	//
	//Options: case insensitive; ^ and $ match at line breaks
	//
	//Match the regular expression below and capture its match into backreference number 1 «((^|\s+)(search strings)([\W]*?)(\s+|$))»
	//   Match the regular expression below and capture its match into backreference number 2 «(^|\s+)»
	//      Match either the regular expression below (attempting the next alternative only if this one fails) «^»
	//         Assert position at the beginning of a line (at beginning of the string or after a line break character) «^»
	//      Or match regular expression number 2 below (the entire group fails if this one fails to match) «\s+»
	//         Match a single character that is a “whitespace character” (spaces, tabs, and line breaks) «\s+»
	//            Between one and unlimited times, as many times as possible, giving back as needed (greedy) «+»
	//   Match the regular expression below and capture its match into backreference number 3 «(search strings)»
	//      Match the characters “search strings” literally «search strings»
	//   Match the regular expression below and capture its match into backreference number 4 «([\W]*?)»
	//      Match a single character that is a “non-word character” «[\W]*?»
	//         Between zero and unlimited times, as few times as possible, expanding as needed (lazy) «*?»
	//   Match the regular expression below and capture its match into backreference number 5 «(\s+|$)»
	//      Match either the regular expression below (attempting the next alternative only if this one fails) «\s+»
	//         Match a single character that is a “whitespace character” (spaces, tabs, and line breaks) «\s+»
	//            Between one and unlimited times, as many times as possible, giving back as needed (greedy) «+»
	//      Or match regular expression number 2 below (the entire group fails if this one fails to match) «$»
	//         Assert position at the end of a line (at the end of the string or before a line break character) «$»
	//	
	private static RegExp userMatcher;
	private static RegExp keywordMatcher;
	private static Settings settings;
	
	public static void Preprocess(Message msg) {
		String text = msg.getText().trim();
		text = SafeHtmlUtils.htmlEscape(text);

		msg.setIsOwnMessage(msg.getAuthor().compareTo(settings.getCurrentUser()) == 0);
		
		// only highlight message if it is from somebody else
		if(!msg.isOwnMessage()) {
			if(userMatcher.test(text)) {
				msg.setIsMessageOfIntrest(true);
			}
			userMatcher.setLastIndex(0);
			
			if(keywordMatcher.test(text)) {
				msg.setIsMessageOfIntrest(true);
			}
			keywordMatcher.setLastIndex(0);
			
			text = userMatcher.replace(text, "$2<span class=\"user\">$3</span>$4$5");
			userMatcher.setLastIndex(0);
		}

		// highlight keywords
		if(settings.getKeywords().size() > 0) {
			text = keywordMatcher.replace(text, "$2<span class=\"keyword\">$3</span>$4$5");
			keywordMatcher.setLastIndex(0);
		}
		
		GWT.log("personalizeMessage: " + (new Date().toString()) + " moi = " + msg.getIsMessageOfIntrest());
		
		msg.setText(text);
	}

	public static void init(Settings settings) {
		MessagePreprocessor.settings = settings;
		userMatcher = RegExp.compile("((^|\\s+)(" + settings.getCurrentUser().getFullname() + ")([\\W]*?)(\\s+|$))", "gi");
		
		String keywords = "";		
		for(int i = 0; i < settings.getKeywords().size(); i++) {
			keywords += i == 0 ? settings.getKeywords().get(i) : "|" + settings.getKeywords().get(i);
		}
		keywordMatcher = RegExp.compile("((^|\\s+)(" + keywords + ")([\\W]*?)(\\s+|$))", "gi");
	}
}
