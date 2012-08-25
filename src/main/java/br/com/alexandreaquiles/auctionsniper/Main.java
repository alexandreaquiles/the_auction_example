package br.com.alexandreaquiles.auctionsniper;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.SwingUtilities;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import br.com.alexandreaquiles.auctionsniper.ui.MainWindow;
import br.com.alexandreaquiles.auctionsniper.ui.SnipersTableModel;

public class Main {
	
	private static final int ARG_HOSTNAME = 0;
	private static final int ARG_USERNAME = 1;
	private static final int ARG_PASSWORD = 2;
	private static final int ARG_ITEM_ID = 3;
	
	private static final String AUCTION_RESOURCE = "Auction";
	private static final String AUCTION_ID_FORMAT = "auction-%s@%s/"+AUCTION_RESOURCE;
	
	
	private final SnipersTableModel snipers = new SnipersTableModel();
	private MainWindow ui;
	private Collection<Chat> notToBeGCD = new ArrayList<Chat>();
	public static final String PRICE_COMMAND_FORMAT = "SOLVersion: 1.1; Event: PRICE; CurrentPrice: %d; Increment: %d; Bidder: %s;";
	public static final String JOIN_COMMAND_FORMAT = "SOLVersion: 1.1; Command: JOIN;";
	public static final String BID_COMMAND_FORMAT = "SOLVersion: 1.1; Command: BID; Price: %d"; 
	public static final String CLOSE_COMMAND_FORMAT = "SOLVersion: 1.1; Event: CLOSE;";
	
	public Main() throws Exception {
		startUserInterface();
	}

	private void startUserInterface() throws Exception {
		SwingUtilities.invokeAndWait(new Runnable() {
			public void run() {
				ui = new MainWindow(snipers);
			}
		});
		
	}

	public static void main(String... args) throws Exception {
		Main main = new Main();
		XMPPConnection connection = connection(args[ARG_HOSTNAME], args[ARG_USERNAME], args[ARG_PASSWORD]);
		main.disconnectWhenUICloses(connection);
		
		for (int i = ARG_ITEM_ID; i < args.length; i++) {
			main.joinAuction(connection, args[i]);
		}
	}

	private void joinAuction(XMPPConnection connection, String itemId) throws XMPPException {

		final Chat chat = connection.getChatManager()
				.createChat(auctionId(connection, itemId), null);
		notToBeGCD.add(chat);

		Auction auction = new XMPPAuction(chat);

		chat.addMessageListener(
				new AuctionMessageTranslator(
						connection.getUser(), 
						new AuctionSniper(itemId, auction, 
								new SwingThreadSniperListener(snipers))));
		
		auction.join();
	}

	private void disconnectWhenUICloses(final XMPPConnection connection) {
		ui.addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				connection.disconnect();
			}
		});
	}

	private String auctionId(XMPPConnection connection, String itemId) {
		return String.format(AUCTION_ID_FORMAT, itemId, connection.getServiceName());
	}

	private static XMPPConnection connection(String hostname, String username, String password) throws XMPPException {
		XMPPConnection connection = new XMPPConnection(hostname);
		connection.connect();
		connection.login(username, password, AUCTION_RESOURCE);
		return connection;
	}

}
