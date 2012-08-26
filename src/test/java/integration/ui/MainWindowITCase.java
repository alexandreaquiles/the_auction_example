package integration.ui;

import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

import br.com.alexandreaquiles.auctionsniper.SniperPortifolio;
import br.com.alexandreaquiles.auctionsniper.ui.MainWindow;
import br.com.alexandreaquiles.auctionsniper.ui.UserRequestListener;

import com.objogate.wl.swing.probe.ValueMatcherProbe;

import endtoend.AuctionSniperDriver;

public class MainWindowITCase {

	private final SniperPortifolio sniperPortifolio = new SniperPortifolio();
	private final MainWindow mainWindow = new MainWindow(sniperPortifolio);
	private final AuctionSniperDriver driver = new AuctionSniperDriver(100);
	
	@Test
	public void makesUserRequestWhenJoinButtonClicked(){
		final ValueMatcherProbe<String> buttonProbe = 
				new ValueMatcherProbe<String>(equalTo("item-id"), "join request");
		
		mainWindow.addUserRequestListener(
			new UserRequestListener() {
				public void joinAuction(String itemId) {
					buttonProbe.setReceivedValue(itemId);
				}
			}
		);
		
		driver.startBiddingFor("item-id", Integer.MAX_VALUE);
		driver.check(buttonProbe);
		
	}
}
