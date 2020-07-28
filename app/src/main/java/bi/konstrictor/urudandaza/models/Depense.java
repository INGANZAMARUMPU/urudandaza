package bi.konstrictor.urudandaza.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable
public class Depense {
    @DatabaseField(generatedId = true)
    public Integer id;
    @DatabaseField
    Date date;
    @DatabaseField
    Integer montant;
    @DatabaseField
    String motif;
}
