package Wallet;

import Transactions.Transaction;
import Transactions.TransactionInput;
import Transactions.TransactionOutput;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Wallet {
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private Map<String, TransactionOutput> UTX0s = new HashMap<>();


    public Wallet() {
        generateKeyPair();
    }


    private void generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");


            keyGen.initialize(ecSpec, random);
            KeyPair keyPair = keyGen.generateKeyPair();
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
        } catch (Exception e) {
            throw new RuntimeException("Error generating key pair", e);
        }
    }



    public float getBalance(){
        float total = UTX0s.values().stream().filter(output -> output.isMine(publicKey)).peek(output -> UTX0s.put(output.getId(),output)).map(TransactionOutput::getValue).reduce(0f,Float::sum);
        return total;
    }


    public Transaction sendFunds(PublicKey recipient, float value){
        if(getBalance()<value){
            System.out.println("Not Enough funds to send transaction. Transaction Discarded");
            return null;
        }

        List<TransactionInput> inputs = new ArrayList<>();
        float total = 0 ;


        for (TransactionOutput UTX0 : UTX0s.values()){
            total += UTX0.getValue();
            inputs.add(new TransactionInput(UTX0.getId())); //Created an extra TransactionInput function
            if(total > value) break;
        }


        Transaction newTransacton = new Transaction(publicKey,recipient,value,inputs);
        newTransacton.generateSignature(privateKey);

        // Remove the used UTXOs from the wallet's list
        inputs.forEach(input -> UTX0s.remove(input.getTransactionOutputId()));
        return newTransacton;
    }




    // Getters for public and private keys
    public PublicKey getPublicKey() {
        return publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }
}



