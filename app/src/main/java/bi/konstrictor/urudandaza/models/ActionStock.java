package bi.konstrictor.urudandaza.models;

import com.j256.ormlite.field.DatabaseField;

import java.util.Date;

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

    public ActionStock(Produit produit, Double quantite, Double prix, Double payee, Personne personne, String motif) {
        this.produit = produit;
        this.quantite = quantite;
        this.prix = prix;
        this.payee = payee;
        this.personne = personne;
        this.motif = motif;
        this.date = new Date();
    }

    public ActionStock(Produit produit, Double quantite) {
        this.produit = produit;
        this.quantite = quantite;
        this.prix = 0.;
        this.payee = 0.;
        this.date = new Date();
    }

    public ActionStock() {
    }
}
