package br.com.alexandreaquiles.auctionsniper;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.SwingUtilities;

import br.com.alexandreaquiles.auctionsniper.ui.MainWindow;
import br.com.alexandreaquiles.auctionsniper.ui.SnipersTableModel;
import br.com.alexandreaquiles.auctionsniper.ui.UserRequestListener;
import br.com.alexandreaquiles.auctionsniper.xmpp.XMPPAuctionHouse;

public class Main {
	
	private static final int ARG_HOSTNAME = 0;
	private static final int ARG_USERNAME = 1;
	private static final int ARG_PASSWORD = 2;
	
	private final SnipersTableModel snipers = new SnipersTableModel();
	private MainWindow ui;
	private Collection<Auction> notToBeGCD = new ArrayList<Auction>();
	
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
		XMPPAuctionHouse auctionHouse = XMPPAuctionHouse.connect(args[ARG_HOSTNAME], args[ARG_USERNAME], args[ARG_PASSWORD]);
		main.disconnectWhenUICloses(auctionHouse);
		main.addUserRequestListenerFor(auctionHouse);
	}

	private void addUserRequestListenerFor(final XMPPAuctionHouse auctionHouse) {
		ui.addUserRequestListener( 
			new UserRequestListener() {
				public void joinAuction(String itemId) {
					snipers.addSniper(SniperSnapshot.joining(itemId));
					Auction auction = auctionHouse.auctionFor(itemId);
					notToBeGCD.add(auction);
					auction.addAuctionEventListeners(
						new AuctionSniper(
								itemId, 
								auction, 
								new SwingThreadSniperListener(snipers)));
					auction.join();
				}
			}
		);
	}

	private void disconnectWhenUICloses(final AuctionHouse auctionHouse) {
		ui.addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				auctionHouse.disconnect();
			}
		});
	}

}
