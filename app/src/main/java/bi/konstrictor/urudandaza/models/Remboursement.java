package bi.konstrictor.urudandaza.models;

import com.j256.ormlite.field.DatabaseField;
import java.util.Date;

import bi.konstrictor.urudandaza.Globals;

public class Remboursement {
    @DatabaseField(generatedId = true)
    public Integer id;
    @DatabaseField(canBeNull=false, foreign=true, foreignColumnName="id", foreignAutoCreate=true)
    public ActionStock action_stock;
    @DatabaseField
    public Double payee;
    @DatabaseField
    public String motif;
    @DatabaseField
    public Date date;
    @DatabaseField
    public String checksum;
    @DatabaseField
    private Signature signature;

    public Remboursement() {
    }

    public Remboursement(ActionStock action_stock, Double payee, String motif, Date date) {
        this.action_stock = action_stock;
        this.payee = payee;
        this.motif = motif;
        this.date = date;
    }

    public void validate(Signature signature){
        this.signature = signature;
        this.checksum = Globals.sign(""+payee+date.getTime(), signature.getSignature());
    }

    public boolean is_valid(){
        return checksum.equals(Globals.sign(""+payee+date.getTime(), this.signature.getSignature()));
    }
}
