package endtoend;

import org.junit.After;
import org.junit.Test;

public class AuctionSniperEndToEndITCase {
	private final FakeAuctionServer auction = new FakeAuctionServer("item-54321");
	private final ApplicationRunner application = new ApplicationRunner();
	
	@Test public void sniperJoinAuctionUntilAuctionCloses() throws Exception {
		auction.startSellingItem();

		application.startBiddingIn(auction);
		auction.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
		
		auction.announceClosed();
		application.showsSniperHasLostAuction(0, 0);
	}
	
	@Test public void sniperMakesAHigherBidButLoses() throws Exception{
		auction.startSellingItem();

		application.startBiddingIn(auction);
		auction.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
		
		auction.reportPrice(1000, 98, "other bidder");
		application.hasShownSniperIsBidding(auction, 1000, 1098); //last price, last bid
		
		auction.hasReceivedBid(1098, ApplicationRunner.SNIPER_XMPP_ID);
		
		auction.announceClosed();
		application.showsSniperHasLostAuction(1000, 1098);
	}
	
	@Test public void sniperWinsAnAuctionByBiddingHigher() throws Exception{
		auction.startSellingItem();
		
		application.startBiddingIn(auction);
		auction.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
		
		auction.reportPrice(1000, 98, "other bidder");
		application.hasShownSniperIsBidding(auction, 1000, 1098); //last price, last bid
		
		auction.hasReceivedBid(1098, ApplicationRunner.SNIPER_XMPP_ID);
		
		auction.reportPrice(1098, 97, ApplicationRunner.SNIPER_XMPP_ID);
		application.hasShownSniperIsWinning(1098); //winning bid
		
		auction.announceClosed();
		application.showsSniperHasWonAuction(1098); //last price
		
	}
	
	@After public void stopAuction(){
		auction.stop();
	}
	
	@After public void stopApplication(){
		application.stop();
	}
			
			
}
