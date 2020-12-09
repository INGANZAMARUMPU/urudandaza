package bi.konstrictor.urudandaza.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import bi.konstrictor.urudandaza.R;
import bi.konstrictor.urudandaza.dialogs.KuranguraForm;
import bi.konstrictor.urudandaza.dialogs.ProductForm;
import bi.konstrictor.urudandaza.interfaces.RefreshableActivity;
import bi.konstrictor.urudandaza.models.ClotureProduit;
import bi.konstrictor.urudandaza.models.Produit;

public class AdaptateurStockBasic extends RecyclerView.Adapter<AdaptateurStockBasic.ViewHolder> {

    private ArrayList<ClotureProduit> clotures;
    private ArrayList<Produit> produits;

    public AdaptateurStockBasic(ArrayList<ClotureProduit> clotures) {
        this.clotures = clotures;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_product, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        ClotureProduit cloture;
        Produit produit;
        if(clotures!=null)
            cloture = clotures.get(position);
        else {
            produit = produits.get(position);
            cloture = new ClotureProduit(produit.quantite, produit, null);
        }
        holder.lbl_card_unite.setText(cloture.produit.unite_entrant);
        holder.lbl_card_quantite.setText(cloture.quantite.toString());
        if(cloture.produit.rapport>1){
            String str_quantite;
            Double quantite = cloture.quantite/cloture.produit.rapport;
            Integer modulo = cloture.quantite.intValue()%cloture.produit.rapport.intValue();
            str_quantite = quantite.intValue()+" "+ cloture.produit.unite_entrant +" "
                    + modulo +" "+ cloture.produit.unite_sortant;
            holder.lbl_card_unite.setText("");
            holder.lbl_card_quantite.setText(str_quantite);
        }
        holder.lbl_card_product.setText(cloture.produit.nom);
        holder.lbl_kurangura_prix.setText(cloture.produit.prix.toString());
    }

    @Override
    public int getItemCount() {
        if (clotures!=null)
            return clotures.size();
        return produits.size();
    }

    public void setData(ArrayList<ClotureProduit> produit) {
        this.clotures = produit;
        notifyDataSetChanged();
    }
    public void setProduits(ArrayList<Produit> produits) {
        this.produits = produits;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
            TextView lbl_card_product, lbl_card_quantite, lbl_card_unite, lbl_kurangura_prix;
            Button btn_kurangura;
            public View view;

            public ViewHolder(final View itemView) {
                super(itemView);
                this.view = itemView;
                lbl_card_product = itemView.findViewById(R.id.lbl_card_product);
                lbl_card_quantite = itemView.findViewById(R.id.lbl_card_quantite);
                lbl_card_unite = itemView.findViewById(R.id.lbl_kurangura_mesure);
                lbl_kurangura_prix = itemView.findViewById(R.id.lbl_kurangura_prix);
                btn_kurangura = itemView.findViewById(R.id.btn_kurangura);
            }
        }
    }
