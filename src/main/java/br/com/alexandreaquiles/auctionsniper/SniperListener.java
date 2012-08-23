package br.com.alexandreaquiles.auctionsniper;

import java.util.EventListener;

public interface SniperListener extends EventListener {
	void sniperLost();
	void sniperStateChanged(SniperSnapshot sniperState);
	void sniperWinning();
	void sniperWon();
}
