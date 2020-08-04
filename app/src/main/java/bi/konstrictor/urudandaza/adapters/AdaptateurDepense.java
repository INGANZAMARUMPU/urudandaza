package bi.konstrictor.urudandaza.adapters;

import android.content.DialogInterface;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;

import bi.konstrictor.urudandaza.DepensesActivity;
import bi.konstrictor.urudandaza.InkoranyaMakuru;
import bi.konstrictor.urudandaza.R;
import bi.konstrictor.urudandaza.models.Depense;
import bi.konstrictor.urudandaza.models.ProxyAction;

public class AdaptateurDepense extends RecyclerView.Adapter<AdaptateurDepense.ViewHolder> {

    private DepensesActivity context;
    private ArrayList<Depense> stocks;

    public AdaptateurDepense(DepensesActivity context, ArrayList<Depense> stocks) {
        this.context = context;
        this.stocks = stocks;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_depenses, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

    }
    @Override
    public int getItemCount() {
        return stocks.size();
    }

    public void setData(ArrayList<Depense> produit) {
        this.stocks = produit;
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
