package br.com.alexandreaquiles.auctionsniper.ui;

import javax.swing.table.AbstractTableModel;

import br.com.alexandreaquiles.auctionsniper.SniperSnapshot;
import br.com.alexandreaquiles.auctionsniper.SniperState;


public class SnipersTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private final static SniperSnapshot STARTING_UP = new SniperSnapshot("", 0, 0, SniperState.JOINING);
	private final static String[] STATUS_TEXT = { "Joining", "Bidding", "Winning", "Lost", "Won" };
	
	private SniperSnapshot sniperSnapshot = STARTING_UP;

	public int getRowCount() {
		return 1;
	}

	public int getColumnCount() {
		return Column.values().length;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		switch(Column.at(columnIndex)){
		case ITEM_IDENTIFIER:
			return sniperSnapshot.itemId;
		case LAST_PRICE:
			return sniperSnapshot.lastPrice;
		case LAST_BID:
			return sniperSnapshot.lastBid;
		case SNIPER_STATE:
			return textFor(sniperSnapshot.state);
		default:
			throw new IllegalArgumentException("No column at " + columnIndex);
		}
	}

	public void sniperStatusChanged(SniperSnapshot newSnapshot) {
		this.sniperSnapshot = newSnapshot;
		fireTableRowsUpdated(0, 0);
	}

	private String textFor(SniperState state) {
		return STATUS_TEXT[state.ordinal()];
	}

}
