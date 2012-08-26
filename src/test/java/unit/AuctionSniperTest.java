package unit;

import static org.hamcrest.Matchers.equalTo;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.States;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.alexandreaquiles.auctionsniper.Auction;
import br.com.alexandreaquiles.auctionsniper.AuctionEventListener.PriceSource;
import br.com.alexandreaquiles.auctionsniper.AuctionSniper;
import br.com.alexandreaquiles.auctionsniper.Item;
import br.com.alexandreaquiles.auctionsniper.SniperListener;
import br.com.alexandreaquiles.auctionsniper.SniperSnapshot;
import br.com.alexandreaquiles.auctionsniper.SniperState;

@RunWith(JMock.class)
public class AuctionSniperTest {
	private static final Item ITEM = new Item("item-id", 1234);
	
	private final Mockery context = new Mockery();
	private final States sniperState = context.states("sniper");
	
	private final Auction auction = context.mock(Auction.class);
	private final SniperListener sniperListener = context.mock(SniperListener.class);
	private final AuctionSniper sniper = new AuctionSniper(ITEM, auction);
	
	@Before public void attachListener() {
		sniper.addSniperListener(sniperListener);
	}
		  
	@Test
	public void reportsLostWhenAuctionClosesImmediately(){
		context.checking(new Expectations(){{
			atLeast(1).of(sniperListener).sniperStateChanged(with(aSniperThatIs(SniperState.LOST)));
		}});
		
		sniper.auctionClosed();
	}
	
	@Test
	public void reportsLostIfAuctionClosesWhenBidding(){
		context.checking(new Expectations(){{
			ignoring(auction);
			allowing(sniperListener).sniperStateChanged(with(aSniperThatIs(SniperState.BIDDING)));
									then(sniperState.is("bidding"));
			atLeast(1).of(sniperListener).sniperStateChanged(with(aSniperThatIs(SniperState.LOST)));
									when(sniperState.is("bidding"));
		}});
		
		sniper.currentPrice(123, 45, PriceSource.FromOtherBidder);
		sniper.auctionClosed();
	}
	
	@Test
	public void reportsWonIfAuctionCloseWhenWinning(){
		context.checking(new Expectations(){{
			ignoring(auction);
			allowing(sniperListener).sniperStateChanged(with(aSniperThatIs(SniperState.WINNING)));
									then(sniperState.is("winning"));
			atLeast(1).of(sniperListener).sniperStateChanged(with(aSniperThatIs(SniperState.WON)));
									when(sniperState.is("winning"));
		}});
		
		sniper.currentPrice(123, 45, PriceSource.FromSniper);
		sniper.auctionClosed();
	}

	@Test
	public void bidsHigherAndReportsBiddingWhenNewPriceArrives(){
		final int price = 1001;
		final int increment = 25;
		
		context.checking(new Expectations(){{
			int bid = price + increment;
			one(auction).bid(bid);
			atLeast(1).of(sniperListener).sniperStateChanged(new SniperSnapshot(ITEM.identifier, price, bid, SniperState.BIDDING));
		}});
		
		sniper.currentPrice(price, increment, PriceSource.FromOtherBidder);
	}
	
	@Test
	public void reportsIsWinningWhenCurrentPricesComesFromSniper(){
		context.checking(new Expectations(){{
			ignoring(auction);
			allowing(sniperListener).sniperStateChanged(with(aSniperThatIs(SniperState.BIDDING)));
									then(sniperState.is("bidding"));
			atLeast(1).of(sniperListener).sniperStateChanged(new SniperSnapshot(ITEM.identifier, 135, 135, SniperState.WINNING));
									when(sniperState.is("bidding"));
		}});
		
		sniper.currentPrice(123, 12, PriceSource.FromOtherBidder);
		sniper.currentPrice(135, 45, PriceSource.FromSniper);
	}
	
	@Test
	public void doesNotBidAndReportsLosingIfSubsequentPriceIsAboveStopPrice(){
		allowingSniperBidding();
		context.checking(new Expectations(){{
			int bid = 123 + 45;
			allowing(auction).bid(bid);
			atLeast(1).of(sniperListener).sniperStateChanged(new SniperSnapshot(ITEM.identifier, 2345, bid, SniperState.LOSING));
		}});
		sniper.currentPrice(123, 45, PriceSource.FromOtherBidder);
		sniper.currentPrice(2345, 25, PriceSource.FromOtherBidder);
	}
	
	private Matcher<SniperSnapshot> aSniperThatIs(SniperState state) {
		return new FeatureMatcher<SniperSnapshot, SniperState>(equalTo(state), "sniper that is", "was") {
			protected SniperState featureValueOf(SniperSnapshot actual) {
				return actual.state;
			}
		};
	}
	
	private void allowingSniperBidding() {
		context.checking(new Expectations(){{
			allowing(sniperListener).sniperStateChanged(with(aSniperThatIs(SniperState.BIDDING)));
									then(sniperState.is("bidding"));
		}});
	}

}
