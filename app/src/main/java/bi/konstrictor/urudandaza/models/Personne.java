package bi.konstrictor.urudandaza.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable
public class Personne {
    @DatabaseField(generatedId = true)
    public Integer id;
    @DatabaseField
    public String nom;
    @DatabaseField
    public String phone;

    public Personne() {
    }
    public Personne(String nom) {
        this.nom = nom;
    }
    public Personne(String nom, String phone) {
        this.nom = nom;
        this.phone = phone;
    }
}