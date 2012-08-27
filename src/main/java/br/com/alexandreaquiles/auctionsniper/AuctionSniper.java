package br.com.alexandreaquiles.auctionsniper;

import br.com.alexandreaquiles.auctionsniper.util.Announcer;

public class AuctionSniper implements AuctionEventListener {

	private final Item item;
	private final Auction auction;
	private final Announcer<SniperListener> listeners = Announcer.to(SniperListener.class);
	private SniperSnapshot snapshot;

	public AuctionSniper(Item item, Auction auction) {
		this.item = item;
		this.auction = auction;
		this.snapshot = SniperSnapshot.joining(item);
	}

	public void auctionClosed() {
		snapshot = snapshot.closed();
		notifyChange();
	}

	public void currentPrice(int price, int increment, PriceSource priceSource) {
		switch (priceSource) {
		case FromSniper:
			snapshot = snapshot.winning(price);
			break;
		case FromOtherBidder:
			int bid = price + increment;
			if(item.allowsBid(bid)){
				auction.bid(bid);
				snapshot = snapshot.bidding(price, bid);
			} else {
				snapshot = snapshot.losing(price);
			}
			break;
		}
		notifyChange();
	}

	private void notifyChange() {
		listeners.announce().sniperStateChanged(snapshot);
	}

	public SniperSnapshot getSnapshot() {
		return snapshot;
	}

	public void addSniperListener(SniperListener sniperListener) {
		listeners.addListener(sniperListener);
	}

	public void auctionFailed() {
		snapshot = snapshot.failed();
		notifyChange();
	}

}
