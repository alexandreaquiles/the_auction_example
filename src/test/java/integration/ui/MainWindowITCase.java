package integration.ui;

import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

import br.com.alexandreaquiles.auctionsniper.Item;
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
		final ValueMatcherProbe<Item> itemProbe = 
				new ValueMatcherProbe<Item>(equalTo(new Item("item-id", 123)), "item request");
		
		mainWindow.addUserRequestListener(
			new UserRequestListener() {
				public void joinAuction(Item item) {
					itemProbe.setReceivedValue(item);
				}
			}
		);
		
		driver.startBiddingFor("item-id", 123);
		driver.check(itemProbe);
		
	}
}
