package br.com.alexandreaquiles.auctionsniper.ui;

import javax.swing.table.AbstractTableModel;


public class SnipersTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	
	private String statusText = MainWindow.STATUS_JOINING;

	public int getRowCount() {
		return 1;
	}

	public int getColumnCount() {
		return 1;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		return statusText;
	}

	public void setStatusText(String newStatusText) {
		statusText = newStatusText;
		fireTableRowsUpdated(0, 0);
	}

}
