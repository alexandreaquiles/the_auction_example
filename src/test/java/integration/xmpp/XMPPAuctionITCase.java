package integration.xmpp;

import static org.junit.Assert.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.alexandreaquiles.auctionsniper.Auction;
import br.com.alexandreaquiles.auctionsniper.AuctionEventListener;
import br.com.alexandreaquiles.auctionsniper.AuctionHouse;
import br.com.alexandreaquiles.auctionsniper.xmpp.XMPPAuctionHouse;
import endtoend.ApplicationRunner;
import endtoend.FakeAuctionServer;

public class XMPPAuctionITCase {

	private static final String ITEM_ID = "item-54321";
	private FakeAuctionServer auctionServer = new FakeAuctionServer(ITEM_ID);
	private AuctionHouse auctionHouse;

	@Before
	public void startAuction() throws Exception{
		auctionServer.startSellingItem();
	}

	@Before
	public void connectAsSniper() throws Exception{
		auctionHouse = XMPPAuctionHouse.connect(FakeAuctionServer.XMPP_HOSTNAME, ApplicationRunner.SNIPER_ID, ApplicationRunner.SNIPER_PASSWORD);
	}
	
	@Test
	public void receivesEventsFromAuctionServerAfterJoining() throws Exception{
		CountDownLatch auctionWasClosed = new CountDownLatch(1);
		
		Auction auction = auctionHouse.auctionFor(ITEM_ID); 
		auction.addAuctionEventListeners(auctionClosedListener(auctionWasClosed));
		
		auction.join();
		auctionServer.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
		auctionServer.announceClosed();
		
		assertTrue("should have been closed", auctionWasClosed.await(2, TimeUnit.SECONDS));
	}

	private AuctionEventListener auctionClosedListener(final CountDownLatch auctionWasClosed) {
		return new AuctionEventListener() {
			public void auctionClosed() {
				auctionWasClosed.countDown();
			}
			
			public void currentPrice(int price, int increment, PriceSource priceSource) {
				//not implemented
			}
		};
	}
	
	@After
	public void disconnectSniper(){
		auctionHouse.disconnect();
	}
	
	@After
	public void stopAuction(){
		auctionServer.stop();
	}
	
}
