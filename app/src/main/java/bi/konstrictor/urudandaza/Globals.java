package bi.konstrictor.urudandaza;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Globals {

    public static String sign(String text, String signature){
        MessageDigest m = null;
        String s  = text+signature;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        m.update(s.getBytes(),0,s.length());
        String hash = new BigInteger(1, m.digest()).toString(16);
        return hash;
    }
}
