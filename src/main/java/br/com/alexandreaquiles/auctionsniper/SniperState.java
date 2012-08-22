package br.com.alexandreaquiles.auctionsniper;

public class SniperState {

	private final String itemId;
	private final int lastPrice;
	private final int lastBid;

	public SniperState(String itemId, int lastPrice, int lastBid) {
		this.itemId = itemId;
		this.lastPrice = lastPrice;
		this.lastBid = lastBid;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((itemId == null) ? 0 : itemId.hashCode());
		result = prime * result + lastBid;
		result = prime * result + lastPrice;
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SniperState other = (SniperState) obj;
		if (itemId == null) {
			if (other.itemId != null)
				return false;
		} else if (!itemId.equals(other.itemId))
			return false;
		if (lastBid != other.lastBid)
			return false;
		if (lastPrice != other.lastPrice)
			return false;
		return true;
	}

	public String toString() {
		return "SniperState [itemId=" + itemId + ", lastPrice=" + lastPrice
				+ ", lastBid=" + lastBid + "]";
	}

}
