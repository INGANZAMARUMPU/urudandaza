package bi.konstrictor.urudandaza;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.print.PdfConverter;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.net.URLConnection;
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
    public static File generatePDF(final AppCompatActivity context, String html){
        PdfConverter converter = PdfConverter.getInstance();
        File folder = new File(Environment.getExternalStorageDirectory().toString(), "urudandaza");
        folder.mkdirs();
        final File file = new File(folder.toString(), "urudandaza-"+new Date().getTime()+".pdf");
        converter.convert(context, html, file);
        converter.SetOnPDFCreatedListener(new PdfConverter.OnPDFCreated() {
            @Override
            public void onPDFCreated() {
                ShareCompat.IntentBuilder.from(context)
                    .setStream(Uri.parse(file.getAbsolutePath()))
                    .setType(URLConnection.guessContentTypeFromName(file.getName()))
                    .startChooser();
            }
        });
        return file;
    }

    public static void exportDB(Context context, SQLiteDatabase db) {
        String sql = "SELECT name FROM sqlite_master WHERE type = \"table\";";
        Cursor cur = db.rawQuery(sql, new String[0]);
        cur.moveToFirst();
        File folder = new File(Environment.getExternalStorageDirectory().toString(), "urudandaza");
        folder.mkdirs();
        File file = new File(folder, "backup.rdz");
        try ( FileOutputStream stream = new FileOutputStream(file)){
            String tableName;
            while (cur.moveToNext()) {
                tableName = cur.getString(cur.getColumnIndex("name"));
                if (!tableName.equals("android_metadata") && !tableName.equals("sqlite_sequence")) {
                    exportTable(db, tableName, stream);
                }
            }
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public static void importDB(Context context, Uri uri) {
        File file;
        if( uri!= null){
            if (uri.getPath()!=null) file = new File(uri.getPath());
            else return;
        } else {
            file = new File(Environment.getExternalStorageDirectory().toString(),
                    "urudandaza/backup.rdz");
        }
        Toast.makeText(context, "biriko birabikurwa ", Toast.LENGTH_SHORT).show();
        try (BufferedReader br = new BufferedReader(new FileReader(file))){
            String line;
            SQLiteDatabase db = new InkoranyaMakuru(context).getWritableDatabase();
            while ((line = br.readLine()) != null) {
                db.execSQL(line);
            }
        } catch (IOException e) {
            Toast.makeText(context, "fichier urudandaza/backup.rdz ntiyuguruka", Toast.LENGTH_SHORT).show();
        }
    }

    private static void exportTable(SQLiteDatabase db, String tableName, FileOutputStream stream) throws IOException {
        String sql = "select * from " + tableName;
        Cursor cur = db.rawQuery(sql, new String[0]);
        int numcols = cur.getColumnCount();
        String line = "DELETE FROM "+tableName+";";
        stream.write(line.getBytes());
        while (cur.moveToNext()) {
            line = "insert into "+tableName+" values (";
            for (int idx = 0; idx < numcols; idx++) {
                line += "\""+cur.getString(idx)+"\", ";
            }
            line = line.substring(0, line.length()- 2).concat(");\n");
            stream.write(line.getBytes());
        }
    }
}
