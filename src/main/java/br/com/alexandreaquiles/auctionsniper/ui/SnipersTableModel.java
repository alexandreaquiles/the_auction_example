package br.com.alexandreaquiles.auctionsniper.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import br.com.alexandreaquiles.auctionsniper.AuctionSniper;
import br.com.alexandreaquiles.auctionsniper.SniperCollector;
import br.com.alexandreaquiles.auctionsniper.SniperListener;
import br.com.alexandreaquiles.auctionsniper.SniperSnapshot;
import br.com.alexandreaquiles.auctionsniper.SniperState;
import br.com.alexandreaquiles.auctionsniper.util.Defect;


public class SnipersTableModel extends AbstractTableModel implements SniperListener, SniperCollector {

	private static final long serialVersionUID = 1L;
	private final static String[] STATUS_TEXT = { "Joining", "Bidding", "Winning", "Lost", "Won" };

	private final List<AuctionSniper> notToBeGCD = new ArrayList<AuctionSniper>();
	private final List<SniperSnapshot> snapshots = new ArrayList<SniperSnapshot>();

	public int getRowCount() {
		return snapshots.size();
	}

	public int getColumnCount() {
		return Column.values().length;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		return Column.at(columnIndex).valueIn(snapshots.get(rowIndex));
	}

	public String getColumnName(int columnIndex) {
		return Column.at(columnIndex).name;
	}
	
	public void sniperStateChanged(SniperSnapshot newSnapshot) {
		for (int i = 0; i < snapshots.size(); i++) {
			if(newSnapshot.isForSameItemAs(snapshots.get(i))){
				snapshots.set(i, newSnapshot);
				fireTableRowsUpdated(i, i);
				return;
			}
		}
		throw new Defect("No existing sniper snapshot for " + newSnapshot.itemId);
	}

	public static String textFor(SniperState state) {
		return STATUS_TEXT[state.ordinal()];
	}

	public void addSniperSnapshot(SniperSnapshot joiningSniper) {
		int row = snapshots.size();
		snapshots.add(joiningSniper);
		fireTableRowsInserted(row, row);
	}

	public void addSniper(AuctionSniper sniper) {
		notToBeGCD.add(sniper);
		addSniperSnapshot(sniper.getSnapshot());
		sniper.addSniperListener(new SwingThreadSniperListener(this));
	}

}
