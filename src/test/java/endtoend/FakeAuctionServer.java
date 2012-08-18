package endtoend;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.hamcrest.Matcher;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import br.com.alexandreaquiles.auctionsniper.Main;

public class FakeAuctionServer {
	
	public static final String XMPP_HOSTNAME = "localhost";
	public static final String AUCTION_RESOURCE = "Auction";

	private static final String ITEM_ID_AS_LOGIN = "auction-%s";
	private static final String AUCTION_PASSWORD = "auction";

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
		receivesAMessageMatching(sniperXmppId, equalTo(Main.JOIN_COMMAND_FORMAT));
	}

	public void announceClosed() throws XMPPException {
		currentChat.sendMessage(new Message());
	}

	public void stop() {
		connection.disconnect();
	}

	public String getItemId() {
		return itemId;
	}

	public void reportPrice(int price, int increment, String bidder) throws XMPPException {
		currentChat.sendMessage(String.format(Main.PRICE_COMMAND_FORMAT, price, increment, bidder));
	}

	public void hasReceivedBid(int bid, String sniperXmppId) throws InterruptedException {
		receivesAMessageMatching(sniperXmppId, equalTo(String.format(Main.BID_COMMAND_FORMAT, bid)));
	}
	
	private void receivesAMessageMatching(String sniperXmppId, Matcher<String> equalTo) throws InterruptedException {
		messageListener.receivesAMessage(equalTo);
		assertThat(currentChat.getParticipant(), equalTo(sniperXmppId));
	}

}

@SuppressWarnings({ "unchecked", "rawtypes" })
class SingleMessageListener implements MessageListener {
	
	private final ArrayBlockingQueue<Message> messages = new ArrayBlockingQueue<Message>(1);

	public void processMessage(Chat chat, Message message) {
		messages.add(message);
	}

	public void receivesAMessage(Matcher messageMatcher) throws InterruptedException {
		final Message message = messages.poll(5, TimeUnit.SECONDS);
		assertThat("Message", message, is(notNullValue()));
		assertThat(message.getBody(), messageMatcher);
	}
	
}