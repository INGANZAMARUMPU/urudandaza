package bi.konstrictor.urudandaza.models;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import bi.konstrictor.urudandaza.InkoranyaMakuru;

public class ActionStock {
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
    @DatabaseField
    public Date date;
    @DatabaseField(canBeNull=false,foreign=true, foreignColumnName="id")
    public Cloture cloture;
    public ActionStock() { }

    public void ideniKurangura(Produit produit, Double quantite, Double prix, Personne personne, Double payee, String motif, Cloture cloture) {
        this.produit = produit;
        setQuantite(quantite);
        setPrix(prix);
        this.payee = 0.;
        this.personne = personne;
        this.motif = motif;
        this.date = new Date();
        this.cloture = cloture;
    }
    public void ideniKudandaza(Produit produit, Double quantite, Personne personne, Double payee,String motif, Cloture cloture) {
        ideniKurangura(produit, -Math.abs(quantite), produit.prix, personne, payee, motif, cloture);
    }
    public void kurangura(Produit produit, Double quantite, @Nullable Double prix, Cloture cloture) {
        this.produit = produit;
        setQuantite(quantite);
        setPrix(prix);
        this.payee = total;
        this.date = new Date();
        this.cloture = cloture;
    }
    public void kudandaza(Produit produit, Double quantite, Cloture cloture) {
        kurangura(produit, -Math.abs(quantite), produit.prix,  cloture);
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
        if (getQuantite()>0) return total;
        return 0.;
    }
    public Double getVenteTotal() {
        if (getQuantite()<0) return total;
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
        return quantite;
    }
    public void setQuantite(Double quantite) {
        if (quantite>0) quantite*=produit.rapport;
        this.quantite=quantite;
        makeTotal();
    }
    @Override
    public String toString() {
        return Math.abs(getQuantite()) +" " + produit.nom + " x " + prix + " : " + getTotal();
    }
    public void update(Context context, ActionStock as){
        InkoranyaMakuru inkoranyaMakuru = new InkoranyaMakuru(context);
        try {
            UpdateBuilder<ActionStock, Integer> update = inkoranyaMakuru.getDaoActionStock().updateBuilder();
            update.where().eq("id", this.id);
            update.updateColumnValue("quantite" , as.quantite);
            update.updateColumnValue("prix" , as.prix);
            update.updateColumnValue("payee" , as.payee);
            update.updateColumnValue("total" , as.total);
            if(personne!=null) update.updateColumnValue("personne_id" , personne.id);
            update.update();
            Toast.makeText(context, "Vyagenze neza", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(context, "ntivyakunze", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
