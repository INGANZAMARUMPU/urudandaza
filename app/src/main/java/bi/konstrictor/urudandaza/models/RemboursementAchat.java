package bi.konstrictor.urudandaza.models;

import android.content.Context;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Callable;

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
    @DatabaseField(foreign=true, foreignColumnName="id")
    private Password signature;

    public RemboursementAchat() {
    }

    public RemboursementAchat(Achat achat, Double payee, String motif) {
        this.achat = achat;
        this.payee = payee;
        this.motif = motif;
        this.date = new Date();
    }

    public boolean sign(Context context, Account account){
        InkoranyaMakuru db = new InkoranyaMakuru(context);
        try {
            Dao<Password,Integer> dao = db.getDao(Password.class);
            ArrayList<Password> signatures = (ArrayList<Password>) dao.queryForEq("account_id", account.id);
            if(signatures.size()>0) {
                signature = signatures.get(signatures.size() - 1);
                this.checksum = Globals.sign("" + payee + date.getTime(), signature.getSignature());
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean is_valid(){
        return checksum.equals(Globals.sign(""+payee+date.getTime(), this.signature.getSignature()));
    }

    @Override
    public void create(final Context context) {
        InkoranyaMakuru inkoranyaMakuru = new InkoranyaMakuru(context);
        try {
            final Dao<RemboursementAchat, Integer> dao = inkoranyaMakuru.getDao(RemboursementAchat.class);
            final Dao<Achat, Integer> dao_achat = inkoranyaMakuru.getDao(Achat.class);
            achat.payee += payee;
            try {
                TransactionManager.callInTransaction(inkoranyaMakuru.getConnectionSource(),
                    new Callable<Void>(){
                        @Override
                        public Void call() throws Exception {
                            dao_achat.update(achat);
                            dao.create(RemboursementAchat.this);
                            Toast.makeText(context, "Vyagenze neza", Toast.LENGTH_LONG).show();
                            return null;
                        }
                    });
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            Toast.makeText(context, "ntivyakunze", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public void update(final Context context) {
        InkoranyaMakuru inkoranyaMakuru = new InkoranyaMakuru(context);
        try {
            final Dao<RemboursementAchat, Integer> dao = inkoranyaMakuru.getDao(RemboursementAchat.class);
            final Dao<Cloture, Integer> dao_cloture = inkoranyaMakuru.getDao(Cloture.class);
            final Cloture cloture = achat.cloture;
            cloture.payee_achat += payee;
            if (signature!=null) {
                try {
                    TransactionManager.callInTransaction(inkoranyaMakuru.getConnectionSource(),
                        new Callable<Void>() {
                            @Override
                            public Void call() throws Exception {
                                if ((!checksum.trim().isEmpty()) && is_valid() ) dao_cloture.update(cloture);
                                dao.update(RemboursementAchat.this);
                                Toast.makeText(context, "Vyagenze neza", Toast.LENGTH_LONG).show();
                                return null;
                            }
                        });
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            Toast.makeText(context, "ntacahindutse", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(context, "ntivyakunze", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Context context) {
    }
}
