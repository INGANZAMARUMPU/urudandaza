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

import bi.konstrictor.urudandaza.adapters.AdaptateurClient;
import bi.konstrictor.urudandaza.dialogs.ClientForm;
import bi.konstrictor.urudandaza.interfaces.RefreshableActivity;
import bi.konstrictor.urudandaza.models.Personne;

public class ClientActivity extends RefreshableActivity {

    private ArrayList<Personne> clients;
    private AdaptateurClient adaptateur;
    private RecyclerView recycler_client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        Toolbar toolbar = findViewById(R.id.client_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recycler_client = findViewById(R.id.recycler_client);
        recycler_client.setLayoutManager(new GridLayoutManager(this, 1));
//        recycler_client.setLayoutManager(new FlexboxLayoutManager(this, FlexDirection.ROW, FlexWrap.WRAP));

        clients = new ArrayList<>();
        adaptateur = new AdaptateurClient(ClientActivity.this, clients);
        recycler_client.addItemDecoration(new DividerItemDecoration(recycler_client.getContext(), DividerItemDecoration.VERTICAL));
        recycler_client.setAdapter(adaptateur);

        chargerClient();
    }

    private void chargerClient() {
        try {
            Dao dao_clients = new InkoranyaMakuru(this).getDao(Personne.class);
            clients = (ArrayList<Personne>) dao_clients.queryForAll();
            adaptateur.setData(clients);
            adaptateur.notifyDataSetChanged();
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erreur de connection à la base", Toast.LENGTH_LONG).show();
        }
    }
    public void startAddClient(View view){
        view.startAnimation(AnimationUtils.loadAnimation(ClientActivity.this, R.anim.button_fadein));
        ClientForm client_form = new ClientForm(ClientActivity.this);
        client_form.show();
    }
    @Override
    public void refresh() {
        chargerClient();
    }
}
