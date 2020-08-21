package bi.konstrictor.urudandaza.models;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import bi.konstrictor.urudandaza.InkoranyaMakuru;

public class Cloture implements Serializable {
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
        return getVente()-payee_vente;
    }
    public Double getAchatReste(){
        return achat-payee_achat;
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
                ", vente=" + getVente() +
                ", reste_achat=" + getAchatReste() +
                ", reste_vente=" + getVenteReste() +
                ", compiled=" + compiled +
                '}';
    }
    public Double getVente() {
        return Math.abs(vente);
    }

    public void cloturer(Context context){
        UpdateBuilder<Cloture, Integer> update;
        try {
            update = new InkoranyaMakuru(context).getDaoCloture().updateBuilder();
            if((this.getVente()>0) || (this.achat>0)) {
                Toast.makeText(context, "Umusi warangiye", Toast.LENGTH_SHORT).show();
                update.where().eq("id", this.id);
                update.updateColumnValue("compiled", true);
                update.update();
                Toast.makeText(context, "Umusi mushasha...", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context, "Umusi ugaragara ntiwugarika", Toast.LENGTH_LONG).show();
            }
        } catch (SQLException e) {
            Log.i("ERREUR", e.getMessage());
            e.printStackTrace();
            Toast.makeText(context, "Hari ikintu kutagenze neza", Toast.LENGTH_LONG).show();
        }
    }
}
