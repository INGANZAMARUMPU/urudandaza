package bi.konstrictor.urudandaza;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;

import bi.konstrictor.urudandaza.adapters.AdaptateurVente;
import bi.konstrictor.urudandaza.adapters.AdaptateurVente2;
import bi.konstrictor.urudandaza.models.ActionStock;
import bi.konstrictor.urudandaza.models.Produit;

import static android.util.TypedValue.COMPLEX_UNIT_PT;
import static android.util.TypedValue.COMPLEX_UNIT_SP;

public class VenteActivity extends AppCompatActivity {

    private RecyclerView recycler_ibidandazwa;
    private AdaptateurVente2 adaptateur;
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

//        recycler_ibidandazwa.setLayoutManager(new GridLayoutManager(this, 2));
        FlexboxLayoutManager layout = new FlexboxLayoutManager(this, FlexDirection.ROW, FlexWrap.WRAP);
//        layout.setJustifyContent(JustifyContent.CENTER);
        recycler_ibidandazwa.setLayoutManager(layout);
        produits = new ArrayList<>();
        adaptateur = new AdaptateurVente2(VenteActivity.this, produits);
//        recycler_ibidandazwa.addItemDecoration(new DividerItemDecoration(recycler_ibidandazwa.getContext(), DividerItemDecoration.VERTICAL));
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
            setMONTANT(0.);
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

    public void vendre(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_fadein));
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Kugurisha");

        ScrollView sv = new ScrollView(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        TextView label = new TextView(this);
        String confirmation = "Mugomba mugurishe: \n";
        Log.i("===== VENTE =====", "For Loop");
        for(ActionStock as:CART){
            confirmation += "\n\t- "+as.toString();
        }
        label.setText(confirmation);
        label.setTextSize(COMPLEX_UNIT_PT, 7);
        layout.addView(label);
        sv.addView(layout);
        builder.setView(sv);

        builder.setPositiveButton("Sawa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(VenteActivity.this, "Vyaguzwe", Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("Reka", null);
        builder.show();
    }
}
