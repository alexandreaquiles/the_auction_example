package unit;

import static org.hamcrest.Matchers.equalTo;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.States;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.alexandreaquiles.auctionsniper.Auction;
import br.com.alexandreaquiles.auctionsniper.AuctionHouse;
import br.com.alexandreaquiles.auctionsniper.AuctionSniper;
import br.com.alexandreaquiles.auctionsniper.SniperCollector;
import br.com.alexandreaquiles.auctionsniper.SniperLauncher;

@RunWith(JMock.class)
public class SniperLauncherTest {

	private final Mockery context = new Mockery();
	private final States auctionState = context.states("auction state")
											.startsAs("not joined");
	private final AuctionHouse auctionHouse = context.mock(AuctionHouse.class);
	private final Auction auction = context.mock(Auction.class);
	private final SniperCollector sniperCollector = context.mock(SniperCollector.class);
	private final SniperLauncher launcher = new SniperLauncher(auctionHouse, sniperCollector);
	
	@Test
	public void addsNewSniperToCollectorAndThenJoinsAuction(){
		final String itemId = "item 123";
		context.checking(new Expectations(){{
			allowing(auctionHouse).auctionFor(itemId);
								will(returnValue(auction));
			oneOf(auction).addAuctionEventListeners(with(sniperForItem(itemId)));
							when(auctionState.is("not joined"));
			oneOf(sniperCollector).addSniper(with(sniperForItem(itemId)));
							when(auctionState.is("not joined"));
			one(auction).join(); then(auctionState.is("joined"));
		}});
		launcher.joinAuction(itemId);
	}

	protected Matcher<AuctionSniper>sniperForItem(String itemId) {
		return new FeatureMatcher<AuctionSniper, String>(equalTo(itemId), "sniper with item id", "item") {
			protected String featureValueOf(AuctionSniper actual) {
				return actual.getSnapshot().itemId;
			}
		};
	}
}
