package unit;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.alexandreaquiles.auctionsniper.AuctionSniper;
import br.com.alexandreaquiles.auctionsniper.SniperListener;

@RunWith(JMock.class)
public class AuctionSniperTest {
	private final Mockery context = new Mockery();
	private final SniperListener sniperListener = context.mock(SniperListener.class);
	private final AuctionSniper sniper = new AuctionSniper(sniperListener);
	
	@Test
	public void reportsLostWhenAuctionCloses(){
		context.checking(new Expectations(){{
			one(sniperListener).sniperLost();
		}});
		
		sniper.auctionClosed();
	}

}
