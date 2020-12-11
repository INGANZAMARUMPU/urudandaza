package bi.konstrictor.urudandaza;

import android.content.Context;
import android.os.Environment;
import android.print.PdfConverter;

import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

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
    public static File generatePDF(Context context, String html){
        PdfConverter converter = PdfConverter.getInstance();
        File file = new File(Environment.getExternalStorageDirectory().toString(),
                "urudandaza-"+new Date().toString()+".pdf");
        converter.convert(context, html, file);
        return file;
    }
}
