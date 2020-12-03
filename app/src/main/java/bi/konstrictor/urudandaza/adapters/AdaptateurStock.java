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
import bi.konstrictor.urudandaza.RefreshableActivity;
import bi.konstrictor.urudandaza.StockActivity;
import bi.konstrictor.urudandaza.dialogs.KuranguraForm;
import bi.konstrictor.urudandaza.dialogs.ProductForm;
import bi.konstrictor.urudandaza.models.Produit;

public class AdaptateurStock extends RecyclerView.Adapter<AdaptateurStock.ViewHolder> {

        private StockActivity context;
        private ArrayList<Produit> produits;

        public AdaptateurStock(StockActivity context, ArrayList<Produit> produits) {
            this.context = context;
            this.produits = produits;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_product, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final Produit produit = produits.get(position);
            holder.lbl_card_unite.setText(produit.unite_entrant);
            holder.lbl_card_quantite.setText(produit.quantite.toString());
            if(produit.rapport>1){
                String str_quantite;
                Double quantite = produit.quantite/produit.rapport;
                Integer modulo = produit.quantite.intValue()%produit.rapport.intValue();
                str_quantite = quantite.intValue()+" "+ produit.unite_entrant +" "+ modulo +" "+ produit.unite_sortant;
                holder.lbl_card_unite.setText("");
                holder.lbl_card_quantite.setText(str_quantite);
            }
            holder.lbl_card_product.setText(produit.nom);
            holder.lbl_kurangura_prix.setText(produit.prix.toString());
            holder.view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.button_fadein));
                    ProductForm product_form = new ProductForm(context);
                    product_form.setEdition(produit);
                    product_form.show();
                    return false;
                }
            });
            holder.btn_kurangura.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.bounce));
                    KuranguraForm kurangura_form = new KuranguraForm(context, produit);
                    kurangura_form.show();
                }
            });
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.bounce));
                    KuranguraForm kurangura_form = new KuranguraForm(context, produit);
                    kurangura_form.show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return produits.size();
        }

        public void setData(ArrayList<Produit> produit) {
            this.produits = produit;
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
