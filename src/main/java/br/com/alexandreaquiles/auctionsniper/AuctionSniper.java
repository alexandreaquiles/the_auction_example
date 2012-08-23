package br.com.alexandreaquiles.auctionsniper;

public class AuctionSniper implements AuctionEventListener {

	private String itemId;
	private SniperListener sniperListener;
	private Auction auction;
	private boolean isWinning = false;

	public AuctionSniper(String itemId, Auction auction, SniperListener sniperListener) {
		this.itemId = itemId;
		this.auction = auction;
		this.sniperListener = sniperListener;
	}

	public void auctionClosed() {
		if(isWinning){
			sniperListener.sniperWon();
		} else {
			sniperListener.sniperLost();
		}
	}

	public void currentPrice(int price, int increment, PriceSource priceSource) {
		isWinning = priceSource == PriceSource.FromSniper; 
		if(isWinning){
			sniperListener.sniperWinning();
		} else {
			int bid = price + increment;
			auction.bid(bid);
			sniperListener.sniperBidding(new SniperState(itemId, price, bid));
		}
	}

}
