package unit;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.packet.Message;
import org.junit.Test;

import br.com.alexandreaquiles.auctionsniper.AuctionEventListener;
import br.com.alexandreaquiles.auctionsniper.AuctionMessageTranslator;

public class AuctionMessageTranslatorTest {
	
	public static final Chat UNUSED_CHAT = null;

	private final AuctionEventListener listener = null;
	private final AuctionMessageTranslator translator = new AuctionMessageTranslator();
	
	@Test
	public void notifiesAuctionClosedWhenCloseMessageReceived() {
		Message message = new Message();
		message.setBody("SOLVerion: 1.1; Event: CLOSE;");
		translator.processMessage(UNUSED_CHAT, message);
	}

}
