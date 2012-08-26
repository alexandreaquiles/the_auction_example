package br.com.alexandreaquiles.auctionsniper;

public interface AuctionHouse {
	Auction auctionFor(Item item);
	void disconnect();
}
