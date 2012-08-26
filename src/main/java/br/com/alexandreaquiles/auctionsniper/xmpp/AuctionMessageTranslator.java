package br.com.alexandreaquiles.auctionsniper.xmpp;

import java.util.HashMap;
import java.util.Map;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

import br.com.alexandreaquiles.auctionsniper.AuctionEventListener;
import br.com.alexandreaquiles.auctionsniper.AuctionEventListener.PriceSource;

public class AuctionMessageTranslator implements MessageListener {

	private final String sniperId;
	private final AuctionEventListener listener;

	public AuctionMessageTranslator(String sniperId, AuctionEventListener listener) {
		this.sniperId = sniperId;
		this.listener = listener;
	}

	public void processMessage(Chat chat, Message message) {
		AuctionEvent event = AuctionEvent.from(message.getBody());
		String eventType = event.type();
		if("CLOSE".equals(eventType)){
			listener.auctionClosed();
		} else if("PRICE".equals(eventType)){
			listener.currentPrice(event.currentPrice(), event.increment(), event.isFrom(sniperId));
		}
	}

	private static class AuctionEvent {
		private Map<String, String> fields = new HashMap<String, String>();
		
		public String type() { 
			return get("Event"); 
		}
		
		public int currentPrice(){
			return getInt("CurrentPrice");
		}
		
		public int increment(){
			return getInt("Increment");
		}

		public PriceSource isFrom(String sniperId) {
			return sniperId.equals(bidder()) ? PriceSource.FromSniper : PriceSource.FromOtherBidder;
		}

		private String bidder() {
			return get("Bidder");
		}

		private int getInt(String fieldName) {
			return Integer.parseInt(get(fieldName));
		}

		private String get(String fieldName) {
			return fields.get(fieldName);
		}
		
		private void addField(String field){
			String[] pair = field.split(":");
			fields.put(pair[0].trim(), pair[1].trim());
		}
		
		static AuctionEvent from(String messageBody){
			AuctionEvent event = new AuctionEvent();
			for(String field : fieldsIn(messageBody)){
				event.addField(field);
			}
			return event;
		}

		private static String[] fieldsIn(String messageBody) {
			return messageBody.split(";");
		}
		
	}

}
