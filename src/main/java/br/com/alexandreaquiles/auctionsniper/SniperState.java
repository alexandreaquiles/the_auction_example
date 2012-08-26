package br.com.alexandreaquiles.auctionsniper;

import br.com.alexandreaquiles.auctionsniper.util.Defect;

public enum SniperState {
	JOINING {
		public SniperState whenAuctionClosed() { return LOST; }
	},
	BIDDING {
		public SniperState whenAuctionClosed() { return LOST; }
	},
	WINNING{
		public SniperState whenAuctionClosed() { return WON; }
	},
	LOST,
	WON;

	public SniperState whenAuctionClosed() {
		throw new Defect("Auction is already closed");
	}
}
