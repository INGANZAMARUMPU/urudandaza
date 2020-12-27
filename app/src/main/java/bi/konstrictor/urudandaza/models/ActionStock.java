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
public class ActionStock implements Model {
    @DatabaseField(generatedId = true)
    public Integer id;
    @DatabaseField(canBeNull=false, foreign=true, foreignColumnName="id", foreignAutoCreate=true)
    public Produit produit;
    @DatabaseField
    private Double quantite = 0.;
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
    @DatabaseField(defaultValue = "0")
    public Boolean perimee=false;

    public ActionStock() { }

    public void kurangura(Produit produit, Double quantite, Double suppl, Double prix, Personne personne, Double payee, String motif, Cloture cloture) {
        this.produit = produit;
        setQuantite(quantite, suppl);
        setPrix(prix);
        this.payee = payee;
        this.personne = personne;
        this.motif = motif;
        this.date = new Date();
        this.cloture = cloture;
        this.perimee = false;
    }
    public void kudandaza(Produit produit, Double quantite, Personne personne, Double payee,Cloture cloture) {
        kurangura(produit, -Math.abs(quantite), 0.0, produit.prix, personne, payee, null, cloture);
    }

    public String getDateFormated(){
        SimpleDateFormat sdate = new SimpleDateFormat("dd/MM/yyyy ");
        return sdate.format(this.date);
    }
    public void makeTotal(){
        this.total = Math.abs(this.prix*getQuantite());
    }
    public Double getTotal(){
        return Math.abs(this.getQuantite()*this.prix);
    }
    public Double getPrix() {
        return prix;
    }
    public void setPrix(Double prix) {
        this.prix = prix;
        makeTotal();
    }
    public Double getAchatTotal() {
        if (quantite>0) return total;
        return 0.;
    }
    public Double getVenteTotal() {
        if (quantite<0) return Math.abs(quantite*produit.prix);
        return 0.;
    }
    public Double getVenteReste() {
        if (getVenteTotal()==0) return 0.;
        return this.getVenteTotal()-payee;
    }
    public Double getAchatReste() {
        if (getAchatTotal()==0) return 0.;
        return this.getAchatTotal()-payee;
    }
    public Double getQuantite() {
        if (quantite>0) return quantite/produit.rapport;
        return Math.abs(quantite);
    }
    public Integer getQuantiteSuppl() {
        if (quantite>0) {
            Double suppl = quantite%produit.rapport;
            return suppl.intValue();
        }
        return 0;
    }
    public void setQuantite(Double quantite, Double supl) {
        if (quantite>0) {
            quantite = quantite*produit.rapport+supl;
        }
        this.quantite=quantite;
        makeTotal();
    }
    public void setQuantite(Double quantite) {
        if (quantite>0) {
            quantite = quantite*produit.rapport;
        }
        this.quantite=quantite;
        makeTotal();
    }
    @Override
    public String toString() {
        return Math.abs(getQuantite()) +" " + produit.nom + " x " + prix + " : " + getTotal();
    }

    @Override
    public void create(Context context) {
        InkoranyaMakuru inkoranyaMakuru = new InkoranyaMakuru(context);
        try {
            final Dao<ActionStock, Integer> daoAS = inkoranyaMakuru.getDao(ActionStock.class);
            final Dao<Produit, Integer> daoProduit = inkoranyaMakuru.getDao(Produit.class);
            final Dao<Cloture, Integer> daoCloture = inkoranyaMakuru.getDao(Cloture.class);
            produit.quantite += quantite;
            if(cloture == null) cloture = inkoranyaMakuru.getLatestCloture();
            if(isAchat()){
                cloture.payee_achat += payee;
                cloture.achat += getAchatTotal();
            } else if (isVente()){
                cloture.payee_vente += payee;
                cloture.vente += getVenteTotal();
            } else {
                cloture.perte = getVenteTotal();
            }
            TransactionManager.callInTransaction(inkoranyaMakuru.getConnectionSource(),
                    new Callable<Void>() {
                        @Override
                        public Void call() throws Exception {
                            daoAS.create(ActionStock.this);
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
            final Dao<ActionStock, Integer> daoAS = inkoranyaMakuru.getDao(ActionStock.class);
            ActionStock old = daoAS.queryForId(id);
            final Dao<Produit, Integer> daoProduit = inkoranyaMakuru.getDao(Produit.class);
            produit.quantite = produit.quantite - old.quantite + quantite;
            final Dao<Cloture, Integer> daoCloture = inkoranyaMakuru.getDao(Cloture.class);
            if(isAchat()){
                cloture.payee_achat -= old.payee + payee;
                cloture.achat -= old.getAchatTotal() + getAchatTotal();
            }else if (isVente()){
                cloture.payee_vente -= old.payee + payee;
                cloture.vente -= old.getVenteTotal() + getVenteTotal();
            } else {
                cloture.perte -= old.getVenteTotal() + getVenteTotal();
            }
            TransactionManager.callInTransaction(inkoranyaMakuru.getConnectionSource(),
                new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        daoAS.update(ActionStock.this);
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
            final Dao<ActionStock, Integer> daoAS = inkoranyaMakuru.getDao(ActionStock.class);
            final Dao<Produit, Integer> daoProduit = inkoranyaMakuru.getDao(Produit.class);
            produit.quantite -= quantite;
            final Dao<Cloture, Integer> daoCloture = inkoranyaMakuru.getDao(Cloture.class);
            if(isAchat()){
                cloture.payee_achat -= payee;
                cloture.achat -= getAchatTotal();
            } else if (isVente()){
                cloture.payee_vente -= payee;
                cloture.vente -= getVenteTotal();
            } else {
                cloture.perte -= getVenteTotal();
            }
            TransactionManager.callInTransaction(inkoranyaMakuru.getConnectionSource(),
                    new Callable<Void>() {
                        @Override
                        public Void call() throws Exception {
                            daoAS.delete(ActionStock.this);
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
        this.quantite = -Math.abs(quantite);
        this.prix = produit.prix;
        this.cloture = cloture;
        makeTotal();
        this.payee = 0.;
        this.date = new Date();
        perimee = true;
    }
    public boolean isAchat() {
        return quantite>0 && !perimee;
    }
    public boolean isVente() {
        return quantite<0 && !perimee;
    }
    public boolean isDette() {
        return payee<total && !perimee;
    }

}
