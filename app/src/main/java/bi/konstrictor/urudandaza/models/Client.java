package bi.konstrictor.urudandaza.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Client {
    @DatabaseField(generatedId = true)
    Integer id;
    @DatabaseField
    String nom;
    @DatabaseField
    String tel;
}
