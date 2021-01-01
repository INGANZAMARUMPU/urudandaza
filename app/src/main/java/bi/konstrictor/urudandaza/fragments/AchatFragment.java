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
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.ArrayList;

import bi.konstrictor.urudandaza.DetailHistActivity;
import bi.konstrictor.urudandaza.InkoranyaMakuru;
import bi.konstrictor.urudandaza.R;
import bi.konstrictor.urudandaza.adapters.AdaptateurAchats;
import bi.konstrictor.urudandaza.dialogs.FilterActionForm;
import bi.konstrictor.urudandaza.interfaces.Filterable;
import bi.konstrictor.urudandaza.models.Achat;
import bi.konstrictor.urudandaza.models.Cloture;
import bi.konstrictor.urudandaza.models.RemboursementAchat;

/**
 * A simple {@link Fragment} subclass.
 */
public class AchatFragment extends Fragment implements Filterable {
    private final DetailHistActivity context;
    TextView lbl_det_clot_tot, lbl_det_clot_rest;
    private View view;

    RecyclerView recycler_history;
    private ArrayList<Achat> achats = new ArrayList<>();;
    private Double achat_tot = 0., achat_reste = 0.;
    private AdaptateurAchats adaptateur;
    public Cloture cloture = null;
    private double payee;
    private RemboursementAchat dette;

    public AchatFragment(DetailHistActivity context) {
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

        adaptateur = new AdaptateurAchats(context, achats, this);
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
        for (Achat as : achats) {
            montant += as.getTotal() - as.payee;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Arishe angahe?");
        final EditText input = new EditText(context);
        input.setText(montant.toString());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);
        final ConnectionSource connection = new InkoranyaMakuru(context).getConnectionSource();
        builder.setPositiveButton("Sawa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                payee = Double.parseDouble(input.getText().toString());
                for (int i=0; i<achats.size(); i++) {
                    final Achat achat = achats.get(i);
                    final double total = achat.getTotal();
                    RemboursementAchat remboursement;
                    if (payee >= total) {
                        remboursement = new RemboursementAchat(achat, total, "");
                        payee -= total;
                    } else {
                        remboursement = new RemboursementAchat(achat, payee, "");
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

    public void setTot(Double achat_tot) {
        this.achat_tot = achat_tot;
        lbl_det_clot_tot.setText(achat_tot.toString());
    }
    public void setRest(Double achat_rest) {
        this.achat_reste = achat_rest;
        lbl_det_clot_rest.setText(achat_rest.toString());
    }
    public void addToTotals(Achat history) {
        setTot(achat_tot +history.getTotal());
        setRest(achat_reste+history.getReste());
    }
    private void chargerStock() {
        try {
            Dao dao_as = new InkoranyaMakuru(context).getDao(Achat.class);
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
            setProducts((ArrayList<Achat>) where.query());
//            produits.addAll(produits);
            adaptateur.setData(achats);
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

    public ArrayList<Achat> getProducts() {
        return achats;
    }

    public void setProducts(ArrayList<Achat> achats) {
        context.achats = achats;
        this.achats = achats;
    }

    @Override
    public void performFiltering(Boolean dette, Boolean perimee) {
        ArrayList<Achat> filtered = new ArrayList<>();
        for (Achat as : achats){
            if(dette && as.getReste()>0) {
                filtered.add(as);
            }
        }
        adaptateur.setData(filtered);
    }

    @Override
    public void cancelFiltering() {
        adaptateur.setData(achats);
        return;
    }
}
