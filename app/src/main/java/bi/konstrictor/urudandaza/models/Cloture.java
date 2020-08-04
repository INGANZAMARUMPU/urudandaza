package bi.konstrictor.urudandaza.models;

import androidx.annotation.NonNull;

import com.j256.ormlite.field.DatabaseField;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Cloture {
    @DatabaseField(generatedId = true)
    public Integer id;
    @DatabaseField
    public Date date;
    @DatabaseField(defaultValue = "0")
    public Double achat;
    @DatabaseField(defaultValue = "0")
    public Double vente;
    @DatabaseField(defaultValue = "0")
    public Double payee_achat;
    @DatabaseField(defaultValue = "0")
    public Double payee_vente;
    @DatabaseField(defaultValue = "0")
    public Boolean compiled;

    public Cloture() {
        this.date = new Date();
    }
    public Double getVenteReste(){
        return Math.abs(vente)-Math.abs(payee_vente);
    }
    public Double getVentePayee(){
        return Math.abs(payee_vente);
    }
    public Double getAchatReste(){
        return Math.abs(achat)-Math.abs(payee_achat);
    }

    public String getDateFormated(){
        SimpleDateFormat sdate = new SimpleDateFormat("dd - MM - yyyy ");
        return sdate.format(this.date);
    }

    @Override
    public String toString() {
        return "Cloture{" +
                "id=" + id +
                ", date=" + date +
                ", achat=" + achat +
                ", vente=" + vente +
                ", payee_achat=" + payee_achat +
                ", payee_vente=" + payee_vente +
                ", compiled=" + compiled +
                '}';
    }

    public Double getVente() {
        return Math.abs(vente);
    }
}
