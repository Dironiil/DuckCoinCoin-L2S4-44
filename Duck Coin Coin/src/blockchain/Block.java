package blockchain;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Block {
	
	/* -- Constants of the block classes -- */
	
	private static final int lengthHash = 32;
	
	/* -- Instance variables of the block objects -- */
	
	/* Header of the block (hashed by the Sha256 algorithm */
	
	private int index;
	
	private String timestamp;
	
	private String lastHash;
	private String hashcode; // Special variable : in the header, but not hashed (you can't hash the hashcode itself...)
	
	private int nbTransactions;
	private String merkeRoot;
	
	private int nonce;
	
	/* Not header of the block (stored but not hashed) */
	
	private int difficulty;
	
	private Transaction[] transactions;
	//Alternative, si modification en cours d'existence du block : ArrayList<Transaction> transactions;
	
	
	/* -- Constructor of the Block class -- */
	
	
	/* Constructor of the "normal" blocks */
	
	/**
	 * Constructor of the Block class, with all instance variable explicitly set.
	 * @param index
	 * @param timestamp
	 * @param lastHash
	 * @param transactions
	 * @param difficulty
	 * @throws IllegalArgumentException If the lastHash argument is not of the correct length.
	 */
	public Block(int index, Date timestamp, String lastHash, Transaction[] transactions, int difficulty) {
		if (lastHash.length() != lengthHash) {
			throw new IllegalArgumentException("The last hash given to construct the block N°" + index + " is impossible.");
		}
		
		this.index = index;
		this.lastHash = lastHash;
		this.difficulty = difficulty;
		
		setTransactions(transactions);
		
		setTimestamp(timestamp);
		
		refreshHashcode();
	}
	
	/**
	 * Constructor of the Block class, with all instance variables explicitly set except
	 * the date that will be set at the time of the creation of the object.
	 * @param index
	 * @param lastHash
	 * @param transactions
	 * @param difficulty
	 * @throws IllegalArgumentException If the lastHash argument is not of the correct length/
	 */
	public Block(int index, String lastHash, Transaction[] transactions, int difficulty) {
		this(index, new Date(), lastHash, transactions, difficulty);
	}
	
	/* Constructors of the Genesys block (accessible only in the package) */
	
	/**
	 * Constructor of the Genesys block (using a pre-established date).
	 * @param timestamp
	 */
	protected Block(Date timestamp) {
		lastHash = "";
		for (int i = 0; i < lengthHash; ++i) {
			lastHash += "0";
		}
		
		index = 0;
		difficulty = 0;
		
		setTransactions(new Transaction[] {new Transaction("Génésis")});
		
		setTimestamp(timestamp);
		
		refreshHashcode();
	}
	
	/**
	 * Constructor of the genesys block.
	 */
	protected Block() {
		this(new Date());
	}
	
	
	/* -- Getters and setters of the class -- */
	
	
	/* Getters */
	
	
	static public int getHashcodeLength() {
		return lengthHash;
	}
	
	public String getHashcode() {
		return hashcode;
	}
	
	public int getIndex() {
		return index;
	}
	
	public String getLastHash() {
		return lastHash;
	}
	
	public String getMerkleRoot() {
		return merkeRoot;
	}
	
	public int getNbTransactions() {
		return nbTransactions;
	}
	
	public String getTimestamp() {
		return timestamp;
	}
	
	public Transaction[] getTransactions() {
		return transactions;
	}
	
	
	/* Setters */
	
	
	/**
	 * Setting the difficulty of the block and refreshing its hashcode.
	 * @param difficulty The new difficulty the block needs to respect.
	 */
	public void setDifficulty(int difficulty) { // Je ne suis pas sûr de si oui, ou non, on doit pouvoir modifier la difficulté après la création
		if (this.difficulty != difficulty) {
			this.difficulty = difficulty;
			refreshHashcode();
		}
	}
	
	/**
	 * Getting the difficulty of the block.
	 * @return The difficulty of the block.
	 */
	public int getDifficulty() {
		return difficulty;
	}
	
	
	/**
	 * Setting the index of the block and refreshing its hashcode.
	 * @param index The new index of the block in the blockchain.
	 */
	public void setIndex(int index) {
		if (this.index != index) {
			this.index = index;
			refreshHashcode();
		}
	}
	
	/**
	 * Setting the lastHash (hashof the precedent block) of this block and refreshing its hashcode.
	 * @param lastHash The new hash of the precedent block.
	 */
	public void setLastHash(String lastHash) {
		if (lastHash.length() != lengthHash) {
			throw new IllegalArgumentException("The last hash given to construct the block N°" + index + " is impossible.");
		}
		
		if (! this.lastHash.equals(lastHash)) {
			this.lastHash = lastHash;
			refreshHashcode();
		}
	}	
	
	/**
	 * Setting the String timestamp by giving it the date at which you wish to set it, then refreshes the hashcode of this block.
	 * @param date The new date at which the timestamp will be set.
	 */
	public void setTimestamp(Date date) {
		timestamp = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(date);
		refreshHashcode();
	}	
	
	/**
	 * Setting the transactions of the block, then refreshes the associated variable and the hashcode.
	 * @param transactions The new array of Transaction.
	 */
	public void setTransactions(Transaction[] transactions) {
		if (! this.transactions.equals(transactions)) {
			this.transactions = transactions;
			nbTransactions = transactions.length;
			
			//merkeRoot = merkleRoot(transactions);
			
			refreshHashcode();
		}
	}
	
	
	/* Other methods */
	
	
	/**
	 * <p>Refresh the hashcode of the blocks (Making it compulsory to be called after every changes in the block header).</p>
	 * <p>It will respect the current difficulty that the block has memorized (either at its creation or
	 * after the setting of it through {@link #setDifficulty(int) setDifficulty}) in this process.</p>
	 */
	private void refreshHashcode() {
		nonce = 0;
		
		StringBuffer stringDifficulty = new StringBuffer();
		for (int i = 0; i < difficulty; i++) {
			stringDifficulty.append(0);
		}
		
		// hashCode = hash();
		while(! hashcode.substring(0, difficulty).equals(stringDifficulty.toString())) {
			++nonce;
			//hashCode = hash();
		}
	}
	
	// Fonction pour rafraichir l'arbre de Merkle du block après un changement de transactions
	// public void refreshMerkleRoot() {}
	
	/* Particular method toString() */
	
	@Override
	public String toString() {
		String s = "Block indexed " + index + " timestamped " + timestamp + ", with a Hashcode : " + hashcode + ". Contents : \n" +
				"Hash of the last block : " + lastHash + "\n" +
				"Merkle root of the " + nbTransactions + " transaction(s) : " + merkeRoot + "\n" +
				"Difficulty of the block : " + difficulty + ", obtained with the nonce : " + nonce + "\n";
		return s;
	}
}
