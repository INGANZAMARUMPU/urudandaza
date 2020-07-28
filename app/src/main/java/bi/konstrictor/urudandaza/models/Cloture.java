package bi.konstrictor.urudandaza.models;

import com.j256.ormlite.field.DatabaseField;

import java.util.Date;

public class Cloture {
    @DatabaseField(generatedId = true)
    public Integer id;
    @DatabaseField
    public Date date;
    @DatabaseField
    public Double entree;
    @DatabaseField
    public Double sortie;
    @DatabaseField
    public Double reste;
}
