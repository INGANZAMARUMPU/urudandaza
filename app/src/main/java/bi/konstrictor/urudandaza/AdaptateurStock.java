package bi.konstrictor.urudandaza;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import bi.konstrictor.urudandaza.dialogs.KuranguraForm;
import bi.konstrictor.urudandaza.dialogs.ProductForm;
import bi.konstrictor.urudandaza.models.Produit;

class AdaptateurStock extends RecyclerView.Adapter<AdaptateurStock.ViewHolder> {

        private RefreshableActivity context;
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
            holder.lbl_card_product.setText(produits.get(position).nom);
            holder.lbl_card_quantite.setText(produits.get(position).quantite.toString());
            holder.lbl_card_unite.setText(produits.get(position).unite_entrant);
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.button_fadein));
                    ProductForm product_form = new ProductForm(context);
                    product_form.show();
                }
            });
            holder.btn_kurangura.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.bounce));
                    KuranguraForm kurangura_form = new KuranguraForm(context, produits.get(position));
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
            TextView lbl_card_product, lbl_card_quantite, lbl_card_unite;
            Button btn_kurangura;
            public View view;

            public ViewHolder(final View itemView) {
                super(itemView);
                this.view = itemView;
                lbl_card_product = itemView.findViewById(R.id.lbl_card_product);
                lbl_card_quantite = itemView.findViewById(R.id.lbl_card_quantite);
                lbl_card_unite = itemView.findViewById(R.id.lbl_card_unite);
                btn_kurangura = itemView.findViewById(R.id.btn_kurangura);
            }
        }
    }
