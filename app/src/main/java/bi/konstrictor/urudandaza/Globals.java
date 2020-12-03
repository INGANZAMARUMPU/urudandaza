package bi.konstrictor.urudandaza;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Globals {

    public static String sign(String text, String signature){
        return hash(text.concat(signature));
    }

    public static String hash(String text){
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        m.update(text.getBytes(),0,text.length());
        String hash = new BigInteger(1, m.digest()).toString(16);
        return hash;
    }
}
