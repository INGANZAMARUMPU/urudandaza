package bi.konstrictor.urudandaza.models;

import com.j256.ormlite.field.DatabaseField;

import java.util.Date;

import bi.konstrictor.urudandaza.Globals;

public class Account {
    @DatabaseField(generatedId = true)
    public Integer id;
    @DatabaseField
    public String username;
}
