package bi.konstrictor.urudandaza.models;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.util.List;

import bi.konstrictor.urudandaza.InkoranyaMakuru;
import bi.konstrictor.urudandaza.interfaces.Model;

@DatabaseTable
public class Personne implements Model {
    @DatabaseField(generatedId = true)
    public Integer id;
    @DatabaseField(unique=true)
    public String nom;
    @DatabaseField(unique=true)
    public String phone;
    @DatabaseField
    public String autres;

    public Personne() {
    }
    public Personne(String nom) {
        this.nom = nom;
    }
    public Personne(String nom, String phone) {
        this.nom = nom;
        this.phone = phone;
    }
    public Personne(String nom, String phone, String autres) {
        this.nom = nom;
        this.phone = phone;
        this.autres = autres;
    }
    public static Personne getClient(String nom, Context context){
        try{
            Dao dao_personne = new InkoranyaMakuru(context).getDao(Personne.class);
            List<Personne> personnes = dao_personne.queryBuilder().where().eq("nom", nom).query();
            if (personnes.size()>0){
                return personnes.get(0);
            }
        } catch (SQLException e) {
            Log.i("ERREUR", e.getMessage());
            e.printStackTrace();
            Toast.makeText(context, "Hari ikintu kutagenze neza", Toast.LENGTH_LONG).show();
        }
        return null;
    }

    @Override
    public String toString() {
        return "Personne{" +
                ", nom='" + nom + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

    @Override
    public void create(Context context) {
        InkoranyaMakuru inkoranyaMakuru = new InkoranyaMakuru(context);
        try {
            final Dao<Personne, Integer> dao = inkoranyaMakuru.getDao(Personne.class);
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
            final Dao<Personne, Integer> dao = inkoranyaMakuru.getDao(Personne.class);
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
            final Dao<Personne, Integer> dao = inkoranyaMakuru.getDao(Personne.class);
            dao.delete(this);
            Toast.makeText(context, "Vyagenze neza", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(context, "ntivyakunze", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}