package bi.konstrictor.urudandaza.models;

import android.content.Context;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.util.Date;

import bi.konstrictor.urudandaza.Globals;
import bi.konstrictor.urudandaza.InkoranyaMakuru;
import bi.konstrictor.urudandaza.interfaces.Model;

@DatabaseTable
public class RemboursementAchat implements Model {
    @DatabaseField(generatedId = true)
    public Integer id;
    @DatabaseField(canBeNull=false, foreign=true, foreignColumnName="id", foreignAutoCreate=true)
    public Achat achat;
    @DatabaseField
    public Double payee;
    @DatabaseField
    public String motif;
    @DatabaseField
    public Date date;
    @DatabaseField
    public String checksum;
    @DatabaseField(canBeNull=false, foreign=true, foreignColumnName="id")
    private Password signature;

    public RemboursementAchat() {
    }

    public RemboursementAchat(Achat achat, Double payee, String motif) {
        this.achat = achat;
        this.payee = payee;
        this.motif = motif;
        this.date = new Date();
    }

    public void validate(Password password){
        this.signature = password;
        this.checksum = Globals.sign(""+payee+date.getTime(), password.getSignature());
    }

    public boolean is_valid(){
        return checksum.equals(Globals.sign(""+payee+date.getTime(), this.signature.getSignature()));
    }

    @Override
    public void create(Context context) {
        InkoranyaMakuru inkoranyaMakuru = new InkoranyaMakuru(context);
        try {
            final Dao<RemboursementAchat, Integer> dao = inkoranyaMakuru.getDao(RemboursementAchat.class);
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
            final Dao<RemboursementAchat, Integer> dao = inkoranyaMakuru.getDao(RemboursementAchat.class);
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
            final Dao<RemboursementAchat, Integer> dao = inkoranyaMakuru.getDao(RemboursementAchat.class);
            dao.delete(this);
            Toast.makeText(context, "Vyagenze neza", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(context, "ntivyakunze", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
