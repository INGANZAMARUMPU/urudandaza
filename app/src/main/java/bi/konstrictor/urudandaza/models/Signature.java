package bi.konstrictor.urudandaza.models;

import android.content.Context;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.misc.TransactionManager;

import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.Callable;

import bi.konstrictor.urudandaza.Globals;
import bi.konstrictor.urudandaza.InkoranyaMakuru;

public class Signature implements Model{
    @DatabaseField(generatedId = true)
    public Integer id;
    @DatabaseField
    private String signature;
    @DatabaseField(foreign=true, foreignColumnName="id")
    public Account account;

    public Signature() {
    }

    public Signature(String signature) {
        this.signature = Globals.hash(signature);
    }
    public String getSignature() {
        return signature;
    }

    @Override
    public void create(Context context) {
        InkoranyaMakuru inkoranyaMakuru = new InkoranyaMakuru(context);
        try {
            final Dao<Signature, Integer> dao = inkoranyaMakuru.getDao(Signature.class);
            dao.create(this);
            Toast.makeText(context, "Vyagenze neza", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(context, "ntivyakunze", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public void update(Context context) {
        InkoranyaMakuru inkoranyaMakuru = new InkoranyaMakuru(context);
        try {
            final Dao<Signature, Integer> dao = inkoranyaMakuru.getDao(Signature.class);
            dao.update(this);
            Toast.makeText(context, "Vyagenze neza", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(context, "ntivyakunze", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Context context) {
        InkoranyaMakuru inkoranyaMakuru = new InkoranyaMakuru(context);
        try {
            final Dao<Signature, Integer> dao = inkoranyaMakuru.getDao(Signature.class);
            dao.delete(this);
            Toast.makeText(context, "Vyagenze neza", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(context, "ntivyakunze", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
