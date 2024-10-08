package Transactions;

import Utils.Utils;
import Chain.Chain;
import java.security.*;
import java.util.ArrayList;
import java.util.List;

public class Transaction {

    private final String transactionId;
    private final PublicKey sender;
    private final PublicKey recipient;
    private final float value;
    private final List<TransactionInput> inputs;
    private List<TransactionOutput> outputs = new ArrayList<>();
    private byte[] signature;


    private static int sequence = 0;


    public Transaction(PublicKey from, PublicKey to, float value, List<TransactionInput> inputs) {
        this.sender = from;
        this.recipient = to;
        this.value = value;
        this.inputs = inputs;
        this.transactionId = calculateHash();
    }

    private String calculateHash(){
        sequence++;
        return Utils.applySha256(Utils.getStringFromKey(sender)+Utils.getStringFromKey(recipient)+value+sequence);
    }

    public void generateSignature(PrivateKey privateKey){
        String data = Utils.getStringFromKey(sender)+Utils.getStringFromKey(recipient)+value;
        signature = Utils.applyECDSASig(privateKey,data);
    }

    public boolean verifySignature(){
        String data = Utils.getStringFromKey(sender) + Utils.getStringFromKey(recipient) + value;
        return Utils.verifyECDSASig(sender,data,signature);
    }


    public boolean processTransaction(){

        if(!verifySignature()){
            System.out.println("Transaction Signature failed to verify");
            return false;
        }

        //Gather inputs(ensure they are unspent)
        for (TransactionInput input : inputs){
            input.UTX0 = Chain.UTX0s.get(input.getTransactionOutputId());
        }

        //Check if transaction is valid:

        float inputSum = inputs.stream().map(input -> input.UTX0).filter(utxo -> utxo != null).map(TransactionOutput::getValue).reduce(0f,Float::sum);


        if(inputSum<value){
            System.out.println("Transaction Inputs too small: "+ inputSum);
            return false ;
        }

        //Generate transaction outputs:
        float leftover = inputSum -value;
        outputs.add(new TransactionOutput(recipient,value,transactionId));//Send value
        outputs.add(new TransactionOutput(sender,leftover,transactionId));//Send leftover value


        //Add outputs to the unspent list
        outputs.forEach(output -> Chain.UTX0s.put(output.getId(),output));


        //Remove transaction inputs from UTX0 list as they are spent:
        inputs.stream().filter(input -> input.UTX0 != null).forEach(input -> Chain.UTX0s.remove(input.UTX0.getId()));

        return true ;
    }



    //Getter for fields
    public String getTransactionId(){
        return transactionId;
    }

    public  PublicKey getSender(){
        return sender ;
    }

    public PublicKey getRecipient() {
        return recipient;
    }

    public float getValue() {
        return value;
    }

    public List<TransactionOutput> getOutputs() {
        return outputs;
    }

    public List<TransactionInput> getInputs() {
        return inputs;
    }








}
