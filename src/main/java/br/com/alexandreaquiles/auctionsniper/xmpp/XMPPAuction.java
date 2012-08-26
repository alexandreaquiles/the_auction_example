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

	public XMPPAuction(XMPPConnection connection, String auctionId) {
		this.chat = connection.getChatManager()
			.createChat(
				auctionId,
				new AuctionMessageTranslator(
					connection.getUser(), 
					auctionEventListeners.announce()));
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