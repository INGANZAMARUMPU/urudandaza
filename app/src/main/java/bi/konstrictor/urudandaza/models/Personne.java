package bi.konstrictor.urudandaza.models;

import android.content.Context;
import android.text.Editable;
import android.util.Log;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import bi.konstrictor.urudandaza.InkoranyaMakuru;

@DatabaseTable
public class Personne {
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
}