package br.com.alexandreaquiles.auctionsniper;

import br.com.alexandreaquiles.auctionsniper.ui.UserRequestListener;

public class SniperLauncher implements UserRequestListener {

	private final SniperCollector collector;
	private final AuctionHouse auctionHouse;

	public SniperLauncher(AuctionHouse auctionHouse, SniperCollector collector) {
		this.auctionHouse = auctionHouse;
		this.collector = collector;
	}

	public void joinAuction(String itemId) {
		Auction auction = auctionHouse.auctionFor(itemId);
		AuctionSniper sniper = new AuctionSniper(itemId, auction);
		auction.addAuctionEventListeners(sniper);
		collector.addSniper(sniper);
		auction.join();
	}

}
