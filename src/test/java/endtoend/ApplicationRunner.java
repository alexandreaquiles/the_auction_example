package endtoend;

import static br.com.alexandreaquiles.auctionsniper.ui.SnipersTableModel.textFor;
import br.com.alexandreaquiles.auctionsniper.Main;
import br.com.alexandreaquiles.auctionsniper.SniperState;
import br.com.alexandreaquiles.auctionsniper.ui.MainWindow;


public class ApplicationRunner {
	
	public static final String SNIPER_ID = "sniper";
	public static final String SNIPER_PASSWORD = "sniper";
	public static final String SNIPER_XMPP_ID = SNIPER_ID + "@" + FakeAuctionServer.XMPP_HOSTNAME + "/" + FakeAuctionServer.AUCTION_RESOURCE;
	
	private AuctionSniperDriver driver;

	public void startBiddingIn(final FakeAuctionServer... auctions) {
		startSniper(auctions);
		
		for (FakeAuctionServer auction : auctions) {
			String itemId = auction.getItemId();
			driver.startBiddingFor(itemId);
			driver.showsSniperStatus(itemId, 0, 0, textFor(SniperState.JOINING));
		}
		
	}

	private void startSniper(final FakeAuctionServer... auctions) {
		Thread thread = new Thread("Test Application"){
			public void run() {
				try {
					Main.main(arguments());
				} catch(Exception e){
					e.printStackTrace();
				}
			}
		};
		thread.setDaemon(true);
		thread.start();
		
		driver = new AuctionSniperDriver(1000);
		driver.hasTitle(MainWindow.APPLICATION_TITLE);
		driver.hasColumnTitles();
	}

	private String[] arguments() {
		String[] arguments = new String[3];
		arguments[0] = FakeAuctionServer.XMPP_HOSTNAME; 
		arguments[1] = SNIPER_ID; 
		arguments[2] = SNIPER_PASSWORD;
		return arguments;
	}

	public void stop() {
		if(driver != null){
			driver.dispose();
		}
	}

	public void hasShownSniperIsBidding(FakeAuctionServer auction, int lastPrice, int lastBid) {
		driver.showsSniperStatus(auction.getItemId(), lastPrice, lastBid, textFor(SniperState.BIDDING));
	}

	public void hasShownSniperIsWinning(FakeAuctionServer auction, int winningBid) {
		driver.showsSniperStatus(auction.getItemId(), winningBid, winningBid, textFor(SniperState.WINNING));
	}

	public void showsSniperHasLostAuction(FakeAuctionServer auction, int lastPrice, int lastBid) {
		driver.showsSniperStatus(auction.getItemId(), lastPrice, lastBid, textFor(SniperState.LOST));
	}

	public void showsSniperHasWonAuction(FakeAuctionServer auction, int lastPrice) {
		driver.showsSniperStatus(auction.getItemId(), lastPrice, lastPrice, textFor(SniperState.WON));
	}

}
