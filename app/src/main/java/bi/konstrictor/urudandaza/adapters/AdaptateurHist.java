package bi.konstrictor.urudandaza.adapters;

import android.content.DialogInterface;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import bi.konstrictor.urudandaza.DetailHistActivity;
import bi.konstrictor.urudandaza.R;
import bi.konstrictor.urudandaza.dialogs.KuranguraForm;
import bi.konstrictor.urudandaza.models.ActionStock;

public class AdaptateurHist extends RecyclerView.Adapter<AdaptateurHist.ViewHolder> {

        private DetailHistActivity context;
        private ArrayList<ActionStock> histories;

        public AdaptateurHist(DetailHistActivity context, ArrayList<ActionStock> histories) {
            this.context = context;
            this.histories = histories;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_history, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final ActionStock historie = histories.get(position);
            Double tot = Math.abs(historie.quantite*historie.prix);
            Double reste = tot - historie.payee;
            Boolean is_vente = historie.quantite<0;
            Double qtt = Math.abs(historie.quantite);
            holder.lbl_hist_product.setText(historie.produit.nom);
            holder.lbl_hist_date.setText(historie.getDateFormated());
            holder.lbl_hist_qtt.setText(qtt.toString());
            holder.lbl_hist_price.setText(historie.prix.toString());
            holder.lbl_hist_tot.setText(tot.toString());
            holder.lbl_hist_payee.setText(historie.payee.toString());
            holder.lbl_hist_reste.setText(reste.toString());
            if(is_vente){
                int vert = context.getResources().getColor(R.color.colorRed);
                holder.lbl_hist_payee.setTextColor(vert);
            }
            holder.btn_hist_options.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.bounce));
                    PopupMenu popup_menu = new PopupMenu(context, holder.btn_hist_options);
                    popup_menu.inflate(R.menu.item_menu);
                    popup_menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            int item_id = item.getItemId();
                            if(item_id == R.id.action_item_edit){
                                if(historie.quantite>0){
                                    KuranguraForm kurangura_form = new KuranguraForm(context, historie.produit);
                                    kurangura_form.setEdition(true, historie);
                                    kurangura_form.show();
                                }
                            }
                            if(item_id == R.id.action_item_delete){
                                new AlertDialog.Builder(context)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setTitle("Guhanagura")
                                    .setMessage("Urakeneye vy'ukuri guhanagura?")
                                    .setPositiveButton("Hanagura", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(context, "vyahanaguwe ", Toast.LENGTH_LONG).show();
                                        }
                                    })
                                    .setNegativeButton("Reka", null)
                                    .show();
                            }
                            return false;
                        }
                    });
                    popup_menu.show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return histories.size();
        }

        public void setData(ArrayList<ActionStock> produit) {
            this.histories = produit;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView lbl_hist_product, lbl_hist_date, lbl_hist_qtt, lbl_hist_price,
                    lbl_hist_tot, lbl_hist_payee, lbl_hist_reste, btn_hist_options;
            public View view;

            public ViewHolder(final View itemView) {
                super(itemView);
                this.view = itemView;
                lbl_hist_product = itemView.findViewById(R.id.lbl_hist_product);
                lbl_hist_date = itemView.findViewById(R.id.lbl_hist_date);
                lbl_hist_qtt = itemView.findViewById(R.id.lbl_hist_qtt);
                lbl_hist_price = itemView.findViewById(R.id.lbl_hist_price);
                lbl_hist_tot = itemView.findViewById(R.id.lbl_hist_tot);
                lbl_hist_payee = itemView.findViewById(R.id.lbl_hist_payee);
                lbl_hist_reste = itemView.findViewById(R.id.lbl_hist_reste);
                btn_hist_options = itemView.findViewById(R.id.btn_hist_options);
            }
        }
    }
