package bi.konstrictor.urudandaza.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;

import bi.konstrictor.urudandaza.InkoranyaMakuru;
import bi.konstrictor.urudandaza.R;
import bi.konstrictor.urudandaza.adapters.AdaptateurStockBasic;
import bi.konstrictor.urudandaza.interfaces.RefreshableActivity;
import bi.konstrictor.urudandaza.models.Cloture;
import bi.konstrictor.urudandaza.models.ClotureProduit;
import bi.konstrictor.urudandaza.models.Produit;

/**
 * A simple {@link Fragment} subclass.
 */
public class StockFragment extends Fragment {
    private final RefreshableActivity context;
    private View view;
    private RecyclerView recycler_stock;
    private ArrayList<ClotureProduit> clotures;
    private AdaptateurStockBasic adaptateur;
    private Cloture cloture;

    public StockFragment(RefreshableActivity context, Cloture cloture) {
        super();
        this.context = context;
        this.cloture = cloture;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_stock, container, false);
        recycler_stock = view.findViewById(R.id.recycler_stock);
        recycler_stock.setLayoutManager(new GridLayoutManager(context, 1));

        adaptateur = new AdaptateurStockBasic(clotures);
        recycler_stock.addItemDecoration(new DividerItemDecoration(recycler_stock.getContext(), DividerItemDecoration.VERTICAL));
        recycler_stock.setAdapter(adaptateur);
        chargerStock();
        return view;
    }
    private void chargerStock() {
        try {
            Dao dao_clotures = new InkoranyaMakuru(context).getDao(ClotureProduit.class);
            Dao dao_produits = new InkoranyaMakuru(context).getDao(Produit.class);
            if (cloture != null && cloture.compiled) {
                clotures = (ArrayList<ClotureProduit>) dao_clotures
                        .queryForEq("cloture_id", cloture.id);
                adaptateur.setData(clotures);
            } else {
                ArrayList<Produit> produits;
                produits = (ArrayList<Produit>) dao_produits.queryForAll();
                adaptateur.setProduits(produits);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(context, "Erreur de connection à la base", Toast.LENGTH_LONG).show();
        }
    }

}
