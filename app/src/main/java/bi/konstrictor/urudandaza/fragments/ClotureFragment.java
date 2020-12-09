package bi.konstrictor.urudandaza.fragments;

import android.os.Bundle;
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
import bi.konstrictor.urudandaza.RefreshableActivity;
import bi.konstrictor.urudandaza.adapters.AdaptateurHist;
import bi.konstrictor.urudandaza.models.ActionStock;
import bi.konstrictor.urudandaza.models.Cloture;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClotureFragment extends Fragment {
    private final DetailHistActivity context;
    TextView lbl_det_hist_achat_tot, lbl_det_hist_achat_rest, lbl_det_hist_vente_tot, lbl_det_hist_vente_reste;
    private View view;

    public boolean is_dette=false;
    RecyclerView recycler_history;
    private ArrayList<ActionStock> products = new ArrayList<>();;
    private Double achat_tot = 0., achat_rest = 0., vente_tot = 0., vente_reste = 0.;
    private AdaptateurHist adaptateur;

    private String filtre, valeur;
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

        recycler_history = view.findViewById(R.id.recycler_history);
        recycler_history.setLayoutManager(new GridLayoutManager(context, 1));
//        recycler_history.setLayoutManager(new FlexboxLayoutManager(this, FlexDirection.ROW, FlexWrap.WRAP));

        adaptateur = new AdaptateurHist(context, products);
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
            Where where = dao_as.queryBuilder().where().eq(filtre, valeur);
            if (is_dette)
                where = where.and().ne("total", new ColumnArg("payee"));
            setProducts((ArrayList<ActionStock>) where.query());
//            produits.addAll(produits);
            adaptateur.setData(products);
            adaptateur.notifyDataSetChanged();
        } catch (SQLException e) {
            Toast.makeText(context, "Erreur de connection à la base", Toast.LENGTH_LONG).show();
        }
    }

    public void refresh() {
        lbl_det_hist_achat_tot.setText("0");
        lbl_det_hist_achat_rest.setText("0");
        lbl_det_hist_vente_tot.setText("0");
        lbl_det_hist_vente_reste.setText("0");
        chargerStock();
    }

    public ArrayList<ActionStock> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<ActionStock> products) {
        context.produits = products;
        this.products = products;
    }
}
