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
import bi.konstrictor.urudandaza.dialogs.KuranguraForm;
import bi.konstrictor.urudandaza.fragments.AchatFragment;
import bi.konstrictor.urudandaza.models.Achat;
import bi.konstrictor.urudandaza.models.Cloture;

public class AdaptateurAchats extends RecyclerView.Adapter<AdaptateurAchats.ViewHolder> {

    private DetailHistActivity context;
    private ArrayList<Achat> achats;
    private Cloture cloture = null;
    private boolean is_dette;
    AchatFragment parent;

    public AdaptateurAchats(DetailHistActivity context, ArrayList<Achat> achats, AchatFragment parent) {
        this.context = context;
        this.achats = achats;
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
        final Achat achat = achats.get(position);
        Double qtt = Math.abs(achat.getQuantite());
        holder.lbl_hist_product.setText(achat.produit.nom);
        holder.lbl_hist_date.setText(achat.getDateFormated());
        holder.lbl_hist_qtt.setText(String.format("%.2f", qtt));
        holder.lbl_hist_price.setText(String.format("%.2f", achat.getPrixUnitaire()));
        holder.lbl_hist_tot.setText(String.format("%.2f", achat.prix));
        holder.lbl_hist_payee.setText(String.format("%.2f", achat.payee));
        holder.lbl_hist_reste.setText(String.format("%.2f", achat.getReste()));
        int rouge = context.getResources().getColor(R.color.colorRed);
        if (achat.getReste()>0) holder.lbl_hist_reste.setTextColor(rouge);
        if (cloture!=null && !cloture.compiled){
            holder.view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    editItem(achat);
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
                            if (item_id == R.id.action_item_edit) editItem(achat);
                            if (item_id == R.id.action_item_delete) deleteItem(achat, position);
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

    private void editItem(Achat achat) {
        KuranguraForm kurangura_form = new KuranguraForm(context, achat.produit);
        kurangura_form.setEdition(achat);
        kurangura_form.show();
    }
    private void deleteItem(final Achat achat, final int position) {
        new AlertDialog.Builder(context)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle("Guhanagura").setMessage("Urakeneye vy'ukuri guhanagura?")
            .setPositiveButton("Hanagura", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Dao dao_action = null;
                    achat.delete(context);
                }
            })
            .setNegativeButton("Reka", null)
            .show();
    }
    @Override
    public int getItemCount() {
        return achats.size();
    }

    public void setData(ArrayList<Achat> achats) {
        parent.reset();
        this.achats = achats;
        for (Achat as : achats){
            parent.addToTotals(as);
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
