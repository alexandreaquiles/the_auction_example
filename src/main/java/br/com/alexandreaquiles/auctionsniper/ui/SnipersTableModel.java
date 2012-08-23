package br.com.alexandreaquiles.auctionsniper.ui;

import javax.swing.table.AbstractTableModel;

import br.com.alexandreaquiles.auctionsniper.Main;
import br.com.alexandreaquiles.auctionsniper.SniperSnapshot;
import br.com.alexandreaquiles.auctionsniper.SniperState;


public class SnipersTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private final static SniperSnapshot STARTING_UP = new SniperSnapshot("", 0, 0, SniperState.JOINING);
	private final static String[] STATUS_TEXT = { MainWindow.STATUS_JOINING, MainWindow.STATUS_BIDDING };
	
	private String statusText = MainWindow.STATUS_JOINING;
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
			return statusText;
		default:
			throw new IllegalArgumentException("No column at " + columnIndex);
		}
	}

	public void setStatusText(String newStatusText) {
		statusText = newStatusText;
		fireTableRowsUpdated(0, 0);
	}

	public void sniperStatusChanged(SniperSnapshot newSnapshot) {
		sniperSnapshot = newSnapshot;
		statusText = STATUS_TEXT[newSnapshot.state.ordinal()];
		fireTableRowsUpdated(0, 0);
	}

}
