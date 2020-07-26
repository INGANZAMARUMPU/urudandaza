package bi.konstrictor.urudandaza.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Produit {
    @DatabaseField(generatedId = true)
    public Integer id;
    @DatabaseField(unique = true)
    public String nom;
    @DatabaseField
    public String unite_entrant;
    @DatabaseField
    public String unite_sortant;
    @DatabaseField
    public Double rapport; //entre les unité i/o
    @DatabaseField
    public Double prix;
    @DatabaseField
    public Boolean visible;
    @DatabaseField
    public Double quantite;

    public Produit() {
    }

    public Produit(String nom, String unite_entrant, String unite_sortant, Double rapport, Double quantite) {
        this.nom = nom;
        this.unite_entrant = unite_entrant;
        this.unite_sortant = unite_sortant;
        this.rapport = rapport;
        this.visible = true;
        this.quantite = quantite;
    }

    @Override
    public String toString() {
        return nom + " "+quantite+" " + unite_entrant+" " ;
    }
}
