package bi.konstrictor.urudandaza.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Produit {
    @DatabaseField(generatedId = true)
    Integer id;
    @DatabaseField
    String nom;
    @DatabaseField
    Double unite_sortant;
    @DatabaseField
    Double unite_entrant;
    @DatabaseField
    Double rapport; //entre les unité i/o
}
