package bi.konstrictor.urudandaza.models;

import com.j256.ormlite.field.DatabaseField;

import java.util.Date;

public class Dette {
    @DatabaseField(generatedId = true)
    public Integer id;
    @DatabaseField(canBeNull=false, foreign=true, foreignColumnName="id", foreignAutoCreate=true)
    public ActionStock action_stock;
    @DatabaseField
    public Double payee;
    @DatabaseField(foreign=true, foreignColumnName="id", foreignAutoCreate=true)
    public Personne personne;
    @DatabaseField
    public String motif;
    @DatabaseField
    public Date date;
}
