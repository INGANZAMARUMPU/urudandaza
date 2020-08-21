package bi.konstrictor.urudandaza;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;

import bi.konstrictor.urudandaza.adapters.AdaptateurCloture;
import bi.konstrictor.urudandaza.models.Cloture;

public class ClotureActivity extends RefreshableActivity {

    public static final int CLOTURE_CODE = 10;
    RecyclerView recycler_history;
    ArrayList<Cloture> clotures;
    private AdaptateurCloture adaptateur;
    public  TextView lbl_hist_achat_tot, lbl_hist_achat_rest, lbl_hist_vente_tot, lbl_hist_vente_reste;
    public Double achat_tot=0., achat_rest=0., vente_tot=0., vente_reste=0.;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = findViewById(R.id.history_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lbl_hist_achat_tot = findViewById(R.id.lbl_hist_achat_tot);
        lbl_hist_achat_rest = findViewById(R.id.lbl_hist_achat_rest);
        lbl_hist_vente_tot = findViewById(R.id.lbl_hist_vente_tot);
        lbl_hist_vente_reste = findViewById(R.id.lbl_hist_vente_reste);

        recycler_history = findViewById(R.id.recycler_history);
        recycler_history.setLayoutManager(new GridLayoutManager(this, 1));
//        recycler_history.setLayoutManager(new FlexboxLayoutManager(this, FlexDirection.ROW, FlexWrap.WRAP));

        clotures = new ArrayList<>();
        adaptateur = new AdaptateurCloture(ClotureActivity.this, clotures);
        recycler_history.addItemDecoration(new DividerItemDecoration(recycler_history.getContext(), DividerItemDecoration.VERTICAL));
        recycler_history.setAdapter(adaptateur);
        chargerStock();
    }
    private void chargerStock() {
        try {
            Dao dao_clotures = new InkoranyaMakuru(this).getDaoCloture();
            clotures = (ArrayList<Cloture>) dao_clotures.queryForAll();
//            clotures.addAll(clotures);
            adaptateur.setData(clotures);
            adaptateur.notifyDataSetChanged();
        } catch (SQLException e) {
            Toast.makeText(this, "Erreur de connection à la base", Toast.LENGTH_LONG).show();
        }
    }
    public void increaseAchatTot(Double achat_tot) {
        this.achat_tot += achat_tot;
        this.lbl_hist_achat_tot.setText(this.achat_tot.toString());
    }

    public void increaseAchatRest(Double achat_rest) {
        this.achat_rest += achat_rest;
        this.lbl_hist_achat_rest.setText(this.achat_rest.toString());
    }

    public void increaseVenteTot(Double vente_tot) {
        this.vente_tot += Math.abs(vente_tot);
        this.lbl_hist_vente_tot.setText(this.vente_tot.toString());
    }

    public void increaseVenteReste(Double vente_reste) {
        this.vente_reste += vente_reste;
        this.lbl_hist_vente_reste.setText(this.vente_reste.toString());
    }
    @Override
    public void refresh() {
        chargerStock();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CLOTURE_CODE) {
            if (resultCode == RESULT_OK) {
                String returned_result = data.getData().toString();
                if (returned_result.equals("changed")){
                    refresh();
                }
            }
        }
    }
}
