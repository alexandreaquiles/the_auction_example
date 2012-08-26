package br.com.alexandreaquiles.auctionsniper.ui;

import java.util.EventListener;

import br.com.alexandreaquiles.auctionsniper.Item;

public interface UserRequestListener extends EventListener {
	void joinAuction(Item item);
}
