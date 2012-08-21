package br.com.alexandreaquiles.auctionsniper;

public interface AuctionEventListener {
	enum PriceSource {
		FromSniper, FromOtherBidder;
	}
	
	void auctionClosed();
	void currentPrice(int price, int increment, PriceSource source);
}
