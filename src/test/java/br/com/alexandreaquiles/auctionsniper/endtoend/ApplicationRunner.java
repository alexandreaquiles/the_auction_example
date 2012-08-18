package br.com.alexandreaquiles.auctionsniper.endtoend;

import br.com.alexandreaquiles.auctionsniper.Main;
import br.com.alexandreaquiles.auctionsniper.ui.MainWindow;

public class ApplicationRunner {
	
	public static final String SNIPER_ID = "sniper";
	public static final String SNIPER_PASSWORD = "sniper";
	public static final String SNIPER_XMPP_ID = SNIPER_ID + "@" + FakeAuctionServer.XMPP_HOSTNAME + "/" + FakeAuctionServer.AUCTION_RESOURCE;
	
	private AuctionSniperDriver driver;

	public void startBiddingIn(final FakeAuctionServer auction) {
		Thread thread = new Thread("Test Application"){
			@Override
			public void run() {
				try {
					Main.main(FakeAuctionServer.XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD, auction.getItemId());
				} catch(Exception e){
					e.printStackTrace();
				}
			}
		};
		thread.setDaemon(true);
		thread.start();
		
		driver = new AuctionSniperDriver(1000);
		driver.showsSniperStatus(MainWindow.STATUS_JOINING);
		
	}

	public void showsSniperHasLostAuction() {
		driver.showsSniperStatus(MainWindow.STATUS_LOST);
	}

	public void stop() {
		if(driver != null){
			driver.dispose();
		}
	}

	public void hasShownSniperIsBidding() {
	}

}
