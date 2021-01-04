package bi.konstrictor.urudandaza.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.ColumnArg;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.ArrayList;

import bi.konstrictor.urudandaza.DetailHistActivity;
import bi.konstrictor.urudandaza.InkoranyaMakuru;
import bi.konstrictor.urudandaza.R;
import bi.konstrictor.urudandaza.adapters.AdaptateurVentes;
import bi.konstrictor.urudandaza.dialogs.FilterActionForm;
import bi.konstrictor.urudandaza.interfaces.Filterable;
import bi.konstrictor.urudandaza.models.Cloture;
import bi.konstrictor.urudandaza.models.RemboursementVente;
import bi.konstrictor.urudandaza.models.Vente;

/**
 * A simple {@link Fragment} subclass.
 */
public class VenteFragment extends Fragment implements Filterable {
    private final DetailHistActivity context;
    TextView lbl_det_clot_tot, lbl_det_clot_rest;
    private View view;

    RecyclerView recycler_history;
    private ArrayList<Vente> ventes = new ArrayList<>();;
    private Double vente_tot = 0., vente_rest = 0.;
    private AdaptateurVentes adaptateur;
    public Cloture cloture = null;
    private double payee;

    public VenteFragment(DetailHistActivity context) {
        super();
        this.context = context;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cloture, container, false);
        setHasOptionsMenu(true);

        lbl_det_clot_tot = view.findViewById(R.id.lbl_det_clot_tot);
        lbl_det_clot_rest = view.findViewById(R.id.lbl_det_clot_rest);

        recycler_history = view.findViewById(R.id.recycler_clotures);
        recycler_history.setLayoutManager(new GridLayoutManager(context, 1));
//        recycler_history.setLayoutManager(new FlexboxLayoutManager(this, FlexDirection.ROW, FlexWrap.WRAP));

        adaptateur = new AdaptateurVentes(context, ventes, this);
        recycler_history.addItemDecoration(new DividerItemDecoration(recycler_history.getContext(), DividerItemDecoration.VERTICAL));
        recycler_history.setAdapter(adaptateur);
        chargerStock();
        return view;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_filtre){
            new FilterActionForm(context, this).show();
        }else if(id == R.id.action_pay){
            showPayDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showPayDialog() {
        Double montant=0.;
        for (Vente as : ventes) {
            montant += as.getTotal() - as.payee;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Arishe angahe?");
        final EditText input = new EditText(context);
        input.setText(montant.toString());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);
        builder.setPositiveButton("Sawa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                payee = Double.parseDouble(input.getText().toString());
                for (int i=0; i<ventes.size(); i++) {
                    final Vente vente = ventes.get(i);
                    final double total = vente.getTotal();
                    RemboursementVente remboursement;
                    if (payee >= total) {
                        remboursement = new RemboursementVente(vente, total, "");
                        payee -= total;
                    } else {
                        remboursement = new RemboursementVente(vente, payee, "");
                        payee = 0.;
                    }
                    remboursement.create(context);
                    payee -= remboursement.payee;
                }
            }
        });
        builder.setNegativeButton("Reka", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
    public void setTot(Double vente_tot) {
        this.vente_tot = vente_tot;
        lbl_det_clot_tot.setText(vente_tot.toString());
    }
    public void setRest(Double vente_rest) {
        this.vente_rest = vente_rest;
        lbl_det_clot_rest.setText(vente_rest.toString());
    }
    public void addToTotals(Vente history) {
        if(history.perimee) return;
        setTot(vente_tot +history.getTotal());
        setRest(vente_rest+history.getReste());
    }
    private void chargerStock() {
        try {
            Dao dao_as = new InkoranyaMakuru(context).getDao(Vente.class);
            Where where = dao_as.queryBuilder().where();
            if (context.filters == null && context.dates == null ) return;
            if (context.is_dette)
                where.ne("total", new ColumnArg("payee")).and();
            if (context.dates != null && context.dates.size()>1){
                where
                    .ge("date", context.dates.get(0)).and()
                    .le("date",context.dates.get(1)).and();
            }
            if (context.filters != null && context.filters.size()>0) {
                for (String clef : context.filters.keySet()) {
                    where.eq(clef, context.filters.get(clef));
                }
                // Join this loop's wheres by and
                if (context.filters.size()>1) where.and(context.filters.size());
            }
            setProducts((ArrayList<Vente>) where.query());
//            produits.addAll(produits);
            adaptateur.setData(ventes);
        } catch (SQLException e) {
            Toast.makeText(context, "Erreur de connection à la base", Toast.LENGTH_LONG).show();
        }
    }

    public void reset() {
        setRest(0.); setTot(0.);
    }

    public void refresh() {
        chargerStock();
    }

    public ArrayList<Vente> getProducts() {
        return ventes;
    }

    public void setProducts(ArrayList<Vente> ventes) {
        context.ventes = ventes;
        this.ventes = ventes;
    }

    @Override
    public void performFiltering(Boolean dette, Boolean perimee) {
        ArrayList<Vente> filtered = new ArrayList<>();
        for (Vente as : ventes){
            if(perimee && as.perimee){
                filtered.add(as); continue;
            }
            if(dette && as.getReste()>0) {
                filtered.add(as);
            }
        }
        adaptateur.setData(filtered);
    }

    @Override
    public void cancelFiltering() {
        adaptateur.setData(ventes);
        return;
    }
}
