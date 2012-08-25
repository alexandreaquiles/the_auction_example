package endtoend;

import static com.objogate.wl.swing.matcher.IterableComponentsMatcher.matching;
import static com.objogate.wl.swing.matcher.JLabelTextMatcher.withLabelText;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.table.JTableHeader;

import br.com.alexandreaquiles.auctionsniper.ui.Column;
import br.com.alexandreaquiles.auctionsniper.ui.MainWindow;

import com.objogate.wl.swing.AWTEventQueueProber;
import com.objogate.wl.swing.driver.JButtonDriver;
import com.objogate.wl.swing.driver.JFrameDriver;
import com.objogate.wl.swing.driver.JTableDriver;
import com.objogate.wl.swing.driver.JTableHeaderDriver;
import com.objogate.wl.swing.driver.JTextFieldDriver;
import com.objogate.wl.swing.gesture.GesturePerformer;

@SuppressWarnings("unchecked")
public class AuctionSniperDriver extends JFrameDriver {

	public AuctionSniperDriver(int timeoutInMillis) {
		super(new GesturePerformer(),
			JFrameDriver.topLevelFrame(named(MainWindow.MAIN_WINDOW_NAME),
			showingOnScreen()),
			new AWTEventQueueProber(timeoutInMillis, 100));
	}
	
	public void showsSniperStatus(String itemId, int lastPrice, int lastBid, String statusText) {
		JTableDriver table = new JTableDriver(this);
		table.hasRow(
			matching(withLabelText(itemId), withLabelText(String.valueOf(lastPrice)),
			withLabelText(String.valueOf(lastBid)), withLabelText(statusText))
		);
		
	}

	public void hasColumnTitles() {
		JTableHeaderDriver headers = new JTableHeaderDriver(this, JTableHeader.class);
		headers.hasHeaders(matching(withLabelText(Column.ITEM_IDENTIFIER.name), withLabelText(Column.LAST_PRICE.name),
									withLabelText(Column.LAST_BID.name), withLabelText(Column.SNIPER_STATE.name)));
		
	}

	public void startBiddingFor(String itemId) {
		itemIdField().replaceAllText(itemId); 
		bidButton().click();
	}
	
	private JTextFieldDriver itemIdField(){
		JTextFieldDriver itemIdField = new JTextFieldDriver(this, JTextField.class, named(MainWindow.NEW_ITEM_ID_NAME));
		//We have to focus 2 times to avoid bug in WindowLicker
		itemIdField.focusWithMouse();
		itemIdField.focusWithMouse();
		return itemIdField;
	}
	
	private JButtonDriver bidButton(){
		return new JButtonDriver(this, JButton.class, named(MainWindow.JOIN_BUTTON_NAME));
	}

}
