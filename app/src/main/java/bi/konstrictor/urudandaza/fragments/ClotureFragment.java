package bi.konstrictor.urudandaza.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import bi.konstrictor.urudandaza.adapters.AdaptateurHist;
import bi.konstrictor.urudandaza.interfaces.Filterable;
import bi.konstrictor.urudandaza.interfaces.SummableActionStock;
import bi.konstrictor.urudandaza.models.ActionStock;
import bi.konstrictor.urudandaza.models.Cloture;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClotureFragment extends Fragment implements SummableActionStock, Filterable {
    private final DetailHistActivity context;
    TextView lbl_det_hist_achat_tot, lbl_det_hist_achat_rest, lbl_det_hist_vente_tot, lbl_det_hist_vente_reste;
    private View view;

    RecyclerView recycler_history;
    private ArrayList<ActionStock> products = new ArrayList<>();;
    private Double achat_tot = 0., achat_rest = 0., vente_tot = 0., vente_reste = 0.;
    private AdaptateurHist adaptateur;
    public Cloture cloture = null;

    public ClotureFragment(DetailHistActivity context) {
        super();
        this.context = context;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cloture, container, false);

        lbl_det_hist_achat_tot = view.findViewById(R.id.lbl_det_hist_achat_tot);
        lbl_det_hist_achat_rest = view.findViewById(R.id.lbl_det_hist_achat_rest);
        lbl_det_hist_vente_tot = view.findViewById(R.id.lbl_det_hist_vente_tot);
        lbl_det_hist_vente_reste = view.findViewById(R.id.lbl_det_hist_vente_reste);

        recycler_history = view.findViewById(R.id.recycler_clotures);
        recycler_history.setLayoutManager(new GridLayoutManager(context, 1));
//        recycler_history.setLayoutManager(new FlexboxLayoutManager(this, FlexDirection.ROW, FlexWrap.WRAP));

        adaptateur = new AdaptateurHist(context, products, this);
        recycler_history.addItemDecoration(new DividerItemDecoration(recycler_history.getContext(), DividerItemDecoration.VERTICAL));
        recycler_history.setAdapter(adaptateur);
        chargerStock();
        return view;
    }

    public void setAchat_tot(Double achat_tot) {
        this.achat_tot = achat_tot;
        lbl_det_hist_achat_tot.setText(achat_tot.toString());
    }
    public void setAchat_rest(Double achat_rest) {
        this.achat_rest = achat_rest;
        lbl_det_hist_achat_rest.setText(achat_rest.toString());
    }
    public void setVente_tot(Double vente_tot) {
        this.vente_tot = vente_tot;
        lbl_det_hist_vente_tot.setText(vente_tot.toString());
    }
    public void setVente_reste(Double vente_reste) {
        this.vente_reste = vente_reste;
        lbl_det_hist_vente_reste.setText(vente_reste.toString());
    }
    public void addToTotals(ActionStock history) {
        if(history.perimee) return;
        setAchat_tot(achat_tot +history.getAchatTotal());
        setAchat_rest(achat_rest+history.getAchatReste());
        setVente_tot(vente_tot+history.getVenteTotal());
        setVente_reste(vente_reste+history.getVenteReste());
    }
    private void chargerStock() {
        try {
            Dao dao_as = new InkoranyaMakuru(context).getDao(ActionStock.class);
            Where where = dao_as.queryBuilder().where();
            if (context.filters == null && context.dates == null ) return;
            if (context.is_dette)
                where.ne("total", new ColumnArg("payee")).and();
            if (context.dates != null && context.dates.size()>1){
                where.between("date", context.dates.get(0), context.dates.get(1)).and();
            }
            if (context.filters != null && context.filters.size()>0) {
                for (String clef : context.filters.keySet()) {
                    where.eq(clef, context.filters.get(clef));
                }
                // Join this loop's wheres by and
                if (context.filters.size()>1) where.and(context.filters.size());
            }
            setProducts((ArrayList<ActionStock>) where.query());
//            produits.addAll(produits);
            adaptateur.setData(products);
        } catch (SQLException e) {
            Toast.makeText(context, "Erreur de connection à la base", Toast.LENGTH_LONG).show();
        }
    }

    public void reset() {
        setAchat_rest(0.); setAchat_tot(0.); setVente_tot(0.); setVente_reste(0.);
    }

    public void refresh() {
        chargerStock();
    }

    public ArrayList<ActionStock> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<ActionStock> products) {
        context.produits = products;
        this.products = products;
    }

    @Override
    public void performFiltering(Boolean in, Boolean out, Boolean dette, Boolean perimee) {
        ArrayList<ActionStock> filtered = new ArrayList<>();
        for (ActionStock as : products){
            if(perimee && as.perimee){
                filtered.add(as); continue;
            }
            if(in && as.isAchat()) {
                filtered.add(as); continue;
            }
            if(out && as.isVente()) {
                filtered.add(as); continue;
            }
            if(dette && as.isDette()) {
                filtered.add(as);
            }
        }
        adaptateur.setData(filtered);
    }

    @Override
    public void cancelFiltering() {
        adaptateur.setData(products);
        return;
    }
}
