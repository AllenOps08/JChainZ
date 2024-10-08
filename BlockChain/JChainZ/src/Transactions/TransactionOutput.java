package Transactions;

import Utils.Utils;
import java.security.PublicKey;
import java.util.Objects;

public class TransactionOutput {
    private final String id;
    private final PublicKey recipient;
    private final float value;
    private final String parentTransactionId;


    //Constructor
    public TransactionOutput(PublicKey recipient, float value, String parentTransactionId) {
        this.recipient = recipient;
        this.value = value;
        this.parentTransactionId = parentTransactionId;
        this.id = Utils.applySha256(Utils.getStringFromKey(recipient) + value + parentTransactionId);
    }


    /*//////////////////////////////////////////////////////////////
                                GETTERS
    //////////////////////////////////////////////////////////////*/

    public String getId() {
        return id;
    }

    public PublicKey getRecipient() {
        return recipient;
    }

    public float getValue() {
        return value;
    }

    public String getParentTransactionId() {
        return parentTransactionId;
    }

    public boolean isMine(PublicKey publicKey) {
        return Objects.equals(publicKey, recipient);
    }






}


