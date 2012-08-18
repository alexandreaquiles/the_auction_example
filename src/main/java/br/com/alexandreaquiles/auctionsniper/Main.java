package br.com.alexandreaquiles.auctionsniper;

import javax.swing.SwingUtilities;

import br.com.alexandreaquiles.auctionsniper.ui.MainWindow;

public class Main {

	protected MainWindow ui;
	
	public Main() throws Exception {
		startUserInterface();
	}

	private void startUserInterface() throws Exception {
		SwingUtilities.invokeAndWait(new Runnable() {
			public void run() {
				ui = new MainWindow();
			}
		});
		
	}

	public static void main(String... args) throws Exception {
		Main main = new Main();
	}

}
