package br.com.alexandreaquiles.auctionsniper;

import java.util.ArrayList;
import java.util.Collection;

import br.com.alexandreaquiles.auctionsniper.ui.SnipersTableModel;
import br.com.alexandreaquiles.auctionsniper.ui.UserRequestListener;

public class SniperLauncher implements UserRequestListener {

	private final Collection<Auction> notToBeGCD = new ArrayList<Auction>();
	private final AuctionHouse auctionHouse;
	private final SnipersTableModel snipers;

	public SniperLauncher(AuctionHouse auctionHouse, SnipersTableModel snipers) {
		this.auctionHouse = auctionHouse;
		this.snipers = snipers;
	}

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
