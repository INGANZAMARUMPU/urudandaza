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
public class Achat implements Model {
    @DatabaseField(generatedId = true)
    public Integer id;
    @DatabaseField(canBeNull=false, foreign=true, foreignColumnName="id", foreignAutoCreate=true)
    public Produit produit;
    @DatabaseField
    private Double quantite = 0.;
    private Double suppl = 0.;
    @DatabaseField
    private Double prix = 0.;
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

    public Achat() { }

    public Achat(Produit produit, Double quantite, Double suppl, Double prix, Double total, Double payee, Personne personne, String motif, Cloture cloture) {
        this.produit = produit;
        setQuantite(quantite, suppl);
        this.prix = prix;
        this.total = total;
        this.payee = payee;
        this.personne = personne;
        this.motif = motif;
        this.cloture = cloture;
    }

    public void setQuantite(Double quantite, Double supl) {
        this.quantite = quantite*produit.rapport+supl;
    }

    public String getDateFormated(){
        SimpleDateFormat sdate = new SimpleDateFormat("dd/MM/yyyy ");
        return sdate.format(this.date);
    }
    @Override
    public String toString() {
        return quantite +" " + produit.nom + " x " + prix + " : " + getTotal();
    }

    private Double getTotal() {
        return quantite*prix;
    }

    @Override
    public void create(Context context) {
        InkoranyaMakuru inkoranyaMakuru = new InkoranyaMakuru(context);
        try {
            final Dao<Achat, Integer> daoAS = inkoranyaMakuru.getDao(Achat.class);
            final Dao<Produit, Integer> daoProduit = inkoranyaMakuru.getDao(Produit.class);
            final Dao<Cloture, Integer> daoCloture = inkoranyaMakuru.getDao(Cloture.class);
            produit.quantite += quantite;
            if(cloture == null) cloture = inkoranyaMakuru.getLatestCloture();
            cloture.payee_achat += payee;
            cloture.achat += getTotal();
            TransactionManager.callInTransaction(inkoranyaMakuru.getConnectionSource(),
                    new Callable<Void>() {
                        @Override
                        public Void call() throws Exception {
                            daoAS.create(Achat.this);
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
            final Dao<Achat, Integer> daoAS = inkoranyaMakuru.getDao(Achat.class);
            Achat old = daoAS.queryForId(id);
            final Dao<Produit, Integer> daoProduit = inkoranyaMakuru.getDao(Produit.class);
            produit.quantite = produit.quantite - old.quantite + quantite;
            final Dao<Cloture, Integer> daoCloture = inkoranyaMakuru.getDao(Cloture.class);
            cloture.payee_achat -= old.payee + payee;
            cloture.achat -= old.getTotal() + getTotal();

            TransactionManager.callInTransaction(inkoranyaMakuru.getConnectionSource(),
                new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        daoAS.update(Achat.this);
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
            final Dao<Achat, Integer> daoAS = inkoranyaMakuru.getDao(Achat.class);
            final Dao<Produit, Integer> daoProduit = inkoranyaMakuru.getDao(Produit.class);
            produit.quantite -= quantite;
            final Dao<Cloture, Integer> daoCloture = inkoranyaMakuru.getDao(Cloture.class);
            cloture.payee_achat -= payee;
            cloture.achat -= getTotal();
            TransactionManager.callInTransaction(inkoranyaMakuru.getConnectionSource(),
                    new Callable<Void>() {
                        @Override
                        public Void call() throws Exception {
                            daoAS.delete(Achat.this);
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
}
