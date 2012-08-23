package br.com.alexandreaquiles.auctionsniper;

import javax.swing.SwingUtilities;

import br.com.alexandreaquiles.auctionsniper.ui.SnipersTableModel;

public class SwingThreadSniperListener implements SniperListener {

	private SnipersTableModel snipers;

	public SwingThreadSniperListener(SnipersTableModel snipers) {
		this.snipers = snipers;
	}
	
	public void sniperStateChanged(final SniperSnapshot snapshot) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				snipers.sniperStateChanged(snapshot);
			}
		});
	}

}