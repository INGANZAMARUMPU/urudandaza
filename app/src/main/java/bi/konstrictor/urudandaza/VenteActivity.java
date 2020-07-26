package bi.konstrictor.urudandaza;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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
    private AdaptateurVente adaptateur;
    private Button btn_vendre;
    private TextView lbl_vente_total;
    private Menu menu;
    private SearchView searchView;

    private ArrayList<Produit> produits;
    private ArrayList<ActionStock> CART;
    private Boolean INTEGER_MODE = false;

    private Double MONTANT = 0.;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vente);
        Toolbar toolbar = findViewById(R.id.vente_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recycler_ibidandazwa = findViewById(R.id.recycler_vente);
        btn_vendre = findViewById(R.id.btn_vendre);
        lbl_vente_total = findViewById(R.id.lbl_vente_total);

        recycler_ibidandazwa.setLayoutManager(new GridLayoutManager(this, 1));
//        recycler_ibidandazwa.setLayoutManager(new FlexboxLayoutManager(this, FlexDirection.ROW, FlexWrap.WRAP));
        produits = new ArrayList<>();
        adaptateur = new AdaptateurVente(VenteActivity.this, produits);
        recycler_ibidandazwa.addItemDecoration(new DividerItemDecoration(recycler_ibidandazwa.getContext(), DividerItemDecoration.VERTICAL));
        recycler_ibidandazwa.setAdapter(adaptateur);

        CART = new ArrayList<>();
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
    @Override
    public boolean onCreateOptionsMenu( Menu menu) {
        getMenuInflater().inflate( R.menu.vente_menu, menu);
        this.menu = menu;

        final MenuItem action_search = menu.findItem( R.id.action_search);
        searchView = (SearchView) action_search.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(VenteActivity.this, "ubushakashatsi...", Toast.LENGTH_SHORT).show();
                if( ! searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                action_search.collapseActionView();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_number_format) {
            if(INTEGER_MODE){
                setINTEGER_MODE(false);
            }else{
                setINTEGER_MODE(true);
            }
        }else if(id == R.id.action_annuler_vente){
            CART = new ArrayList<>();
            chargerStock();
            adaptateur.notifyDataSetChanged();
        }
        return super.onOptionsItemSelected(item);
    }
    public Boolean getINTEGER_MODE() {
        return INTEGER_MODE;
    }

    public void setINTEGER_MODE(Boolean INTEGER_MODE) {
        MenuItem action_number_format = menu.findItem( R.id.action_number_format);
        this.INTEGER_MODE = INTEGER_MODE;
        if(INTEGER_MODE){
            action_number_format.setIcon(R.drawable.ic_float);
            action_number_format.setTitle("Ibigaburika");
        }else{
            action_number_format.setIcon(R.drawable.ic_float);
            action_number_format.setTitle("Ibitagaburika");
        }
        adaptateur.notifyDataSetChanged();
    }
    public int indexInCart(ActionStock stock){
        for (int i=0; i<CART.size(); i++){
            if(stock.produit.id == CART.get(i).produit.id) return i;
        }
        return -1;
    }
    public void addToCart(ActionStock stock){
        if(stock.quantite<1){
            removeFromCart(stock.produit);
            return;
        };
        Integer index = indexInCart(stock);
        if(index<0){
            CART.add(stock);
            setMONTANT(MONTANT + stock.prix*stock.quantite);
        }else{
            ActionStock old = CART.get(index);
            setMONTANT(MONTANT - old.prix*old.quantite + stock.prix*stock.quantite);
            old.quantite = stock.quantite;
        }
    }

    public void setMONTANT(Double MONTANT) {
        this.MONTANT = MONTANT;
        lbl_vente_total.setText(this.MONTANT.toString());
    }

    public void removeFromCart(Produit produit){
        for(ActionStock as : CART){
            if(produit.id == as.produit.id){
                CART.remove(as);
                setMONTANT(MONTANT - as.prix*as.quantite);
                return;
            }
        }
    }
}
