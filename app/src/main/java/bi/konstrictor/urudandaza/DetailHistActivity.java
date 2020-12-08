package bi.konstrictor.urudandaza;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.ColumnArg;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.ArrayList;

import bi.konstrictor.urudandaza.adapters.AdaptateurHist;
import bi.konstrictor.urudandaza.dialogs.ConfirmKudandaza;
import bi.konstrictor.urudandaza.models.ActionStock;
import bi.konstrictor.urudandaza.models.Cloture;

public class DetailHistActivity extends RefreshableActivity {

    public boolean is_dette=false;
    RecyclerView recycler_history;
    ArrayList<ActionStock> produits;
    TextView lbl_det_hist_achat_tot, lbl_det_hist_achat_rest, lbl_det_hist_vente_tot, lbl_det_hist_vente_reste;
    private Double achat_tot = 0., achat_rest = 0., vente_tot = 0., vente_reste = 0.;
    private AdaptateurHist adaptateur;

    private String filtre, valeur;
    public Cloture cloture = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_det_hist);
        Toolbar toolbar = findViewById(R.id.history_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        filtre = getIntent().getExtras().getString("filtre");
        valeur = getIntent().getExtras().getString("valeur");
        is_dette = getIntent().getExtras().getBoolean("is_dette");
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
        chargerStock();
    }
    @Override
    public boolean onCreateOptionsMenu( Menu menu) {
        if (cloture!=null && !cloture.compiled) {
            getMenuInflater().inflate(R.menu.cloture_menu, menu);
            return true;
        }
        if (is_dette) {
            getMenuInflater().inflate(R.menu.pay_menu, menu);
            return true;
        }
        return false;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_cloture) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Guhanagura")
                    .setMessage("Urakeneye vy'ukuri gufunga ibikorwa vyakozwe uno musi?\n" +
                            "M.N: Ubifunze ntuba ugishoboye kubihindagura")
                    .setPositiveButton("Funga", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cloture.cloturer(DetailHistActivity.this);
                            Intent data = new Intent();
                            data.setData(Uri.parse("changed"));
                            setResult(RESULT_OK, data);
                            finish();
                        }
                    })
                    .setNegativeButton("Reka", null)
                    .show();
        }else if(id == R.id.action_filtre){
        }else if(id == R.id.action_pay){
            Double montant=0.;
            for (ActionStock as:produits){
                montant += as.getTotal();
            }
            ConfirmKudandaza kurangura_form = new ConfirmKudandaza(this, produits, montant);
            kurangura_form.setEdition(true, produits.get(0).personne);
            kurangura_form.show();
        }
        return super.onOptionsItemSelected(item);
    }
    public void addToTotals(ActionStock history) {
        if(history.perimee) return;
        setAchat_tot(achat_tot +history.getAchatTotal());
        setAchat_rest(achat_rest+history.getAchatReste());
        setVente_tot(vente_tot+history.getVenteTotal());
        setVente_reste(vente_reste+history.getVenteReste());
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
    private void chargerStock() {
        try {
            Dao dao_as = new InkoranyaMakuru(this).getDao(ActionStock.class);
            Where where = dao_as.queryBuilder().where().eq(filtre, valeur);
            if (is_dette)
                where = where.and().ne("total", new ColumnArg("payee"));
            produits = (ArrayList<ActionStock>) where.query();
//            produits.addAll(produits);
            adaptateur.setData(produits);
            adaptateur.notifyDataSetChanged();
        } catch (SQLException e) {
            Toast.makeText(this, "Erreur de connection à la base", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void refresh() {
        lbl_det_hist_achat_tot.setText("0");
        lbl_det_hist_achat_rest.setText("0");
        lbl_det_hist_vente_tot.setText("0");
        lbl_det_hist_vente_reste.setText("0");
        chargerStock();
    }
}
