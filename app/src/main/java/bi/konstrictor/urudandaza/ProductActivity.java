package bi.konstrictor.urudandaza;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;

import bi.konstrictor.urudandaza.dialogs.ProductForm;
import bi.konstrictor.urudandaza.models.Produit;

public class ProductActivity extends AppCompatActivity {

    RecyclerView recycler_ibidandazwa;
    ArrayList<Produit> produits;
    private AdaptateurProduit adaptateur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        Toolbar toolbar = findViewById(R.id.ibidandazwa_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recycler_ibidandazwa = findViewById(R.id.recycler_ibidandazwa);
        recycler_ibidandazwa.setLayoutManager(new GridLayoutManager(this, 1));

        produits = new ArrayList<>();
        adaptateur = new AdaptateurProduit(ProductActivity.this, produits);
        recycler_ibidandazwa.addItemDecoration(new DividerItemDecoration(recycler_ibidandazwa.getContext(), DividerItemDecoration.VERTICAL));
        recycler_ibidandazwa.setAdapter(adaptateur);
        chargerProduits();
    }

    private void chargerProduits() {
        try {
            Dao dao_produits = new InkoranyaMakuru(this).getDaoProduit();
            produits = (ArrayList<Produit>) dao_produits.queryForAll();
//            produits.addAll(produits);
            adaptateur.setData(produits);
            adaptateur.notifyDataSetChanged();
        } catch (SQLException e) {
            Toast.makeText(this, "Erreur de connection à la base", Toast.LENGTH_LONG).show();
        }
    }

    public void startAddProduct(View view){
        view.startAnimation(AnimationUtils.loadAnimation(ProductActivity.this, R.anim.button_fadein));
        final ProductForm product_form = new ProductForm(ProductActivity.this);
        product_form.show();
        product_form.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(product_form.something_changed){
                    chargerProduits();
                }
            }
        });
    }

}
