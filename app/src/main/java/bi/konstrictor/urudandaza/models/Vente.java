package bi.konstrictor.urudandaza.models;

import android.content.Context;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;

import bi.konstrictor.urudandaza.InkoranyaMakuru;
import bi.konstrictor.urudandaza.interfaces.Model;

@DatabaseTable
public class Vente implements Model {
    @DatabaseField(generatedId = true)
    public Integer id;
    @DatabaseField(canBeNull=false, foreign=true, foreignColumnName="id", foreignAutoCreate=true)
    public Produit produit;
    @DatabaseField
    public Double quantite = 0.;
    @DatabaseField
    public Double prix = 0.;
    @DatabaseField(canBeNull = false)
    private Double total;
    @DatabaseField
    public Double payee;
    @DatabaseField(foreign=true, foreignColumnName="id", foreignAutoCreate=true)
    public Personne personne;
    @DatabaseField
    public String motif;
    @DatabaseField(canBeNull=false,foreign=true, foreignColumnName="id")
    public Cloture cloture;
    @DatabaseField
    public Date date;
    @DatabaseField(defaultValue = "0")
    public Boolean perimee=false;

    public Vente() {}

    public Vente(Produit produit, Double quantite, Double payee, Personne personne, String motif, Cloture cloture) {
        this.produit = produit;
        this.quantite = quantite;
        this.prix = produit.prix;
        this.total = getTotal();
        this.payee = payee;
        this.personne = personne;
        this.motif = motif;
        this.cloture = cloture;
        this.date = new Date();
    }

    public String getDateFormated(){
        SimpleDateFormat sdate = new SimpleDateFormat("dd/MM/yyyy ");
        return sdate.format(this.date);
    }

    public Double getTotal(){
        return quantite*prix;
    }

    public Double getQuantite() {
        return quantite;
    }
    @Override
    public String toString() {
        return quantite +" " + produit.nom + " x " + prix + " : " + getTotal();
    }

    @Override
    public void create(Context context) {
        InkoranyaMakuru inkoranyaMakuru = new InkoranyaMakuru(context);
        try {
            final Dao<Vente, Integer> daoAS = inkoranyaMakuru.getDao(Vente.class);
            final Dao<Produit, Integer> daoProduit = inkoranyaMakuru.getDao(Produit.class);
            final Dao<Cloture, Integer> daoCloture = inkoranyaMakuru.getDao(Cloture.class);
            produit.quantite += quantite;
            if(cloture == null) cloture = inkoranyaMakuru.getLatestCloture();
            if(perimee){
                cloture.perte = getTotal();
            } else {
                cloture.payee_vente += payee;
                cloture.vente += getTotal();
            }
            TransactionManager.callInTransaction(inkoranyaMakuru.getConnectionSource(),
                    new Callable<Void>() {
                        @Override
                        public Void call() throws Exception {
                            daoAS.create(Vente.this);
                            daoProduit.update(produit);
                            daoCloture.update(cloture);
                            return null;
                        }
                    });
            Toast.makeText(context, "Vyagenze neza", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(context, "ntivyakunze", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

    public void update(Context context){
        InkoranyaMakuru inkoranyaMakuru = new InkoranyaMakuru(context);
        try {
            final Dao<Vente, Integer> daoAS = inkoranyaMakuru.getDao(Vente.class);
            Vente old = daoAS.queryForId(id);
            final Dao<Produit, Integer> daoProduit = inkoranyaMakuru.getDao(Produit.class);
            produit.quantite = produit.quantite - old.quantite + quantite;
            final Dao<Cloture, Integer> daoCloture = inkoranyaMakuru.getDao(Cloture.class);
            if(perimee){
                cloture.perte -= old.getTotal() + getTotal();
            }else {
                cloture.payee_vente -= old.payee + payee;
                cloture.vente -= old.getTotal() + getTotal();
            }
            TransactionManager.callInTransaction(inkoranyaMakuru.getConnectionSource(),
                new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        daoAS.update(Vente.this);
                        daoProduit.update(produit);
                        daoCloture.update(cloture);
                        return null;
                    }
            });
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
            final Dao<Vente, Integer> daoAS = inkoranyaMakuru.getDao(Vente.class);
            final Dao<Produit, Integer> daoProduit = inkoranyaMakuru.getDao(Produit.class);
            produit.quantite -= quantite;
            final Dao<Cloture, Integer> daoCloture = inkoranyaMakuru.getDao(Cloture.class);
            if(perimee){
                cloture.perte -= getTotal();
            } else {
                cloture.payee_vente -= payee;
                cloture.vente -= getTotal();
            }
            TransactionManager.callInTransaction(inkoranyaMakuru.getConnectionSource(),
                    new Callable<Void>() {
                        @Override
                        public Void call() throws Exception {
                            daoAS.delete(Vente.this);
                            daoProduit.update(produit);
                            daoCloture.update(cloture);
                            return null;
                        }
                    });
            Toast.makeText(context, "Vyagenze neza", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(context, "ntivyakunze", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void expiration(Produit produit, Double quantite, Cloture cloture) {
        this.produit = produit;
        this.quantite = quantite;
        this.prix = produit.prix;
        this.cloture = cloture;
        getTotal();
        this.payee = 0.;
        this.date = new Date();
        perimee = true;
    }

    public Double getReste() {
        return total-payee;
    }
}
