package br.com.alexandreaquiles.auctionsniper.xmpp;

public class XMPPAuctionException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public XMPPAuctionException(String message, Exception exception) {
		super(message, exception);
	}

}
