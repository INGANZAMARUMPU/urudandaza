package bi.konstrictor.urudandaza.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Offre {
    @DatabaseField(generatedId = true)
    Integer id;
    @DatabaseField(canBeNull = false, foreign = true, foreignColumnName = "id")
    Produit produit;
    @DatabaseField
    String fournisseur;
    @DatabaseField
    Integer prix;

    @Override
    public String toString() {
        return produit +" " + prix + " " + fournisseur;
    }
}
