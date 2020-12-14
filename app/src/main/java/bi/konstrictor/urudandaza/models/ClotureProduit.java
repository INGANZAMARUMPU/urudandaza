package bi.konstrictor.urudandaza.models;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.util.Date;

import bi.konstrictor.urudandaza.InkoranyaMakuru;
import bi.konstrictor.urudandaza.interfaces.Model;

@DatabaseTable
public class ClotureProduit implements Model {
    @DatabaseField(generatedId = true)
    public Integer id;
    @DatabaseField
    public Double quantite;
    @DatabaseField(canBeNull=false,foreign=true, foreignColumnName="id")
    public Produit produit;
    @DatabaseField(canBeNull=false,foreign=true, foreignColumnName="id")
    public Cloture cloture;

    @DatabaseField
    public Date date;

    public ClotureProduit() {
        this.date = new Date();
    }

    public ClotureProduit(Double quantite, Produit produit, Cloture cloture) {
        this.quantite = quantite;
        this.produit = produit;
        this.cloture = cloture;
        this.date = cloture == null ? new Date() : cloture.date;
    }

    @Override
    public String toString() {
        return produit.nom + " "+quantite+" " + " "+cloture+" " + produit.unite_entrant+" " ;
    }

    @Override
    public void create(Context context) {
        InkoranyaMakuru inkoranyaMakuru = new InkoranyaMakuru(context);
        try {
            final Dao<ClotureProduit, Integer> dao = inkoranyaMakuru.getDao(ClotureProduit.class);
            dao.create(ClotureProduit.this);
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
            final Dao<ClotureProduit, Integer> dao = inkoranyaMakuru.getDao(ClotureProduit.class);
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
            final Dao<ClotureProduit, Integer> dao = inkoranyaMakuru.getDao(ClotureProduit.class);
            dao.delete(this);
            Toast.makeText(context, "Vyagenze neza", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(context, "ntivyakunze", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
