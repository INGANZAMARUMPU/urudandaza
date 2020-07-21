package bi.konstrictor.urudandaza.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable
public class Vente {
    @DatabaseField(generatedId = true)
    Integer id;
    @DatabaseField(canBeNull = false, foreign = true, foreignColumnName = "id")
    Stock stock;
    @DatabaseField(foreign = true, foreignColumnName = "id")
    Client client;
    @DatabaseField(canBeNull = false, foreign = true, foreignColumnName = "id")
    Prix prix;
    @DatabaseField
    Integer quantite;
    @DatabaseField
    Date date;

    @Override
    public String toString() {
        return quantite+stock.produit.unite_sortant+" " + stock.produit.nom + " " + prix;
    }
}
