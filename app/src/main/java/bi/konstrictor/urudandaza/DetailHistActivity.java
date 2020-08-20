package bi.konstrictor.urudandaza;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;

import bi.konstrictor.urudandaza.adapters.AdaptateurHist;
import bi.konstrictor.urudandaza.models.ActionStock;
import bi.konstrictor.urudandaza.models.Cloture;

public class DetailHistActivity extends RefreshableActivity {

    RecyclerView recycler_history;
    ArrayList<ActionStock> produits;
    TextView lbl_det_hist_achat_tot, lbl_det_hist_achat_rest, lbl_det_hist_vente_tot, lbl_det_hist_vente_reste;
    private AdaptateurHist adaptateur;
    private String filtre, valeur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_det_hist);
        Toolbar toolbar = findViewById(R.id.history_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        filtre = getIntent().getExtras().getString("filtre");
        valeur = getIntent().getExtras().getString("valeur");
        lbl_det_hist_achat_tot = findViewById(R.id.lbl_det_hist_achat_tot);
        lbl_det_hist_achat_rest = findViewById(R.id.lbl_det_hist_achat_rest);
        lbl_det_hist_vente_tot = findViewById(R.id.lbl_det_hist_vente_tot);
        lbl_det_hist_vente_reste = findViewById(R.id.lbl_det_hist_vente_reste);

        recycler_history = findViewById(R.id.recycler_history);
        recycler_history.setLayoutManager(new GridLayoutManager(this, 1));
//        recycler_history.setLayoutManager(new FlexboxLayoutManager(this, FlexDirection.ROW, FlexWrap.WRAP));

        produits = new ArrayList<>();
        adaptateur = new AdaptateurHist(DetailHistActivity.this, produits);
        recycler_history.addItemDecoration(new DividerItemDecoration(recycler_history.getContext(), DividerItemDecoration.VERTICAL));
        recycler_history.setAdapter(adaptateur);
        chargerStock();
    }

    public void addToTotals(ActionStock history) {
        Double achat_tot = Double.parseDouble(lbl_det_hist_achat_tot.getText().toString())+history.getAchatTotal();
        Double achat_rest = Double.parseDouble(lbl_det_hist_achat_rest.getText().toString())+history.getAchatReste();
        Double vente_tot = Double.parseDouble(lbl_det_hist_vente_tot.getText().toString())+history.getVenteTotal();
        Double vente_reste = Double.parseDouble(lbl_det_hist_vente_reste.getText().toString())+history.getVenteReste();

        lbl_det_hist_achat_tot.setText(achat_tot.toString());
        lbl_det_hist_achat_rest.setText(achat_rest.toString());
        lbl_det_hist_vente_tot.setText(vente_tot.toString());
        lbl_det_hist_vente_reste.setText(vente_reste.toString());
    }

    private void chargerStock() {
        try {
            Dao dao_as = new InkoranyaMakuru(this).getDaoActionStock();
            produits = (ArrayList<ActionStock>) dao_as.queryBuilder().where().eq(filtre,valeur).query();
//            produits.addAll(produits);
            adaptateur.setData(produits);
            adaptateur.notifyDataSetChanged();
        } catch (SQLException e) {
            Toast.makeText(this, "Erreur de connection à la base", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void refresh() {
        chargerStock();
    }
}
