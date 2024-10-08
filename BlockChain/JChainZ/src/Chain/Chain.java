package Chain;

import Block.Block;
import Transactions.Transaction;
import Transactions.TransactionOutput;
import Transactions.TransactionInput;
import  Wallet.Wallet ;

import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class Chain {

    public  static List<Block> blockchain = new ArrayList<>();
    public static Map<String, TransactionOutput> UTX0s = new HashMap<>();


    public static  int difficulty = 3;
    public  static  float minimumTransaction = 0.1f ;
    public  static  Wallet walletA ;
    public  static  Wallet walletB;
    public static  Transaction genesisTransaction;


    public static void main(String[] args) {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());


//        Create Wallets
         walletA = new Wallet();
         walletB = new Wallet();
         Wallet coinbase = new Wallet();



        // Create genesis transaction, which sends 100 coins to walletA:
        genesisTransaction = new Transaction(coinbase.getPublicKey(), walletA.getPublicKey(), 100f, null);
        genesisTransaction.generateSignature(coinbase.getPrivateKey());  // Manually sign the genesis transaction
        genesisTransaction.getOutputs().add(new TransactionOutput(genesisTransaction.getRecipient(), genesisTransaction.getValue(), genesisTransaction.getTransactionId()));
        UTX0s.put(genesisTransaction.getOutputs().get(0).getId(), genesisTransaction.getOutputs().get(0)); // It's important to store our first transaction in the UTXOs list.

        System.out.println("Creating and Mining Genesis block...");
        Block genesis = new Block("0");
        genesis.addTransaction(genesisTransaction);
        addBlock(genesis);

        // Testing
        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("WalletA is attempting to send funds (40) to WalletB...");
        Block block1 = new Block(genesis.getHash());
        block1.addTransaction(walletA.sendFunds(walletB.getPublicKey(), 40f));
        addBlock(block1);
        printWalletBalances();
        System.out.println("\nWalletA attempting to send more funds (1000) than it has...");
        Block block2 = new Block(block1.getHash());
        block2.addTransaction(walletA.sendFunds(walletB.getPublicKey(), 1000f));
        addBlock(block2);
        printWalletBalances();

        System.out.println("\nWalletB is attempting to send funds (20) to WalletA...");
        Block block3 = new Block(block2.getHash());
        block3.addTransaction(walletB.sendFunds(walletA.getPublicKey(), 20f));
        addBlock(block3);
        printWalletBalances();

        validateBlockchain();

    }

    //Add block to the blockchain and mine it
    public static void addBlock(Block newBlock){
        newBlock.mineBlock(difficulty);
        blockchain.add(newBlock);
    }


    //Print balances of WalletA and WalletB
    private static void printWalletBalances(){
        System.out.println("WalletA's balance is: "+ walletA.getBalance());
        System.out.println("WalletB's balance is: "+ walletB.getBalance());
    }

    // Validates the entire blockchain
    public static boolean validateBlockchain() {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');
        Map<String, TransactionOutput> tempUTXOs = new HashMap<>();
        tempUTXOs.put(genesisTransaction.getOutputs().get(0).getId(), genesisTransaction.getOutputs().get(0));

        // Loop through the blockchain to check hashes:
        for (int i = 1; i < blockchain.size(); i++) {
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i - 1);

            // Compare registered hash and calculated hash:
            if (!currentBlock.getHash().equals(currentBlock.calculateHash())) {
                System.out.println("#Current Hashes not equal");
                return false;
            }

            // Compare previous hash and registered previous hash
            if (!previousBlock.getHash().equals(currentBlock.getPreviousHash())) {
                System.out.println("#Previous Hashes not equal");
                return false;
            }

            // Check if hash is solved:
            if (!currentBlock.getHash().substring(0, difficulty).equals(hashTarget)) {
                System.out.println("#This block hasn't been mined");
                return false;
            }

            // Loop through blockchains transactions:
            for (Transaction transaction : currentBlock.getTransactions()) {
                if (!transaction.verifySignature()) {
                    System.out.println("#Signature on Transaction(" + transaction.getTransactionId() + ") is Invalid");
                    return false;
                }
                if (transaction.getInputs() != null) {
                    for (TransactionInput input : transaction.getInputs()) {
                        TransactionOutput tempOutput = tempUTXOs.get(input.getTransactionOutputId());
                        if (tempOutput == null) {
                            System.out.println("#Referenced input on Transaction(" + transaction.getTransactionId() + ") is Missing");
                            return false;
                        }

                        if (input.UTX0.getValue() != tempOutput.getValue()) {
                            System.out.println("#Referenced input Transaction(" + transaction.getTransactionId() + ") value is Invalid");
                            return false;
                        }

                        tempUTXOs.remove(input.getTransactionOutputId());
                    }
                }

                for (TransactionOutput output : transaction.getOutputs()) {
                    tempUTXOs.put(output.getId(), output);
                }

                if (transaction.getOutputs().get(0).getRecipient() != transaction.getRecipient()) {
                    System.out.println("#Transaction(" + transaction.getTransactionId() + ") output recipient is not who it should be");
                    return false;
                }
                if (transaction.getOutputs().get(1).getRecipient() != transaction.getSender()) {
                    System.out.println("#Transaction(" + transaction.getTransactionId() + ") output 'change' is not sender.");
                    return false;
                }
            }
        }
        System.out.println("Blockchain is valid");
        return true;
    }


}
