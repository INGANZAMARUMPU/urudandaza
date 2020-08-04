package bi.konstrictor.urudandaza.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import bi.konstrictor.urudandaza.ClotureActivity;
import bi.konstrictor.urudandaza.R;
import bi.konstrictor.urudandaza.models.Cloture;

public class AdaptateurCloture extends RecyclerView.Adapter<AdaptateurCloture.ViewHolder> {

        private ClotureActivity context;
        private ArrayList<Cloture> clotures;

        public AdaptateurCloture(ClotureActivity context, ArrayList<Cloture> clotures) {
            this.context = context;
            this.clotures = clotures;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_cloture, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final Cloture cloture = clotures.get(position);
            Log.i("==== CLOTURE ====", cloture.toString());

            holder.lbl_cloture_achat_rest.setText(cloture.getAchatReste().toString());
            holder.lbl_cloture_achat_tot.setText(cloture.achat.toString());
            holder.lbl_cloture_vente_reste.setText(cloture.getVenteReste().toString());
            holder.lbl_cloture_vente_tot.setText(cloture.getVente().toString());
            holder.lbl_cloture_vente_payee.setText(cloture.getVentePayee().toString());
            holder.lbl_cloture_date.setText(cloture.getDateFormated());
        }

        @Override
        public int getItemCount() {
            return clotures.size();
        }

        public void setData(ArrayList<Cloture> clotures) {
            this.clotures = clotures;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView lbl_cloture_date, lbl_cloture_achat_tot, lbl_cloture_achat_rest,
                    lbl_cloture_vente_tot, lbl_cloture_vente_payee, lbl_cloture_vente_reste;
            public View view;

            public ViewHolder(final View itemView) {
                super(itemView);
                this.view = itemView;
                lbl_cloture_date = itemView.findViewById(R.id.lbl_cloture_date);
                lbl_cloture_achat_tot = itemView.findViewById(R.id.lbl_cloture_achat_tot);
                lbl_cloture_achat_rest = itemView.findViewById(R.id.lbl_cloture_achat_rest);
                lbl_cloture_vente_tot = itemView.findViewById(R.id.lbl_cloture_vente_tot);
                lbl_cloture_vente_reste = itemView.findViewById(R.id.lbl_cloture_vente_reste);
                lbl_cloture_vente_payee = itemView.findViewById(R.id.lbl_cloture_vente_payee);
            }
        }
    }
