package br.com.alexandreaquiles.auctionsniper.ui;

import javax.swing.table.AbstractTableModel;

import br.com.alexandreaquiles.auctionsniper.SniperState;


public class SnipersTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private final static SniperState STARTING_UP = new SniperState("", 0, 0);
	
	private String statusText = MainWindow.STATUS_JOINING;
	private SniperState sniperState = STARTING_UP;

	public int getRowCount() {
		return 1;
	}

	public int getColumnCount() {
		return Column.values().length;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		return statusText;
	}

	public void setStatusText(String newStatusText) {
		statusText = newStatusText;
		fireTableRowsUpdated(0, 0);
	}

	public void sniperStatusChanged(SniperState state, String statusText) {
	}

}
