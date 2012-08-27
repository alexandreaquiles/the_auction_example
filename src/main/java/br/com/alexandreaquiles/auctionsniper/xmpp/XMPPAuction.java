package br.com.alexandreaquiles.auctionsniper.xmpp;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import br.com.alexandreaquiles.auctionsniper.Auction;
import br.com.alexandreaquiles.auctionsniper.AuctionEventListener;
import br.com.alexandreaquiles.auctionsniper.util.Announcer;

public class XMPPAuction implements Auction {
	
	public static final String JOIN_COMMAND_FORMAT = "SOLVersion: 1.1; Command: JOIN;";
	public static final String BID_COMMAND_FORMAT = "SOLVersion: 1.1; Command: BID; Price: %d"; 
	
	private Announcer<AuctionEventListener> auctionEventListeners = Announcer.to(AuctionEventListener.class);
	private Chat chat;

	public XMPPAuction(XMPPConnection connection, String auctionId, XMPPFailureReporter failureReporter) {
		AuctionMessageTranslator translator = translatorFor(connection, failureReporter);
		this.chat = connection.getChatManager()
						.createChat(auctionId, translator);
		addAuctionEventListeners(chatDisconnectorFor(translator));
		
	}

	private AuctionMessageTranslator translatorFor(XMPPConnection connection, XMPPFailureReporter failureReporter) {
		return new AuctionMessageTranslator(connection.getUser(), auctionEventListeners.announce(), failureReporter);
	}
	
	private AuctionEventListener chatDisconnectorFor(final AuctionMessageTranslator translator) {
		return new AuctionEventListener(){
			public void auctionClosed() {}
			public void currentPrice(int price, int increment, PriceSource priceSource) {}
			public void auctionFailed() {
				chat.removeMessageListener(translator);
			}
		};
	}
	
	public void bid(int amount) {
		sendMessage(String.format(BID_COMMAND_FORMAT, amount));
	}

	public void join() {
		sendMessage(JOIN_COMMAND_FORMAT);
	}

	public void addAuctionEventListeners(AuctionEventListener auctionEventListener) {
		auctionEventListeners.addListener(auctionEventListener);
	}

	private void sendMessage(String message) {
		try {
			chat.sendMessage(message);
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}


}