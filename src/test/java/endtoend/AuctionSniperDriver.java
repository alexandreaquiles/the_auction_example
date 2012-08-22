package endtoend;

import static com.objogate.wl.swing.matcher.IterableComponentsMatcher.matching;
import static com.objogate.wl.swing.matcher.JLabelTextMatcher.withLabelText;
import static org.hamcrest.core.IsEqual.equalTo;
import br.com.alexandreaquiles.auctionsniper.ui.MainWindow;

import com.objogate.wl.swing.AWTEventQueueProber;
import com.objogate.wl.swing.driver.JFrameDriver;
import com.objogate.wl.swing.driver.JTableDriver;
import com.objogate.wl.swing.gesture.GesturePerformer;

@SuppressWarnings("unchecked")
public class AuctionSniperDriver extends JFrameDriver {

	public AuctionSniperDriver(int timeoutInMillis) {
		super(new GesturePerformer(),
			JFrameDriver.topLevelFrame(named(MainWindow.MAIN_WINDOW_NAME),
			showingOnScreen()),
			new AWTEventQueueProber(timeoutInMillis, 100));
	}
	
	public void showsSniperStatus(String statusText){
		new JTableDriver(this).hasCell(withLabelText(equalTo(statusText)));
	}

	public void showsSniperStatus(String itemId, int lastPrice, int lastBid, String statusText) {
		JTableDriver table = new JTableDriver(this);
		table.hasRow(
			matching(withLabelText(itemId), withLabelText(String.valueOf(lastPrice)),
			withLabelText(String.valueOf(lastBid)), withLabelText(statusText))
		);
		
	}

}
