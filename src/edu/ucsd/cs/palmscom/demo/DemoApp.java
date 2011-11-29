package edu.ucsd.cs.palmscom.demo;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;

import edu.ucsd.cs.palmscom.client.widgets.PalmscomWidget;

public class DemoApp implements EntryPoint {
	final LayoutPanel layout = new LayoutPanel();
	final FlowPanel panel = new FlowPanel();
	final HTML text = new HTML();
	final PalmscomWidget pcw = new PalmscomWidget();
	
	@Override
	public void onModuleLoad() {

		text.setHTML("<p>Pork chop ground round shankle kielbasa, bresaola biltong boudin tail. Chuck pastrami tail, flank hamburger strip steak pork loin pancetta tongue frankfurter biltong beef meatball. Ham hock fatback pork, pastrami meatball swine turkey bacon biltong shoulder pork belly pig sirloin tenderloin. Short loin jerky bresaola, short ribs drumstick beef ribs biltong chicken pork ground round frankfurter ham boudin pastrami. Turducken t-bone pastrami shank sirloin drumstick hamburger, fatback venison ham hock capicola. Shankle ball tip meatball pork chop, filet mignon fatback salami hamburger drumstick strip steak ham sausage. Strip steak chuck pork, short ribs frankfurter rump bresaola capicola meatball corned beef meatloaf kielbasa.  </p>"
				   + "<p> Chuck short ribs chicken, kielbasa t-bone tri-tip bresaola tongue prosciutto spare ribs shankle tenderloin turducken ribeye shoulder. Bacon meatloaf salami, biltong short ribs strip steak capicola brisket corned beef. Brisket jerky sausage pig, meatloaf frankfurter ribeye chicken short ribs bresaola swine leberkäse t-bone filet mignon meatball. Cow bacon leberkäse ground round. Sausage meatloaf pork belly biltong. Tongue beef ribs biltong, pork chop fatback jerky pork tail tenderloin rump bacon salami. Leberkäse pork biltong pastrami flank, hamburger ribeye ham hock ham short loin jerky drumstick chicken pancetta.  </p>"
				   + "<p> Pig tenderloin turkey shoulder, hamburger sirloin turducken frankfurter fatback ball tip filet mignon. Shoulder chuck pork loin capicola, frankfurter ham hock t-bone hamburger ribeye beef ribs tenderloin swine tongue turducken. Jerky venison spare ribs, pig pork loin frankfurter tail. Beef capicola fatback pork belly, prosciutto frankfurter meatball shank pork swine. Bacon kielbasa shank, pancetta capicola pork belly meatball pork chop chuck. Shankle boudin pork chop sausage ground round. Sirloin ground round pork pork belly, frankfurter short loin venison rump.  </p>"
				   + "<p> Ball tip beef sausage, ribeye shank t-bone short loin pig tri-tip frankfurter. Sirloin sausage flank turducken, drumstick tri-tip shank tongue. Cow venison andouille corned beef, biltong frankfurter beef pork loin short loin swine fatback. Boudin pancetta tongue chuck pig t-bone, bresaola venison tail. Swine ribeye bacon, ham shank drumstick spare ribs corned beef cow. Pork tail venison, beef ribs shank brisket fatback salami hamburger capicola bacon pork chop. Meatloaf spare ribs sausage, boudin t-bone rump sirloin swine bresaola beef ribs tenderloin shoulder.  </p>"
				   + "<p> Bresaola hamburger rump pork loin t-bone chuck jowl salami brisket. Ham hock pork corned beef sirloin turducken. Strip steak pork jerky, sirloin cow pork chop sausage meatloaf. Tenderloin drumstick spare ribs, short ribs flank pork bacon pork loin meatloaf jowl fatback. Kielbasa swine andouille, tail cow short ribs prosciutto fatback biltong spare ribs pig shankle. Andouille pork loin ribeye, strip steak boudin beef pancetta brisket chuck beef ribs hamburger rump bacon. Capicola jerky tenderloin short loin.  </p>"
				   + "<p> Does your lorem ipsum text long for something a little meatier? Give our generator a try… it’s tasty! </p>");
				
		panel.setStylePrimaryName("main");		
		panel.add(text);		
		layout.add(panel);
		layout.add(pcw);
		layout.setWidgetLeftRight(panel, 0, Unit.PX, 320, Unit.PX);
		layout.setWidgetRightWidth(pcw, 0, Unit.PX, 320, Unit.PX);
		RootLayoutPanel.get().add(layout);	
	}

}
