package br.com.alexandreaquiles.auctionsniper;

public class AuctionSniper implements AuctionEventListener {

	private SniperListener sniperListener;

	public AuctionSniper(SniperListener sniperListener) {
		this.sniperListener = sniperListener;
	}

	public void auctionClosed() {
		sniperListener.sniperLost();
	}

	@Override
	public void currentPrice(int price, int increment) {
		
	}

}
