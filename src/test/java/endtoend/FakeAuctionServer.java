package endtoend;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.hamcrest.Matcher;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import br.com.alexandreaquiles.auctionsniper.xmpp.XMPPAuction;

public class FakeAuctionServer {
	
	private static final String PRICE_COMMAND_FORMAT = "SOLVersion: 1.1; Event: PRICE; CurrentPrice: %d; Increment: %d; Bidder: %s;";
	private static final String CLOSE_COMMAND_FORMAT = "SOLVersion: 1.1; Event: CLOSE;";

	
	public static final String XMPP_HOSTNAME = "localhost";
	public static final String AUCTION_RESOURCE = "Auction";

	public static final String ITEM_ID_AS_LOGIN = "auction-%s";
	public static final String AUCTION_PASSWORD = "auction";

	private final SingleMessageListener messageListener = new SingleMessageListener();
	
	private String itemId;
	private XMPPConnection connection;
	private Chat currentChat;


	public FakeAuctionServer(String itemId) {
		this.itemId = itemId;
		this.connection = new XMPPConnection(XMPP_HOSTNAME);
	}

	public void startSellingItem() throws XMPPException {
		connection.connect();
		String username = String.format(ITEM_ID_AS_LOGIN, itemId);
		connection.login(username, AUCTION_PASSWORD, AUCTION_RESOURCE);
		connection.getChatManager().addChatListener(
				new ChatManagerListener() {
					public void chatCreated(Chat chat, boolean createdLocally) {
						currentChat = chat;
						chat.addMessageListener(messageListener);
					}
				}
		);
	}

	public void hasReceivedJoinRequestFrom(String sniperXmppId) throws InterruptedException {
		receivesAMessageMatching(sniperXmppId, equalTo(XMPPAuction.JOIN_COMMAND_FORMAT));
	}

	public void announceClosed() throws XMPPException {
		currentChat.sendMessage(CLOSE_COMMAND_FORMAT);
	}

	public void stop() {
		connection.disconnect();
	}

	public String getItemId() {
		return itemId;
	}

	public void reportPrice(int price, int increment, String bidder) throws XMPPException {
		currentChat.sendMessage(String.format(PRICE_COMMAND_FORMAT, price, increment, bidder));
	}

	public void hasReceivedBid(int bid, String sniperXmppId) throws InterruptedException {
		receivesAMessageMatching(sniperXmppId, equalTo(String.format(XMPPAuction.BID_COMMAND_FORMAT, bid)));
	}
	
	private void receivesAMessageMatching(String sniperXmppId, Matcher<String> messageMatcher) throws InterruptedException {
		messageListener.receivesAMessage(messageMatcher);
		assertThat(currentChat.getParticipant(), equalTo(sniperXmppId));
	}

}

@SuppressWarnings("rawtypes")
class SingleMessageListener implements MessageListener {
	
	private final ArrayBlockingQueue<Message> messages = new ArrayBlockingQueue<Message>(1);

	public void processMessage(Chat chat, Message message) {
		messages.add(message);
	}

	public void receivesAMessage(Matcher messageMatcher) throws InterruptedException {
		final Message message = messages.poll(5, TimeUnit.SECONDS);
		assertThat(message, hasProperty("body",  messageMatcher));
	}
	
}