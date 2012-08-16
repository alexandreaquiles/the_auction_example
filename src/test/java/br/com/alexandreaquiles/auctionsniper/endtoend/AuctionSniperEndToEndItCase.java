package br.com.alexandreaquiles.auctionsniper.endtoend;

import org.junit.After;
import org.junit.Test;

public class AuctionSniperEndToEndItCase {
	private final FakeAuctionServer auction = new FakeAuctionServer("item-54321");
	private final ApplicationRunner application = new ApplicationRunner();
	
	@Test public void sniperJoinAuctionUntilAuctionCloses() {
		auction.startSellingItem();
		application.startBiddingIn(auction);
		auction.hasReceivedJoinRequestFromSniper();
		auction.announceClosed();
		application.showsSniperHasLostAuction();
	}
	
	@After public void stopAuction(){
		auction.stop();
	}
	
	@After public void stopApplication(){
		application.stop();
	}
			
			
}
