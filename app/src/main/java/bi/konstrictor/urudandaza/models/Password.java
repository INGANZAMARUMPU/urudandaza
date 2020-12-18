package bi.konstrictor.urudandaza.models;

import android.content.Context;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;

import bi.konstrictor.urudandaza.Globals;
import bi.konstrictor.urudandaza.InkoranyaMakuru;
import bi.konstrictor.urudandaza.interfaces.Model;

@DatabaseTable
public class Password implements Model {
    @DatabaseField(generatedId = true)
    public Integer id;
    @DatabaseField
    private String hash;
    @DatabaseField(foreign=true, foreignColumnName="id", canBeNull=false, foreignAutoCreate=true)
    public Account account;

    public Password() {
    }

    public void setHash(String password) {
        this.hash = Globals.hash(password);
    }

    public Password(String password, Account account) {
        setHash(password);
        this.account = account;
    }

    public String getSignature() {
        return hash;
    }

    @Override
    public void create(Context context) {
        InkoranyaMakuru inkoranyaMakuru = new InkoranyaMakuru(context);
        try {
            final Dao<Password, Integer> dao = inkoranyaMakuru.getDao(Password.class);
            dao.create(this);
            Toast.makeText(context, "Vyagenze neza", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(context, "ntivyakunze", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public void update(Context context) {
        new Password(hash, account).create(context);
    }

    @Override
    public void delete(Context context) {
    }
}
