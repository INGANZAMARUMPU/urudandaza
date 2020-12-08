package bi.konstrictor.urudandaza.models;

import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable
public class ProxyAction extends ActionStock{
    private Double quantite, prix, total;

    public ProxyAction() {
        super();
    }
}
