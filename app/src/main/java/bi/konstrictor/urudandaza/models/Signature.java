package bi.konstrictor.urudandaza.models;

import com.j256.ormlite.field.DatabaseField;

import java.util.Date;

import bi.konstrictor.urudandaza.Globals;

public class Signature implements Model{
    @DatabaseField(generatedId = true)
    public Integer id;
    @DatabaseField
    private String signature;
    @DatabaseField(foreign=true, foreignColumnName="id")
    public Account account;

    public Signature() {
    }

    public Signature(String signature) {
        this.signature = Globals.hash(signature);
    }
    public String getSignature() {
        return signature;
    }
}
