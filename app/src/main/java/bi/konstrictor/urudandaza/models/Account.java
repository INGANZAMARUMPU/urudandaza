package bi.konstrictor.urudandaza.models;

import android.content.Context;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.util.ArrayList;

import bi.konstrictor.urudandaza.InkoranyaMakuru;

@DatabaseTable
public class Account {
    @DatabaseField(generatedId = true)
    public Integer id;
    @DatabaseField
    public String username;

    public Account() {
    }

    public Account getAdminAccount(Context context){
        InkoranyaMakuru inkoranyaMakuru = new InkoranyaMakuru(context);
        try {
            final Dao<Account, Integer> dao = inkoranyaMakuru.getDao(Account.class);
            ArrayList<Account> accounts = (ArrayList<Account>)
                    dao.queryForEq("username", "admin");
            if (accounts.isEmpty()){
                this.username = "admin";
                this.create(context);
                return this;
            }
            return accounts.get(0);
        } catch (SQLException e) {
            Toast.makeText(context, "ntivyakunze", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return null;
    }

    public void create(Context context) {
        InkoranyaMakuru inkoranyaMakuru = new InkoranyaMakuru(context);
        try {
            final Dao<Account, Integer> dao = inkoranyaMakuru.getDao(Account.class);
            dao.create(this);
            Toast.makeText(context, "Vyagenze neza", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(context, "ntivyakunze", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
