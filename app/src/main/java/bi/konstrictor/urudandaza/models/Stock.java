package bi.konstrictor.urudandaza.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable
public class Stock {
    @DatabaseField(generatedId = true)
    Integer id;
    @DatabaseField(canBeNull = false, foreign = true, foreignColumnName = "id")
    Produit produit;
    @DatabaseField(foreign = true, foreignColumnName = "id")
    Offre offre;
    @DatabaseField
    Double quantite;
    @DatabaseField
    Date date;

    @Override
    public String toString() {
        return produit.nom+" "+quantite+produit.unite_entrant+" "+date;
    }
}
