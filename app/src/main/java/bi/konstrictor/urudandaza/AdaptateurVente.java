package bi.konstrictor.urudandaza;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import bi.konstrictor.urudandaza.dialogs.KuranguraForm;
import bi.konstrictor.urudandaza.dialogs.ProductForm;
import bi.konstrictor.urudandaza.models.ActionStock;
import bi.konstrictor.urudandaza.models.Produit;

class AdaptateurVente extends RecyclerView.Adapter<AdaptateurVente.ViewHolder> {

        private VenteActivity context;
        private ArrayList<Produit> stocks;

        public AdaptateurVente(VenteActivity context, ArrayList<Produit> stocks) {
            this.context = context;
            this.stocks = stocks;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_vente, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.lbl_card_vente.setText(stocks.get(position).nom);
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.button_fadein));
                    if(context.EDITION) {
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return stocks.size();
        }

        public void setData(ArrayList<Produit> produit) {
            this.stocks = produit;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView lbl_card_vente;
            Button btn_card_refresh;
            NumberPicker nbr_vente_qtt;
            Boolean IS_SELECTED = false;
            View view;

            public ViewHolder(final View itemView) {
                super(itemView);
                this.view = itemView;
                lbl_card_vente = itemView.findViewById(R.id.lbl_card_vente);
                btn_card_refresh = itemView.findViewById(R.id.btn_card_refresh);
                nbr_vente_qtt = itemView.findViewById(R.id.nbr_vente_qtt);
                nbr_vente_qtt.setMaxValue(100);
            }
        }
    }
