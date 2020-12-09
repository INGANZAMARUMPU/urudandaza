package bi.konstrictor.urudandaza;

import android.content.DialogInterface;
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

import bi.konstrictor.urudandaza.adapters.AdaptateurStock;
import bi.konstrictor.urudandaza.dialogs.ProductForm;
import bi.konstrictor.urudandaza.interfaces.RefreshableActivity;
import bi.konstrictor.urudandaza.models.Produit;

public class StockActivity extends RefreshableActivity {

    RecyclerView recycler_ibidandazwa;
    ArrayList<Produit> produits;
    private AdaptateurStock adaptateur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        Toolbar toolbar = findViewById(R.id.ibidandazwa_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recycler_ibidandazwa = findViewById(R.id.recycler_ibidandazwa);
        recycler_ibidandazwa.setLayoutManager(new GridLayoutManager(this, 1));
//        recycler_ibidandazwa.setLayoutManager(new FlexboxLayoutManager(this, FlexDirection.ROW, FlexWrap.WRAP));

        produits = new ArrayList<>();
        adaptateur = new AdaptateurStock(StockActivity.this, produits);
        recycler_ibidandazwa.addItemDecoration(new DividerItemDecoration(recycler_ibidandazwa.getContext(), DividerItemDecoration.VERTICAL));
        recycler_ibidandazwa.setAdapter(adaptateur);
        chargerStock();
    }
    private void chargerStock() {
        try {
            Dao dao_produits = new InkoranyaMakuru(this).getDao(Produit.class);
            produits = (ArrayList<Produit>) dao_produits.queryForAll();
            adaptateur.setData(produits);
        } catch (SQLException e) {
            Toast.makeText(this, "Erreur de connection à la base", Toast.LENGTH_LONG).show();
        }
    }

    public void startAddProduct(View view){
        view.startAnimation(AnimationUtils.loadAnimation(StockActivity.this, R.anim.button_fadein));
        final ProductForm product_form = new ProductForm(StockActivity.this);
        product_form.show();
        product_form.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(product_form.something_changed){
                    chargerStock();
                }
            }
        });
    }

    @Override
    public void refresh() {
        chargerStock();
    }
}
