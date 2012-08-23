package br.com.alexandreaquiles.auctionsniper.ui;

import br.com.alexandreaquiles.auctionsniper.SniperSnapshot;

public enum Column {

	ITEM_IDENTIFIER {
		public Object valueIn(SniperSnapshot snapshot) {
			return snapshot.itemId;
		}
	},
	LAST_PRICE {
		public Object valueIn(SniperSnapshot snapshot) {
			return snapshot.lastPrice;
		}
	},
	LAST_BID {
		public Object valueIn(SniperSnapshot snapshot) {
			return snapshot.lastBid;
		}
	},
	SNIPER_STATE {
		public Object valueIn(SniperSnapshot snapshot) {
			return SnipersTableModel.textFor(snapshot.state);
		}
	};
	
	public static Column at(int offset) { 
		return values()[offset]; 
	}
	
	abstract public Object valueIn(SniperSnapshot snapshot);
}
