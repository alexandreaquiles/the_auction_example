package br.com.alexandreaquiles.auctionsniper.ui;

import javax.swing.table.AbstractTableModel;

import br.com.alexandreaquiles.auctionsniper.SniperListener;
import br.com.alexandreaquiles.auctionsniper.SniperSnapshot;
import br.com.alexandreaquiles.auctionsniper.SniperState;


public class SnipersTableModel extends AbstractTableModel implements SniperListener {

	private static final long serialVersionUID = 1L;
	private final static String[] STATUS_TEXT = { "Joining", "Bidding", "Winning", "Lost", "Won" };

	public final static SniperSnapshot JOINING = new SniperSnapshot("", 0, 0, SniperState.JOINING);
	
	private SniperSnapshot sniperSnapshot = JOINING;

	public int getRowCount() {
		return 1;
	}

	public int getColumnCount() {
		return Column.values().length;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		return Column.at(columnIndex).valueIn(sniperSnapshot);
	}

	public String getColumnName(int columnIndex) {
		return Column.at(columnIndex).name;
	}
	
	public void sniperStateChanged(SniperSnapshot newSnapshot) {
		this.sniperSnapshot = newSnapshot;
		fireTableRowsUpdated(0, 0);
	}

	public static String textFor(SniperState state) {
		return STATUS_TEXT[state.ordinal()];
	}

}
