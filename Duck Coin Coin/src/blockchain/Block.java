package blockchain;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Block {
	
	/* -- Constants of the block classes -- */
	
	public static final int lengthHash = 32;
	
	/* -- Instance variables of the block objects -- */
	
	/* Header of the block (hashed by the Sha256 algorithm) */
	
	private int index;
	private String timestamp;
	private String lastHash;
	private String hashcode; // Special variable : in the header, but not hashed (you can't hash the hashcode itself...)
	private int nbTransactions;
	private String merkeRoot;
	private int nonce;
	
	/* Not header of the block (stored but not hashed) */
	
	private int difficulty;
	private ArrayList<String> transactions; // A transformer en ArrayList<Transaction> à l'étape 2
	
	
	/* -- Constructor of the Block class -- */
	
	
	/* Constructor of the "normal" blocks */
	
	/**
	 * Constructor of the Block class, with all instance variable explicitly set.
	 * @param index The index of the Block
	 * @param timestamp Time of creation of this block
	 * @param lastHash The hash of the precedent block in the list
	 * @param transactions The list of the transactions stored in the block
	 * @param difficulty The difficulty to hash the block
	 * @throws IllegalArgumentException If the lastHash argument is not of the correct length.
	 */
	public Block(int index, Date timestamp, String lastHash, ArrayList<String> transactions, int difficulty) {
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
	 * @param index The index of the block
	 * @param lastHash The hash of the precedent block in the list
	 * @param transactions The list of the transactions stored in the block
	 * @param difficulty The difficulty to hash the block
	 * @throws IllegalArgumentException If the lastHash argument is not of the correct length/
	 */
	public Block(int index, String lastHash, ArrayList<String> transactions, int difficulty) {
		this(index, new Date(), lastHash, transactions, difficulty);
	}
	
	/**
	 * Constructor of the Block class, timestamped at a given point in time.
	 * @param lastBlock The precedent block in blockchain.
	 * @param timestamp Time of creation of this block
	 * @param transactions The list of the transactions stored in the block
	 * @param difficulty The difficulty to hash the block
	 */
	public Block(Block lastBlock, Date timestamp, ArrayList<String> transactions, int difficulty) {
		this(lastBlock.getIndex() + 1, timestamp, lastBlock.getHashcode(), transactions, difficulty);
	}
	
	/**
	 * Constructor of the Block class, timestamped at the current time and using a Block reference to set its
	 * lastHash and index value.
	 * @param lastBlock The precedent block in blockchain.
	 * @param transactions The list of the transactions stored in the block
	 * @param difficulty The difficulty to hash the block
	 */
	public Block (Block lastBlock, ArrayList<String> transactions, int difficulty) {
		this(lastBlock, new Date(), transactions, difficulty);
	}
	
	/* Static "constructors" of the Genesys block (accessible only in the package or by daughter classes) */
	
	/**
	 * <p>Overloaded static constructor of the Genesys block (using a pre-established date).</p>
	 * <p>/!\ PACKAGE ONLY METHOD</p>
	 * @param timestamp The time of the creation of this block
	 * @return The genesys block
	 */
	static Block genesys(Date timestamp) {
		String lastHash = "";
		for (int i = 0; i < lengthHash; ++i) {
			lastHash += "0";
		}
		
		ArrayList<String> transactions = new ArrayList<>();
		transactions.add("Genesis");
		
		return new Block(0, timestamp, lastHash, transactions, 0);
	}
	
	/**
	 * <p>Overloaded static constructor of the genesys block (timestamped at the current time).</p>
	 * <p>/!\ PACKAGE ONLY METHOD</p>
	 * @return The genesys block
	 */
	static Block genesys() {
		return Block.genesys(new Date());
	}
	
	
	/* -- Getters and setters of the class -- */
	
	
	/* Getters */
	
	
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
	
	public ArrayList<String> getTransactions() {
		return transactions;
	}
	
	public int getDifficulty() {
		return difficulty;
	}
	
	
	/* Setters - JE NE SAIS VRAIMENT PAS LESQUELS DOIVENT REELEMENT ETRE DISPONIBLES (après tout, un bloc n'est pas sensé être modifié après création) */
	
	
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
	public void setTransactions(ArrayList<String> transactions) {
		if (! this.transactions.equals(transactions)) {
			this.transactions = transactions;
			nbTransactions = transactions.size();
			
			refreshMerkleRoot();			
			refreshHashcode();
		}
	}
	
	/**
	 * Adding a transaction to the transaction list of the block, refreshing the Merkle root and the hashcode
	 * in the process.
	 * @param transaction The transaction to add.
	 */
	public void addTransaction(String transaction) {
		transactions.add(transaction);
		refreshMerkleRoot();
		refreshHashcode();
	}
	
	
	/* Other methods */
	
	
	/**
	 * <p>Refresh the hashcode of the blocks (Making it compulsory to be called after every changes in the block header).</p>
	 * <p>It will respect the current difficulty that the block has memorized (either at its creation or
	 * after the setting of it through {@link #setDifficulty(int) setDifficulty}) in this process.</p>
	 */
	private void refreshHashcode() {
		// @Xavier Ce que j'ai écrit en dessous est une proposition, fais en ce que tu en souhaites !
		
		nonce = 0;
		
		String stringDifficulty = "";
		for (int i = 0; i < difficulty; i++) {
			stringDifficulty += "0";
		}
		
		// hashCode = hash();
		while(! hashcode.substring(0, difficulty).equals(stringDifficulty.toString())) {
			++nonce;
			//hashCode = hash();
		}
	}
	
	/**
	 * Fonction pour rafraichir l'arbre de Merkle du block après un changement de transactions
	 */
	private void refreshMerkleRoot() {
		//TODO
	}
	
	/* Particular method toString() */
	
	@Override
	public String toString() { // Simplifiable ou modifiable à souhait selon les besoins, c'est juste un premier jet
		String s = "Block indexed " + index + " timestamped " + timestamp + ", with a Hashcode : " + hashcode + ". Contents : \n" +
				"Hash of the last block : " + lastHash + "\n" +
				"Merkle root of the " + nbTransactions + " transaction(s) : " + merkeRoot + "\n" +
				"Difficulty of the block : " + difficulty + ", obtained with the nonce : " + nonce + "\n";
		return s;
	}
}
