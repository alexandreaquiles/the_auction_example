package br.com.alexandreaquiles.auctionsniper;

import java.util.ArrayList;
import java.util.List;

import br.com.alexandreaquiles.auctionsniper.util.Announcer;

public class SniperPortifolio implements SniperCollector {
	
	List<AuctionSniper> snipers = new ArrayList<AuctionSniper>(); 
	Announcer<PortfolioListener> listeners = Announcer.to(PortfolioListener.class);

	public void addSniper(AuctionSniper sniper) {
		snipers.add(sniper);
		listeners.announce().sniperAdded(sniper);
	}

	public void addPortfolioListener(PortfolioListener portifolio) {
		listeners.addListener(portifolio);
	}

}
