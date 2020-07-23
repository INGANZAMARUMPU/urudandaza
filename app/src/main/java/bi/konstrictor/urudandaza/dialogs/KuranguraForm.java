package bi.konstrictor.urudandaza.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import bi.konstrictor.urudandaza.InkoranyaMakuru;
import bi.konstrictor.urudandaza.R;
import bi.konstrictor.urudandaza.models.ActionStock;
import bi.konstrictor.urudandaza.models.Produit;

public class KuranguraForm extends Dialog {

    Context context;
    private TextView lbl_kurangura_product;
    private EditText champ_kurangura_quantite;
    private ProgressBar progress_kurangura;
    private Button btn_moins_10, btn_moins_1, btn_plus_10, btn_plus_1;
    public Boolean something_changed = false;

    public KuranguraForm(Context context) {
        super(context, R.style.Theme_AppCompat_DayNight_Dialog);
        setContentView(R.layout.form_product);
        this.context = context;

        lbl_kurangura_product = findViewById(R.id.lbl_kurangura_product);
        champ_kurangura_quantite = findViewById(R.id.champ_kurangura_quantite);
        progress_kurangura = findViewById(R.id.progress_kurangura);
        btn_moins_10 = findViewById(R.id.btn_moins_10);
        btn_moins_1 = findViewById(R.id.btn_moins_1);
        btn_plus_10 = findViewById(R.id.btn_plus_10);
        btn_plus_1 = findViewById(R.id.btn_plus_1);

        btn_moins_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double quantite = Double.parseDouble(champ_kurangura_quantite.getText().toString())-10;
                champ_kurangura_quantite.setText(quantite.toString());
            }
        });
        btn_moins_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double quantite = Double.parseDouble(champ_kurangura_quantite.getText().toString())-1;
                champ_kurangura_quantite.setText(quantite.toString());
            }
        });
        btn_plus_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double quantite = Double.parseDouble(champ_kurangura_quantite.getText().toString())+10;
                champ_kurangura_quantite.setText(quantite.toString());
            }
        });
        btn_plus_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double quantite = Double.parseDouble(champ_kurangura_quantite.getText().toString())+1;
                champ_kurangura_quantite.setText(quantite.toString());
            }
        });

    }

    public void build(){
        show();
    }
    public void submit(){
        progress_kurangura.setVisibility(View.VISIBLE);


        something_changed = true;
        progress_kurangura.setVisibility(View.GONE);
    }
}
