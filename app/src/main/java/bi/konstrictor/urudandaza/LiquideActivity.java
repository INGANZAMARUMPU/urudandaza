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

import bi.konstrictor.urudandaza.adapters.AdaptateurLiquide;
import bi.konstrictor.urudandaza.dialogs.ActionLiquideForm;
import bi.konstrictor.urudandaza.models.Liquide;

public class LiquideActivity extends RefreshableActivity {
    private ArrayList<Liquide> depenses;
    private AdaptateurLiquide adaptateur;
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
        adaptateur = new AdaptateurLiquide(LiquideActivity.this, depenses);
        recycler_client.addItemDecoration(new DividerItemDecoration(recycler_client.getContext(), DividerItemDecoration.VERTICAL));
        recycler_client.setAdapter(adaptateur);

        chargerDepenses();
    }

    private void chargerDepenses() {
        try {
            Dao dao_depenses = new InkoranyaMakuru(this).getDao(Liquide.class);
            depenses = (ArrayList<Liquide>) dao_depenses.queryForAll();
            adaptateur.setData(depenses);
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erreur de connection à la base", Toast.LENGTH_LONG).show();
        }
    }
    public void startAddDepenses(View view){
        view.startAnimation(AnimationUtils.loadAnimation(LiquideActivity.this, R.anim.button_fadein));
        ActionLiquideForm client_form = new ActionLiquideForm(LiquideActivity.this);
        client_form.show();
    }
    @Override
    public void refresh() {
        chargerDepenses();
    }
}
