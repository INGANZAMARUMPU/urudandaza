package bi.konstrictor.urudandaza.models;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Callable;

import bi.konstrictor.urudandaza.InkoranyaMakuru;
import bi.konstrictor.urudandaza.interfaces.Model;

@DatabaseTable
public class Cloture implements Serializable, Model {
    @DatabaseField(generatedId = true)
    public Integer id;
    @DatabaseField
    public Date date;
    @DatabaseField(defaultValue = "0")
    public Double achat = 0.;
    @DatabaseField(defaultValue = "0")
    public Double vente = 0.;
    @DatabaseField(defaultValue = "0")
    public Double payee_achat = 0.;
    @DatabaseField(defaultValue = "0")
    public Double payee_vente = 0.;
    @DatabaseField(defaultValue = "0")
    public Double perte = 0.;
    @DatabaseField(defaultValue = "0")
    public Boolean compiled;

    public Cloture() {
        this.date = new Date();
    }
    public Double getVenteReste(){
        return getVente()-payee_vente;
    }
    public Double getAchatReste(){
        return achat-payee_achat;
    }

    public String getDateFormated(){
        SimpleDateFormat sdate = new SimpleDateFormat("dd - MM - yyyy ");
        return sdate.format(this.date);
    }

    @Override
    public String toString() {
        return "Cloture{" +
                "id=" + id +
                ", date=" + date +
                ", achat=" + achat +
                ", vente=" + vente +
                ", reste_achat=" + getAchatReste() +
                ", reste_vente=" + getVenteReste() +
                ", compiled=" + compiled +
                '}';
    }
    public Double getVente() {
        return Math.abs(vente);
    }

    public void cloturer(final Context context) throws SQLException {
        InkoranyaMakuru inkoranyaMakuru = new InkoranyaMakuru(context);
        final Dao<Produit, Integer> dao_p = inkoranyaMakuru.getDao(Produit.class);
        final Dao<ClotureProduit, Integer> dao_c = inkoranyaMakuru.getDao(ClotureProduit.class);
        final Dao<Cloture, Integer> dao = inkoranyaMakuru.getDao(Cloture.class);
        final ArrayList<Produit> produits = (ArrayList<Produit>) dao_p.queryForAll();

        final ArrayList<ClotureProduit> clotureProduits = new ArrayList<>();

        for (Produit produit : produits){
            clotureProduits.add(new ClotureProduit(produit.quantite, produit, Cloture.this));
        }

        if((this.getVente()>0) | (this.achat>0)) {
            TransactionManager.callInTransaction(inkoranyaMakuru.getConnectionSource(),
                new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        compiled = true;
                        dao_c.create(clotureProduits);
                        dao.update(Cloture.this);
                        return null;
                    }
                });
            Toast.makeText(context, "Umusi mushasha...", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Umusi ugaragara ntiwugarika", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void create(Context context) {
        InkoranyaMakuru inkoranyaMakuru = new InkoranyaMakuru(context);
        try {
            final Dao<Cloture, Integer> dao = inkoranyaMakuru.getDao(Cloture.class);
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
            final Dao<Cloture, Integer> dao = inkoranyaMakuru.getDao(Cloture.class);
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
            final Dao<Cloture, Integer> dao = inkoranyaMakuru.getDao(Cloture.class);
            dao.delete(this);
            Toast.makeText(context, "Vyagenze neza", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(context, "ntivyakunze", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

}
