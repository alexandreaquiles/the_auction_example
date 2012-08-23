package br.com.alexandreaquiles.auctionsniper.ui;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class MainWindow extends JFrame {
	private static final String APPLICATION_TITLE = "Auction Sniper";

	private static final long serialVersionUID = 1L;

	public static final String MAIN_WINDOW_NAME = "Auction Sniper Main";
	public static final String SNIPER_TABLE_NAME = "sniper status";
	
	public static final String STATUS_JOINING = "Joining";
	public static final String STATUS_BIDDING = "Bidding";
	public static final String STATUS_WINNING = "Winning";
	
	public static final String STATUS_LOST = "Lost";
	public static final String STATUS_WON = "Won";
	
	private final SnipersTableModel snipers;

	public MainWindow(SnipersTableModel snipers) {
		super(APPLICATION_TITLE);
		this.snipers = snipers;
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

}
