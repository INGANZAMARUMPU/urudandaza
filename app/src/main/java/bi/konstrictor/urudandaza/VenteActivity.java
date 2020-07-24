package bi.konstrictor.urudandaza;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;

import bi.konstrictor.urudandaza.models.ActionStock;
import bi.konstrictor.urudandaza.models.Produit;

public class VenteActivity extends AppCompatActivity {

    private RecyclerView recycler_ibidandazwa;
    private ArrayList<Produit> produits;
    private AdaptateurVente adaptateur;
    private ToggleButton btn_toggle_vente;
    private TextView lbl_vente_quantite;
    private LinearLayout layout_vente;
    public Boolean EDITION = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vente);
        Toolbar toolbar = findViewById(R.id.vente_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recycler_ibidandazwa = findViewById(R.id.recycler_vente);
        btn_toggle_vente = findViewById(R.id.btn_toggle_vente);
        lbl_vente_quantite = findViewById(R.id.lbl_vente_quantite);
        layout_vente = findViewById(R.id.layout_vente);

        recycler_ibidandazwa.setLayoutManager(new GridLayoutManager(this, 1));
//        recycler_ibidandazwa.setLayoutManager(new FlexboxLayoutManager(this, FlexDirection.ROW, FlexWrap.WRAP));
        produits = new ArrayList<>();
        adaptateur = new AdaptateurVente(VenteActivity.this, produits);
        recycler_ibidandazwa.addItemDecoration(new DividerItemDecoration(recycler_ibidandazwa.getContext(), DividerItemDecoration.VERTICAL));
        recycler_ibidandazwa.setAdapter(adaptateur);
        btn_toggle_vente.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    EDITION = true;
                    layout_vente.setVisibility(View.VISIBLE);
                }else{
                    EDITION = false;
                    layout_vente.setVisibility(View.GONE);
                }
                adaptateur.notifyDataSetChanged();
            }
        });
        chargerStock();
    }

    private void chargerStock() {
        try {
            Dao dao_stocks = new InkoranyaMakuru(this).getDaoProduit();
            produits = (ArrayList<Produit>) dao_stocks.queryForAll();
//            stocks.addAll(stocks);
            adaptateur.setData(produits);
            adaptateur.notifyDataSetChanged();
        } catch (SQLException e) {
            Toast.makeText(this, "Erreur de connection à la base", Toast.LENGTH_LONG).show();
        }
    }
}
