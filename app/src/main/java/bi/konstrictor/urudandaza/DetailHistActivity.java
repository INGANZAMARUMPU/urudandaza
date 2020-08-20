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
    private int cloture_id;
    private Cloture cloture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_det_hist);
        Toolbar toolbar = findViewById(R.id.history_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cloture_id = getIntent().getExtras().getInt("cloture_id");
        cloture = (Cloture) getIntent().getSerializableExtra("cloture");
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
        fillLabels();
        chargerStock();
    }

    private void fillLabels() {
        lbl_det_hist_achat_tot.setText(cloture.achat.toString());
        lbl_det_hist_achat_rest.setText(cloture.getAchatReste().toString());
        lbl_det_hist_vente_tot.setText(cloture.getVente().toString());
        lbl_det_hist_vente_reste.setText(cloture.getVenteReste().toString());
    }

    private void chargerStock() {
        try {
            Dao dao_as = new InkoranyaMakuru(this).getDaoActionStock();
            produits = (ArrayList<ActionStock>) dao_as.queryBuilder().where().eq("cloture_id",cloture_id).query();
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
