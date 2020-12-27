package bi.konstrictor.urudandaza;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;

import bi.konstrictor.urudandaza.adapters.AdaptateurVente;
import bi.konstrictor.urudandaza.dialogs.ConfirmKudandaza;
import bi.konstrictor.urudandaza.interfaces.RefreshableActivity;
import bi.konstrictor.urudandaza.models.ActionStock;
import bi.konstrictor.urudandaza.models.Produit;

public class VenteActivity extends RefreshableActivity {

    private RecyclerView recycler_ibidandazwa;
    private AdaptateurVente adaptateur;
    private Button btn_vendre;
    private TextView lbl_vente_total;
    private Menu menu;
    private SearchView searchView;

    private ArrayList<Produit> produits;
    private ArrayList<ActionStock> CART;
    private Boolean INTEGER_MODE = true;

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

        FlexboxLayoutManager layout = new FlexboxLayoutManager(this, FlexDirection.ROW, FlexWrap.WRAP);
        layout.setJustifyContent(JustifyContent.SPACE_EVENLY);
        recycler_ibidandazwa.setLayoutManager(layout);
        produits = new ArrayList<>();
        adaptateur = new AdaptateurVente(VenteActivity.this, produits);
        recycler_ibidandazwa.setAdapter(adaptateur);

        CART = new ArrayList<>();
        chargerStock();
    }

    private void chargerStock() {
        try {
            Dao dao_stocks = new InkoranyaMakuru(this).getDao(Produit.class);
            produits = (ArrayList<Produit>) dao_stocks.queryBuilder().orderByRaw("nom COLLATE NOCASE ASC").query();
            adaptateur.setData(produits);
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
                adaptateur.setData(produits);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<Produit> result = new ArrayList<>();
                for (Produit p : produits){
                    if(p.nom.toLowerCase().contains(s.toLowerCase())){
                        result.add(p);
                    }
                }
                adaptateur.setData(result);
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                adaptateur.setData(produits);
                return false;
            }
        });
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_number_format) {
            setINTEGER_MODE(!this.isINTEGER_MODE());
        }else if(id == R.id.action_annuler_vente){
            refresh();
        }
        return super.onOptionsItemSelected(item);
    }
    public Boolean isINTEGER_MODE() {
        return INTEGER_MODE;
    }

    public void setINTEGER_MODE(Boolean INTEGER_MODE) {
        MenuItem action_number_format = menu.findItem( R.id.action_number_format);
        this.INTEGER_MODE = INTEGER_MODE;
        if(INTEGER_MODE){
            action_number_format.setIcon(R.drawable.ic_float);
            action_number_format.setTitle("Ibigaburika");
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }else{
            action_number_format.setIcon(R.drawable.ic_int);
            action_number_format.setTitle("Ibitagaburika");
        }
        chargerStock();
    }
    public ActionStock getCartItem(Produit produit){
        for (int i=0; i<CART.size(); i++){
            if(produit.id == CART.get(i).produit.id) return CART.get(i);
        }
        return null;
    }
    public void addToCart(ActionStock stock){
        ActionStock old = getCartItem(stock.produit);
        if (stock.getQuantite()>0) {
            if(old==null){
                CART.add(stock);
                setMONTANT(MONTANT + stock.getTotal());
            }else{
                setMONTANT(MONTANT - old.getTotal() + stock.getTotal());
                old.setQuantite(stock.getQuantite());
            }
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
                setMONTANT(MONTANT - as.getTotal());
                return;
            }
        }
    }

    public void vendre(View view) {
        if(MONTANT>0) {
            ConfirmKudandaza kurangura_form = new ConfirmKudandaza(this, CART, MONTANT);
            kurangura_form.show();
        }
    }
    @Override
    public void refresh() {
        CART = new ArrayList<>();
        setMONTANT(0.);
        chargerStock();
    }
}
