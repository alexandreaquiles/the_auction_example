package br.com.alexandreaquiles.auctionsniper.ui;

import javax.swing.SwingUtilities;

import br.com.alexandreaquiles.auctionsniper.SniperListener;
import br.com.alexandreaquiles.auctionsniper.SniperSnapshot;

public class SwingThreadSniperListener implements SniperListener {

	private SniperListener sniperListener;

	public SwingThreadSniperListener(SniperListener sniperListener) {
		this.sniperListener = sniperListener;
	}
	
	public void sniperStateChanged(final SniperSnapshot snapshot) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				sniperListener.sniperStateChanged(snapshot);
			}
		});
	}

}