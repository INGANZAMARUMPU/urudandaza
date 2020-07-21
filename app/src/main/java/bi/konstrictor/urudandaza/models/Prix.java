package bi.konstrictor.urudandaza.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable
public class Prix {
    @DatabaseField(generatedId = true)
    Integer id;
    @DatabaseField(canBeNull = false, foreign = true, foreignColumnName = "id")
    Produit produit;
    @DatabaseField
    Date date;
}
