package bi.konstrictor.urudandaza.adapters;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.j256.ormlite.dao.Dao;

import java.util.ArrayList;

import bi.konstrictor.urudandaza.DetailHistActivity;
import bi.konstrictor.urudandaza.R;
import bi.konstrictor.urudandaza.dialogs.KudandazaForm;
import bi.konstrictor.urudandaza.fragments.VenteFragment;
import bi.konstrictor.urudandaza.models.Vente;
import bi.konstrictor.urudandaza.models.Cloture;

public class AdaptateurVentes extends RecyclerView.Adapter<AdaptateurVentes.ViewHolder> {

    private DetailHistActivity context;
    private ArrayList<Vente> ventes;
    private Cloture cloture = null;
    private boolean is_dette;
    VenteFragment parent;

    public AdaptateurVentes(DetailHistActivity context, ArrayList<Vente> ventes, VenteFragment parent) {
        this.context = context;
        this.ventes = ventes;
        this.cloture = context.cloture;
        this.is_dette = context.is_dette;
        this.parent = parent;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_history, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Vente vente = ventes.get(position);
        holder.lbl_hist_product.setText(vente.produit.nom);
        holder.lbl_hist_date.setText(vente.getDateFormated());
        holder.lbl_hist_qtt.setText(String.format("%.2f", vente.getQuantite()));
        holder.lbl_hist_price.setText(String.format("%.2f", vente.prix));
        holder.lbl_hist_tot.setText(String.format("%.2f", vente.getTotal()));
        holder.lbl_hist_payee.setText(String.format("%.2f", vente.payee));
        holder.lbl_hist_reste.setText(String.format("%.2f", vente.getReste()));
        int rouge = context.getResources().getColor(R.color.colorRed);
        if (vente.getReste()>0) holder.lbl_hist_reste.setTextColor(rouge);
        if (vente.perimee){
            int lightRed = context.getResources().getColor(R.color.lightRed);
            holder.card_hist.setBackgroundColor(lightRed);
        }else {

            int blank = context.getResources().getColor(R.color.blank);
            holder.card_hist.setBackgroundColor(blank);
        }
        if (cloture!=null && !cloture.compiled){
            holder.view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    editItem(vente);
                    return false;
                }
            });
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
                            if (item_id == R.id.action_item_edit) editItem(vente);
                            if (item_id == R.id.action_item_delete) deleteItem(vente, position);
                            return false;
                        }
                    });
                    popup_menu.show();
                }
            });
        }else{
            holder.btn_hist_options.setVisibility(View.GONE);
        }
    }

    private void editItem(Vente vente) {
        KudandazaForm kudandaza_form = new KudandazaForm(context, vente.produit);
        kudandaza_form.setEdition(vente);
        kudandaza_form.show();
    }
    private void deleteItem(final Vente vente, final int position) {
        new AlertDialog.Builder(context)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle("Guhanagura").setMessage("Urakeneye vy'ukuri guhanagura?")
            .setPositiveButton("Hanagura", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Dao dao_action = null;
                    vente.delete(context);
                }
            })
            .setNegativeButton("Reka", null)
            .show();
    }
    @Override
    public int getItemCount() {
        return ventes.size();
    }

    public void setData(ArrayList<Vente> ventes) {
        parent.reset();
        this.ventes = ventes;
        for (Vente vente : ventes){
            parent.addToTotals(vente);
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView lbl_hist_product, lbl_hist_date, lbl_hist_qtt, lbl_hist_price,
                lbl_hist_tot, lbl_hist_payee, lbl_hist_reste, btn_hist_options;
        CardView card_hist;
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
            card_hist = itemView.findViewById(R.id.card_hist);
        }
    }
}
