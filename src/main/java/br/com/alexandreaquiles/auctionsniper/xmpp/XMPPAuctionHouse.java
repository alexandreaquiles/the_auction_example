package br.com.alexandreaquiles.auctionsniper.xmpp;

import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import br.com.alexandreaquiles.auctionsniper.Auction;
import br.com.alexandreaquiles.auctionsniper.AuctionHouse;
import br.com.alexandreaquiles.auctionsniper.Item;

public class XMPPAuctionHouse implements AuctionHouse {

	private static final String AUCTION_RESOURCE = "Auction";
	private static final String AUCTION_ID_FORMAT = "auction-%s@%s/"+ XMPPAuctionHouse.AUCTION_RESOURCE;
	private static final String LOGGER_NAME = "auction-sniper";
	
	public static final String LOG_FILE_NAME = "auction-sniper.log";
	
	private XMPPConnection connection;
	private XMPPFailureReporter failureReporter;

	public XMPPAuctionHouse(XMPPConnection connection) {
		this.connection = connection;
		this.failureReporter = new LoggingXMPPFailureReporter(makeLogger());
	}
	
	public Auction auctionFor(Item item) {
		return new XMPPAuction(connection, auctionId(connection, item), failureReporter);
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

	private Logger makeLogger() {
		Logger logger = Logger.getLogger(LOGGER_NAME);
		logger.setUseParentHandlers(false);
		logger.addHandler(simpleFileHandler());
		return logger;
	}

	private Handler simpleFileHandler() throws XMPPAuctionException {
		FileHandler handler;
		try {
			handler = new FileHandler(LOG_FILE_NAME);
			handler.setFormatter(new SimpleFormatter());
			return handler;
		} catch (Exception e) {
			throw new XMPPAuctionException("Could not create logger FileHandler " + LOG_FILE_NAME, e);
		}
	}

}
