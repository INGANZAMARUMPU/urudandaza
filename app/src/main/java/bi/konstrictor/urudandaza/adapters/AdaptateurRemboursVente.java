package bi.konstrictor.urudandaza.adapters;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import bi.konstrictor.urudandaza.R;
import bi.konstrictor.urudandaza.RemboursementActivity;
import bi.konstrictor.urudandaza.interfaces.RemboursementFragment;
import bi.konstrictor.urudandaza.models.RemboursementVente;

public class AdaptateurRemboursVente extends RecyclerView.Adapter<AdaptateurRemboursVente.ViewHolder> {

    private RemboursementActivity context;
    private ArrayList<RemboursementVente> remboursables;
    RemboursementFragment parent;

    public AdaptateurRemboursVente(RemboursementActivity context, ArrayList<RemboursementVente> remboursables, RemboursementFragment parent) {
        this.context = context;
        this.remboursables = remboursables;
        this.parent = parent;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_history, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final RemboursementVente remboursable = remboursables.get(position);
        Double qtt = Math.abs(remboursable.vente.getQuantite());
        holder.lbl_hist_product.setText(remboursable.vente.produit.nom);
        holder.lbl_hist_date.setText(remboursable.vente.getDateFormated());
        holder.lbl_hist_qtt.setText(String.format("%.2f", qtt));
        holder.lbl_hist_price.setText(String.format("%.2f", remboursable.vente.prix));
        holder.lbl_hist_tot.setText(String.format("%.2f", remboursable.vente.prix));
        holder.lbl_hist_payee.setText(String.format("%.2f", remboursable.payee));
        holder.lbl_hist_reste.setText(String.format("%.2f", remboursable.vente.getReste()));
        int rouge = context.getResources().getColor(R.color.colorRed);
        if (remboursable.vente.getReste()>0) holder.lbl_hist_reste.setTextColor(rouge);
    }

    private void editItem(RemboursementVente remboursable) {
//        KuranguraForm kurangura_form = new KuranguraForm(context, remboursable.produit);
//        kurangura_form.setEdition(remboursable);
//        kurangura_form.show();
    }
    private void deleteItem(final RemboursementVente remboursable, final int position) {
        new AlertDialog.Builder(context)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle("Guhanagura").setMessage("Urakeneye vy'ukuri guhanagura?")
            .setPositiveButton("Hanagura", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    remboursable.delete(context);
                }
            })
            .setNegativeButton("Reka", null)
            .show();
    }
    @Override
    public int getItemCount() {
        return remboursables.size();
    }

    public void setData(ArrayList<RemboursementVente> remboursables) {
        parent.setTot(0.);
        this.remboursables = remboursables;
        for (RemboursementVente as : remboursables){
            parent.addToTotals(as.payee);
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
