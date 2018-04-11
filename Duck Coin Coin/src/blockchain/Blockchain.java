package blockchain;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

public class Blockchain {
	
	/* -- Instance variable of the Blockchain objects -- */
	
	
	private int difficulty;
	private int numberBlocks; // Pas nécessaire : LinkedList a déjà une méthode size(). Le garde-t-on ?
	private LinkedList<Block> blockchain;
	
	
	/* -- Constructors of the Blockchain classes -- */
	
	
	/**
	 * Constructor of the blockchain, creating the genesys block with the blockchain itself
	 * @param difficulty The difficulty the blocks of the blockchain must respect
	 */
	public Blockchain(int difficulty) {
		this.difficulty = difficulty;
		
		blockchain = new LinkedList<>();
		blockchain.add(Block.genesys());
		
		numberBlocks = blockchain.size(); // CF le commentaire sur numberBlocks
	}
	
	
	/* -- Getters, Setters and other methods -- */
	
	
	/* Getters */
	
	
	public int getDifficulty() {
		return difficulty;
	}
	
	public Block getBlock(int i) {
		return blockchain.get(i);
	}
	
	public int getBlockchainSize() {
		return numberBlocks;
		//return blockchain.size();
	}
	
	
	/* Setters */
	
	
	/**
	 * Set the difficulty of the blockchain at the given integer, and refresh all the hashcodes of the concerned block
	 * @param difficulty
	 */
	public void setDifficulty(int difficulty) { // Je ne suis pas sûr de si oui, ou non, il doit être possible de modifier la difficulté après création de la chaine
		this.difficulty = difficulty;
		for(int i = 1; i < blockchain.size(); ++i) { // i = 1 because block 0 is the genesys, with a nonce of 0
			blockchain.get(i).setDifficulty(difficulty);
		}
	}
	
	
	/**
	 * Overloaded method addblock, taking a block and modifying it to add it at the end of the blockchain
	 * @param block
	 */
	public void addBlock(Block block) {
		block.setDifficulty(difficulty);
		block.setIndex(blockchain.size());
		block.setLastHash(blockchain.get(blockchain.size() - 1).getHashcode());
		
		blockchain.add(block);
		numberBlocks = blockchain.size();
	}
	
	/**
	 * Overloaded method addblock, taking a String arraylist (as a transactions list) to add a block to the blockchain at the current date
	 * @param index
	 * @param lastHash
	 * @param transactions
	 * @param difficulty
	 */
	public void addBlock(ArrayList<String> transactions) {
		int index = blockchain.size();
		String lastHash = blockchain.get(index - 1).getHashcode();
		blockchain.add(new Block(index, lastHash, transactions, difficulty));
		
		numberBlocks = blockchain.size();
	}
	
	/**
	 * Overloaded method addblock, taking a timestamp and a String arraylist (as a transactions list) to add a block to the blockchain
	 * @param index
	 * @param timestamp
	 * @param lastHash
	 * @param transactions
	 * @param difficulty
	 */
	public void addBlock(Date timestamp, ArrayList<String> transactions) {
		int index = blockchain.size();
		String lastHash = blockchain.get(index - 1).getHashcode();
		blockchain.add(new Block(index, timestamp, lastHash, transactions, difficulty));
		
		numberBlocks = blockchain.size();
	}
	
	// TODO Add block at index
	
}
