package br.com.alexandreaquiles.auctionsniper;

import br.com.alexandreaquiles.auctionsniper.ui.UserRequestListener;

public class SniperLauncher implements UserRequestListener {

	private final SniperCollector collector;
	private final AuctionHouse auctionHouse;

	public SniperLauncher(AuctionHouse auctionHouse, SniperCollector collector) {
		this.auctionHouse = auctionHouse;
		this.collector = collector;
	}

	public void joinAuction(Item item) {
		Auction auction = auctionHouse.auctionFor(item);
		AuctionSniper sniper = new AuctionSniper(item, auction);
		auction.addAuctionEventListeners(sniper);
		collector.addSniper(sniper);
		auction.join();
	}

}
