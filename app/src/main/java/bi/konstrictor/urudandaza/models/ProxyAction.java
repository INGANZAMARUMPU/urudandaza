package bi.konstrictor.urudandaza.models;

import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class ProxyAction extends ActionStock{
    public ProxyAction(Produit produit, Double quantite, Double prix, Double payee, Personne personne, String motif, Cloture cloture) {
        super(produit, quantite, prix, payee, personne, motif, cloture);
    }

    public ProxyAction(Produit produit, Double quantite, Double prix, Cloture cloture) {
        super(produit, quantite, prix, cloture);
    }

    public ProxyAction(Produit produit, Double quantite, Cloture cloture) {
        super(produit, quantite, cloture);
    }
    public ProxyAction() {
    }
}
