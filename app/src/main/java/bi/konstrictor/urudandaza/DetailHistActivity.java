package bi.konstrictor.urudandaza;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import bi.konstrictor.urudandaza.dialogs.ConfirmKudandaza;
import bi.konstrictor.urudandaza.interfaces.RefreshableActivity;
import bi.konstrictor.urudandaza.models.ActionStock;
import bi.konstrictor.urudandaza.models.Cloture;
import bi.konstrictor.urudandaza.pageadapters.CloturePageAdapter;

public class DetailHistActivity extends RefreshableActivity {

    public CloturePageAdapter cloture_adapter;
    public ViewPager view_pager_cloture;
    private TabLayout tab_layout_cloture;
    public String filtre, valeur;
    public Boolean is_dette;
    public Cloture cloture;
    public ArrayList<ActionStock> produits = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_det_hist);
        Toolbar toolbar = findViewById(R.id.history_toolbar);

        filtre = getIntent().getExtras().getString("filtre");
        valeur = getIntent().getExtras().getString("valeur");
        is_dette = getIntent().getExtras().getBoolean("is_dette");
        cloture = (Cloture) getIntent().getSerializableExtra("cloture");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    @Override
    public void refresh() {

    }
}
