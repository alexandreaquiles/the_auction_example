package integration;

import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

import com.objogate.wl.swing.probe.ValueMatcherProbe;

import br.com.alexandreaquiles.auctionsniper.ui.MainWindow;
import br.com.alexandreaquiles.auctionsniper.ui.SnipersTableModel;
import br.com.alexandreaquiles.auctionsniper.ui.UserRequestListener;
import endtoend.AuctionSniperDriver;

public class MainWindowITCase {

	private final SnipersTableModel tableModel = new SnipersTableModel();
	private final MainWindow mainWindow = new MainWindow(tableModel);
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
		
		driver.startBiddingFor("item-id");
		driver.check(buttonProbe);
		
	}
}
