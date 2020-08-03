package bi.konstrictor.urudandaza.models;

import com.j256.ormlite.field.DatabaseField;

import java.text.SimpleDateFormat;
import java.util.Date;

import bi.konstrictor.urudandaza.InkoranyaMakuru;

public class ActionStock {
    @DatabaseField(generatedId = true)
    public Integer id;
    @DatabaseField(canBeNull=false, foreign=true, foreignColumnName="id", foreignAutoCreate=true)
    public Produit produit;
    @DatabaseField
    public Double quantite;
    @DatabaseField
    public Double prix;
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

    public ActionStock(Produit produit, Double quantite, Double prix, Double payee, Personne personne, String motif, Cloture cloture) {
        this.produit = produit;
        this.quantite = quantite;
        this.prix = prix;
        this.payee = payee;
        this.personne = personne;
        this.motif = motif;
        this.date = new Date();
        this.cloture = cloture;
    }
    public ActionStock() {
    }
    public ActionStock(Produit produit, Double quantite, Double prix, Cloture cloture) {
        this.produit = produit;
        this.quantite = quantite;
        this.prix = prix;
        this.payee = 0.;
        this.date = new Date();
        this.cloture = cloture;
    }

    public ActionStock(Produit produit, Double quantite, Cloture cloture) {
        this.produit = produit;
        this.quantite = quantite;
        this.prix = produit.prix;
        this.payee = 0.;
        this.date = new Date();
        this.cloture = cloture;
    }
    public String getDateFormated(){
        SimpleDateFormat sdate = new SimpleDateFormat("dd/MM/yyyy ");
        return sdate.format(this.date);
    }
    public Double total(){
        return Math.abs(this.prix*this.quantite);
    }

    @Override
    public String toString() {
        return quantite + " " + produit.unite_sortant + " " + produit.nom + " : " + Math.abs(quantite*prix);
    }
}
