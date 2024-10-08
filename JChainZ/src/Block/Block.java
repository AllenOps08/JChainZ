package Block;

import Transactions.Transaction;
import Utils.Utils;
import  java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Block {

    private String hash;
    private String previousHash;
    private String merkleRoot;
    private List<Transaction> transactions = new ArrayList<>();
    private  long timeStamp;
    private  int nonce;

//    Block Constructor
    public Block(String previousHash){
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash();
    }


//    Calculate new hash based on block's contents
    public String calculateHash(){
        return Utils.applySha256(
                previousHash+Long.toString(timeStamp)+Integer.toString(nonce)+merkleRoot
        );
    }


    //Mine Block by finding the hash with the appropriate difficulty prefix
    public void mineBlock(int difficulty) {
        String target = new String(new char[difficulty]).replace('\0', '0'); // Create a string with difficulty * "0"
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
        System.out.println("Block Mined!!! : " + hash);
    }

    // Add transactions to the block
    public boolean addTransaction(Transaction transaction) {
        if (transaction == null) return false;

        // Process transaction and check if valid, except for genesis block
        if (!previousHash.equals("0")) {
            if (!transaction.processTransaction()) {
                System.out.println("Transaction failed to process. Discarded.");
                return false;
            }
        }

        transactions.add(transaction);
        System.out.println("Transaction successfully added to Block.");
        return true;
    }


    //Getters
    public String getHash(){
        return hash ;
    }

    public String getPreviousHash(){
        return previousHash;
    }


    public List<Transaction> getTransactions() {
        return transactions;
    }
}
