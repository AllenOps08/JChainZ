package Transactions;

public class TransactionInput {

    private String transactionOutputId; // Reference to TransactionOutputs -> transactionId
    public TransactionOutput UTX0;     // Contains the unspent transaction output



    public TransactionInput(String transactionOutputId) {
        this.transactionOutputId = transactionOutputId;
    }


    //Getter for transactionOutputId
    public String getTransactionOutputId(){
        return transactionOutputId;
    }

    //Getter and setter for UTX0 if needed (Optional)
    public TransactionOutput getUTX0(){
        return UTX0;
    }

    public void setUTX0(TransactionOutput UTX0){
        this.UTX0 = UTX0;
    }

}
