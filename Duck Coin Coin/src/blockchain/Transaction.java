package blockchain;

public class Transaction {
	
	
	/* -- Instance variables of the Transaction objects -- */
	
	
	private int index;
	private String timestamp;
	private String sender;
	private String receiver;
	private int amount;
	private String senderSignature;
	
	
	/* -- TODO Constructors of the Transaction class -- */
	
	
	public Transaction() {}
	
	
	/* -- Methods -- */
	
	
	/* Getters */
	
	
	public int getIndex() {
		return index;
	}


	public String getTimestamp() {
		return timestamp;
	}


	public String getSender() {
		return sender;
	}


	public String getReceiver() {
		return receiver;
	}


	public int getAmount() {
		return amount;
	}


	public String getSenderSignature() {
		return senderSignature;
	}
	
	
	/* Setters - TODO */
	
	
	
	
}
