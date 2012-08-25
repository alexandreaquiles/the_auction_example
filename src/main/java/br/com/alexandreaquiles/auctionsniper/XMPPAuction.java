package br.com.alexandreaquiles.auctionsniper;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

class XMPPAuction implements Auction {
	
	private static final String AUCTION_ID_FORMAT = "auction-%s@%s/"+Main.AUCTION_RESOURCE;

	private Announcer<AuctionEventListener> auctionEventListeners = Announcer.to(AuctionEventListener.class);
	private Chat chat;

	public XMPPAuction(XMPPConnection connection, String itemId) {
		this.chat = connection.getChatManager()
			.createChat(
				auctionId(connection, itemId),
					new AuctionMessageTranslator(
						connection.getUser(), 
						auctionEventListeners.announce()));
	}
	
	public void bid(int amount) {
		sendMessage(String.format(Main.BID_COMMAND_FORMAT, amount));
	}

	public void join() {
		sendMessage(Main.JOIN_COMMAND_FORMAT);
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

	private String auctionId(XMPPConnection connection, String itemId) {
		return String.format(AUCTION_ID_FORMAT, itemId, connection.getServiceName());
	}

}