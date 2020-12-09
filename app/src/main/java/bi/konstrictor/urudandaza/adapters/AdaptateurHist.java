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
import bi.konstrictor.urudandaza.dialogs.KuranguraForm;
import bi.konstrictor.urudandaza.interfaces.SummableActionStock;
import bi.konstrictor.urudandaza.models.ActionStock;
import bi.konstrictor.urudandaza.models.Cloture;

public class AdaptateurHist extends RecyclerView.Adapter<AdaptateurHist.ViewHolder> {

    private DetailHistActivity context;
    private ArrayList<ActionStock> histories;
    private Cloture cloture = null;
    private boolean is_dette;
    SummableActionStock parent;

    public AdaptateurHist(DetailHistActivity context, ArrayList<ActionStock> histories, SummableActionStock parent) {
        this.context = context;
        this.histories = histories;
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
        final ActionStock historie = histories.get(position);
        Double reste = historie.perimee ? 0 : historie.getTotal() - historie.payee;
        Double qtt = Math.abs(historie.getQuantite());
        holder.lbl_hist_product.setText(historie.produit.nom);
        holder.lbl_hist_date.setText(historie.getDateFormated());
        holder.lbl_hist_qtt.setText(qtt.toString());
        holder.lbl_hist_price.setText(historie.getPrix().toString());
        holder.lbl_hist_tot.setText(historie.getTotal().toString());
        holder.lbl_hist_payee.setText(historie.payee.toString());
        holder.lbl_hist_reste.setText(reste.toString());
        int rouge = context.getResources().getColor(R.color.colorRed);
        if (reste>0) holder.lbl_hist_reste.setTextColor(rouge);
        if (historie.perimee){
            int lightRed = context.getResources().getColor(R.color.lightRed);
            holder.card_hist.setBackgroundColor(lightRed);
        }
        if (historie.isAchat()){
            int lightBlue = context.getResources().getColor(R.color.lightBlue);
            holder.card_hist.setBackgroundColor(lightBlue);
        }
        if ((cloture!=null && !cloture.compiled) | (is_dette)){
            holder.view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    editItem(historie);
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
                            if (item_id == R.id.action_item_edit) editItem(historie);
                            if (item_id == R.id.action_item_delete) deleteItem(historie, position);
                            return false;
                        }
                    });
                    popup_menu.show();
                }
            });
        }else{
            holder.btn_hist_options.setVisibility(View.GONE);
        }
        parent.addToTotals(historie);
    }
    private void editItem(ActionStock historie) {
        if (historie.isAchat()) {
            KuranguraForm kurangura_form = new KuranguraForm(context, historie.produit);
            kurangura_form.setEdition(historie);
            kurangura_form.show();
        } else {
            KudandazaForm kudandaza_form = new KudandazaForm(context, historie.produit);
            kudandaza_form.setEdition(historie);
            kudandaza_form.show();
        }
    }
    private void deleteItem(final ActionStock historie, final int position) {
        new AlertDialog.Builder(context)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle("Guhanagura").setMessage("Urakeneye vy'ukuri guhanagura?")
            .setPositiveButton("Hanagura", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Dao dao_action = null;
                    historie.delete(context);
                }
            })
            .setNegativeButton("Reka", null)
            .show();
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
