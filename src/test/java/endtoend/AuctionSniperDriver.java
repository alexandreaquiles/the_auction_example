package endtoend;

import static org.hamcrest.core.IsEqual.equalTo;
import br.com.alexandreaquiles.auctionsniper.ui.MainWindow;

import com.objogate.wl.swing.AWTEventQueueProber;
import com.objogate.wl.swing.driver.JFrameDriver;
import com.objogate.wl.swing.driver.JLabelDriver;
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
		new JLabelDriver(this, named(MainWindow.SNIPER_STATUS_NAME)).hasText(equalTo(statusText));
	}

}
