package br.com.alexandreaquiles.auctionsniper;

public interface Auction {
	void bid(int amount);
	void join();
	void addAuctionEventListeners(AuctionEventListener auctionEventListener);
}
