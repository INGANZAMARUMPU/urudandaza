package bi.konstrictor.urudandaza.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import bi.konstrictor.urudandaza.LiquideActivity;
import bi.konstrictor.urudandaza.R;
import bi.konstrictor.urudandaza.dialogs.ActionLiquideForm;
import bi.konstrictor.urudandaza.dialogs.ProductForm;
import bi.konstrictor.urudandaza.models.Liquide;

public class AdaptateurLiquide extends RecyclerView.Adapter<AdaptateurLiquide.ViewHolder> {

    private LiquideActivity context;
    private ArrayList<Liquide> liquides;

    public AdaptateurLiquide(LiquideActivity context, ArrayList<Liquide> liquides) {
        this.context = context;
        this.liquides = liquides;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_liquide, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Liquide liquide = liquides.get(position);
        holder.lbl_card_sortie_date.setText(liquide.getDateFormated());
        holder.lbl_card_sortie_motif.setText(liquide.motif);
        holder.lbl_card_sortie_somme.setText(liquide.montant.toString());
        holder.lbl_card_sortie_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.button_fadein));
                ActionLiquideForm liquide_form = new ActionLiquideForm(context);
                liquide_form.setEdition(liquide);
                liquide_form.show();
            }
        });
    }
    @Override
    public int getItemCount() {
        return liquides.size();
    }

    public void setData(ArrayList<Liquide> produit) {
        this.liquides = produit;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView lbl_card_sortie_date, lbl_card_sortie_somme, lbl_card_sortie_motif;
        Button lbl_card_sortie_edit;
        View view;

        public ViewHolder(final View itemView) {
            super(itemView);
            this.view = itemView;
            lbl_card_sortie_date = itemView.findViewById(R.id.lbl_card_sortie_date);
            lbl_card_sortie_somme = itemView.findViewById(R.id.lbl_card_sortie_somme);
            lbl_card_sortie_motif = itemView.findViewById(R.id.lbl_card_sortie_motif);
            lbl_card_sortie_edit = itemView.findViewById(R.id.lbl_card_sortie_edit);

        }
    }
}
