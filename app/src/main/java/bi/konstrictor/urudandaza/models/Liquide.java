package bi.konstrictor.urudandaza.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.text.SimpleDateFormat;
import java.util.Date;

@DatabaseTable
public class Liquide implements Model{
    @DatabaseField(generatedId = true)
    public Integer id;
    @DatabaseField
    public Date date;
    @DatabaseField
    public Double montant;
    @DatabaseField
    public String motif;

    public Liquide() {
    }
    public Liquide(Double montant, String motif) {
        this.montant = montant;
        this.motif = motif;
        this.date = new Date();
    }
    public String getDateFormated(){
        SimpleDateFormat sdate = new SimpleDateFormat("dd - MM - yyyy ");
        return sdate.format(this.date);
    }
}
