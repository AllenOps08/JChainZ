package Utils;

import java.io.UnsupportedEncodingException;
import java.security.*;
import java.util.Base64;




public class Utils {



    //Computes the SHA-256 hash of the input string
    public static String applySha256(String input){
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for(byte b: hash){
                String hex = Integer.toHexString(0xff & b);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }




//    This method generates a digital signature using the ECDSA (Elliptic Curve Digital Signature Algorithm) for a given input string.

    public static byte[] applyECDSASig(PrivateKey privateKey, String input){
        try{
            Signature dsa = Signature.getInstance("SHA256withECDSA");
            dsa.initSign(privateKey);
            dsa.update(input.getBytes("UTF-8"));
            return dsa.sign();
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }




   //Verifies the ECDSA Signature
    public static boolean verifyECDSASig(PublicKey publicKey, String data, byte[] signature){
        try {
            Signature ecdsaVerify = Signature.getInstance("SHA256withECDSA");
            ecdsaVerify.initVerify(publicKey);
            ecdsaVerify.update(data.getBytes("UTF-8"));
            return ecdsaVerify.verify(signature);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//    This method converts a cryptographic key (either public or private) into a Base64 encoded string.

    public static String getStringFromKey(Key key){
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }
}
