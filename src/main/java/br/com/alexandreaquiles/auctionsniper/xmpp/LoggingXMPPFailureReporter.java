package br.com.alexandreaquiles.auctionsniper.xmpp;

import java.util.logging.Logger;

public class LoggingXMPPFailureReporter implements XMPPFailureReporter {

	private Logger logger;

	public LoggingXMPPFailureReporter(Logger logger) {
		this.logger = logger;
	}

	public void cannotTranslateMessage(String auctionId, String failedMessage,	Exception exception) {
		logger.severe(String.format("<%s>Could not translate message \"%s\" because \"%s\"", auctionId, failedMessage, exception));
	}

}
