package br.com.alexandreaquiles.auctionsniper.ui;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import br.com.alexandreaquiles.auctionsniper.SniperState;

public class MainWindow extends JFrame {
	private static final String APPLICATION_TITLE = "Auction Sniper";

	private static final long serialVersionUID = 1L;

	public static final String MAIN_WINDOW_NAME = "Auction Sniper Main";
	public static final String SNIPER_TABLE_NAME = "sniper status";
	
	public static final String STATUS_JOINING = "JOINING";
	public static final String STATUS_BIDDING = "BIDDING";
	public static final String STATUS_WINNING = "WINNING";
	
	public static final String STATUS_LOST = "LOST";
	public static final String STATUS_WON = "WON";
	
	private final SnipersTableModel snipers = new SnipersTableModel();

	public MainWindow() {
		super(APPLICATION_TITLE);
		setName(MAIN_WINDOW_NAME);
		fillContentPane(makeSnipersTable());
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	private void fillContentPane(JTable snipersTable) {
		final Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		
		contentPane.add(new JScrollPane(snipersTable), BorderLayout.CENTER);
	}

	private JTable makeSnipersTable() {
		final JTable snipersTable = new JTable(snipers);
		snipersTable.setName(SNIPER_TABLE_NAME);
		return snipersTable;
	}

	public void showStatus(String statusText) {
		snipers.setStatusText(statusText);
	}

	public void sniperStatusChanged(SniperState state, String statusText) {
		snipers.sniperStatusChanged(state, statusText);
		
	}

}
