package br.com.alexandreaquiles.auctionsniper;

public interface AuctionHouse {
	Auction auctionFor(String itemId);
	void disconnect();
}
