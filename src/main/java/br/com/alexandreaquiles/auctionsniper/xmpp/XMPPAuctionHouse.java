package br.com.alexandreaquiles.auctionsniper.xmpp;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import br.com.alexandreaquiles.auctionsniper.Auction;
import br.com.alexandreaquiles.auctionsniper.AuctionHouse;
import br.com.alexandreaquiles.auctionsniper.Item;

public class XMPPAuctionHouse implements AuctionHouse {

	private static final String AUCTION_RESOURCE = "Auction";
	private static final String AUCTION_ID_FORMAT = "auction-%s@%s/"+ XMPPAuctionHouse.AUCTION_RESOURCE;
	
	private XMPPConnection connection;

	public XMPPAuctionHouse(XMPPConnection connection) {
		this.connection = connection;
	}
	
	public Auction auctionFor(Item item) {
		return new XMPPAuction(connection, auctionId(connection, item));
	}
	
	public static XMPPAuctionHouse connect(String hostname, String username, String password) throws XMPPException {
		XMPPConnection connection = new XMPPConnection(hostname);
		connection.connect();
		connection.login(username, password, AUCTION_RESOURCE);
		return new XMPPAuctionHouse(connection);
	}

	public void disconnect() {
		connection.disconnect();
	}
	
	private String auctionId(XMPPConnection connection, Item item) {
		return String.format(AUCTION_ID_FORMAT, item.identifier, connection.getServiceName());
	}

}
