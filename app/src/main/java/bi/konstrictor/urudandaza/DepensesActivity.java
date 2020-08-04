package bi.konstrictor.urudandaza;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;

import bi.konstrictor.urudandaza.adapters.AdaptateurDepense;
import bi.konstrictor.urudandaza.dialogs.DepensesForm;
import bi.konstrictor.urudandaza.models.Depense;
import bi.konstrictor.urudandaza.models.Depense;

public class DepensesActivity extends RefreshableActivity {

    private ArrayList<Depense> depenses;
    private AdaptateurDepense adaptateur;
    private RecyclerView recycler_client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_depenses);
        Toolbar toolbar = findViewById(R.id.depenses_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recycler_client = findViewById(R.id.recycler_client);
        recycler_client.setLayoutManager(new GridLayoutManager(this, 1));
//        recycler_client.setLayoutManager(new FlexboxLayoutManager(this, FlexDirection.ROW, FlexWrap.WRAP));

        depenses = new ArrayList<>();
        adaptateur = new AdaptateurDepense(DepensesActivity.this, depenses);
        recycler_client.addItemDecoration(new DividerItemDecoration(recycler_client.getContext(), DividerItemDecoration.VERTICAL));
        recycler_client.setAdapter(adaptateur);

        chargerDepenses();
    }

    private void chargerDepenses() {
        try {
            Dao dao_depenses = new InkoranyaMakuru(this).getDaoDepense();
            depenses = (ArrayList<Depense>) dao_depenses.queryForAll();
            adaptateur.setData(depenses);
            adaptateur.notifyDataSetChanged();
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erreur de connection à la base", Toast.LENGTH_LONG).show();
        }
    }
    public void startAddDepenses(View view){
        view.startAnimation(AnimationUtils.loadAnimation(DepensesActivity.this, R.anim.button_fadein));
        DepensesForm client_form = new DepensesForm(DepensesActivity.this);
        client_form.show();
    }
    @Override
    public void refresh() {
        chargerDepenses();
    }
}
