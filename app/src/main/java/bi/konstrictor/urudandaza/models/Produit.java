package bi.konstrictor.urudandaza.models;

import android.content.Context;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;

import bi.konstrictor.urudandaza.InkoranyaMakuru;
import bi.konstrictor.urudandaza.interfaces.Model;

@DatabaseTable
public class Produit implements Model {
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

    public Produit(String nom, String unite_entrant, String unite_sortant, Double rapport, Double prix) {
        this.nom = nom;
        this.unite_entrant = unite_entrant;
        this.unite_sortant = unite_sortant;
        this.rapport = rapport;
        this.visible = true;
        this.quantite = 0.0;
        this.prix = prix;
    }
    @Override
    public String toString() {
        return nom + " "+quantite+" " + unite_entrant+" " ;
    }

    @Override
    public void create(Context context) {
        InkoranyaMakuru inkoranyaMakuru = new InkoranyaMakuru(context);
        try {
            final Dao<Produit, Integer> dao = inkoranyaMakuru.getDao(Produit.class);
            dao.create(this);
            Toast.makeText(context, "Vyagenze neza", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(context, "ntivyakunze", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void update(Context context) {
        InkoranyaMakuru inkoranyaMakuru = new InkoranyaMakuru(context);
        try {
            final Dao<Produit, Integer> dao = inkoranyaMakuru.getDao(Produit.class);
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
            final Dao<Produit, Integer> dao = inkoranyaMakuru.getDao(Produit.class);
            dao.delete(this);
            Toast.makeText(context, "Vyagenze neza", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(context, "ntivyakunze", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
