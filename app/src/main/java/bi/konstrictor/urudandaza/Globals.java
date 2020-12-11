package bi.konstrictor.urudandaza;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.print.PdfConverter;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
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

    public static void exportDB(Context context, SQLiteDatabase db) {
        String sql = "SELECT name FROM sqlite_master WHERE type = \"table\";";
        Cursor cur = db.rawQuery(sql, new String[0]);
        cur.moveToFirst();

        File file = new File(Environment.getExternalStorageDirectory().toString(),
                "urudandaza.backup.txt");
        try {
            FileOutputStream stream = new FileOutputStream(file);

            String tableName;
            while (cur.moveToNext()) {
                tableName = cur.getString(cur.getColumnIndex("name"));
                Log.i("BACKUP", tableName);
                if (!tableName.equals("android_metadata") && !tableName.equals("sqlite_sequence")) {
                    exportTable(db, tableName, stream);
                }
            }
            Toast.makeText(context, "wabitswe muri "+file.getCanonicalPath(), Toast.LENGTH_SHORT).show();
            stream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private static void exportTable(SQLiteDatabase db, String tableName, FileOutputStream stream) throws IOException {
        String sql = "select * from " + tableName;
        Cursor cur = db.rawQuery(sql, new String[0]);
        int numcols = cur.getColumnCount();
        String line = "DELETE FROM "+tableName+";\n";
        stream.write(line.getBytes());
        while (cur.moveToNext()) {
            line = "insert into "+tableName+" values (";
            for (int idx = 0; idx < numcols; idx++) {
                line += "\""+cur.getString(idx)+"\", ";
            }
            line = line.substring(0, line.length()- 2).concat(");\n");
            Log.i("BACKUP", line);
            stream.write(line.getBytes());
        }
    }
}
