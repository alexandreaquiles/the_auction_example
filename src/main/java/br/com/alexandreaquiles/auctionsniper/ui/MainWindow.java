package br.com.alexandreaquiles.auctionsniper.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import br.com.alexandreaquiles.auctionsniper.SniperPortifolio;
import br.com.alexandreaquiles.auctionsniper.util.Announcer;

public class MainWindow extends JFrame {
	private static final long serialVersionUID = 1L;

	public static final String APPLICATION_TITLE = "Auction Sniper";
	public static final String MAIN_WINDOW_NAME = "Auction Sniper Main";
	public static final String SNIPER_TABLE_NAME = "sniper status";
	public static final String NEW_ITEM_ID_NAME = "item id";
	public static final String JOIN_BUTTON_NAME = "join";
	
	private final Announcer<UserRequestListener> userRequests = Announcer.to(UserRequestListener.class);

	public MainWindow(SniperPortifolio portifolio) {
		super(APPLICATION_TITLE);
		setName(MAIN_WINDOW_NAME);
		fillContentPane(makeSnipersTable(portifolio), makeControls());
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	private void fillContentPane(JTable snipersTable, JPanel controlsPanel) {
		final Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
	    contentPane.add(controlsPanel, BorderLayout.NORTH); 
		contentPane.add(new JScrollPane(snipersTable), BorderLayout.CENTER);
	}

	private JTable makeSnipersTable(SniperPortifolio portifolio) {
		SnipersTableModel model = new SnipersTableModel();
		portifolio.addPortfolioListener(model);
		final JTable snipersTable = new JTable(model);
		snipersTable.setName(SNIPER_TABLE_NAME);
		return snipersTable;
	}

	private JPanel makeControls() {
		JPanel controls = new JPanel(new FlowLayout());
		
		final JTextField itemIdField = new JTextField();
		itemIdField.setColumns(25);
		itemIdField.setName(NEW_ITEM_ID_NAME);
		controls.add(itemIdField);
		
		final JButton joinAuctionButton = new JButton("Join Auction");
		joinAuctionButton.setName(JOIN_BUTTON_NAME);
		joinAuctionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				userRequests.announce().joinAuction(itemIdField.getText());
			}
		});
		controls.add(joinAuctionButton);
		
		return controls;
	}

	public void addUserRequestListener(UserRequestListener userRequestListener) {
		userRequests.addListener(userRequestListener);
	}

}
