package bi.konstrictor.urudandaza.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import bi.konstrictor.urudandaza.InkoranyaMakuru;
import bi.konstrictor.urudandaza.R;
import bi.konstrictor.urudandaza.models.ActionStock;
import bi.konstrictor.urudandaza.models.Produit;

public class ProductForm extends Dialog {

    Context context;
    private TextView field_product_name, field_product_unite_in, field_product_unite_out,
            field_product_unit_rapport, field_product_quantite, lbl_unite_mesure,field_product_prix;
    private ProgressBar progress_product;
    private String product_name, product_unite_in, product_unite_out, product_unit_rapport,
            product_quantite, product_prix;
    private Button btn_product_cancel, btn_product_submit;
    public Boolean edition = false;
    public Boolean something_changed = false;

    public ProductForm(Context context) {
        super(context, R.style.Theme_AppCompat_DayNight_Dialog);
        setContentView(R.layout.form_product);
        this.context = context;

        field_product_name = findViewById(R.id.field_product_name);
        field_product_unite_in = findViewById(R.id.field_product_unite_in);
        field_product_unite_out = findViewById(R.id.field_product_unite_out);
        field_product_unit_rapport = findViewById(R.id.field_product_unit_rapport);
        field_product_quantite = findViewById(R.id.field_product_quantite);
        field_product_prix = findViewById(R.id.field_product_prix);
        lbl_unite_mesure = findViewById(R.id.lbl_unite_mesure);
        progress_product = findViewById(R.id.progress_product);
        btn_product_cancel = findViewById(R.id.btn_product_cancel);
        btn_product_submit = findViewById(R.id.btn_product_submit);
        btn_product_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
        btn_product_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        field_product_unite_in.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                lbl_unite_mesure.setText(field_product_unite_in.getText());
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
    }
    public void setEdition(Boolean edition) {
        this.edition = edition;
    }
    public void build(){
        show();
    }
    public void submit(){
        progress_product.setVisibility(View.VISIBLE);
        if(validateFields()) {
            if (edition) {

            } else {
                double rapport = Double.parseDouble(product_unit_rapport);
                double prix = Double.parseDouble(product_prix);
                Produit produit = new Produit(product_name, product_unite_in, product_unite_out, rapport, prix);
                ActionStock action = new ActionStock(produit, Double.parseDouble(product_quantite));
                try {
                    Dao dao_action = new InkoranyaMakuru(context).getDaoActionStock();
                    dao_action.create(action);
                    Toast.makeText(context, "Vyagenze neza", Toast.LENGTH_LONG).show();
                    dismiss();
                } catch (SQLException e) {
                    Log.i("ERREUR", e.getMessage());
                    e.printStackTrace();
                    Toast.makeText(context, "Hari ikintu kutagenze neza", Toast.LENGTH_LONG).show();
                }
            }
            something_changed = true;
        }
//        Intent intent = new Intent(context, context.getClass());
//        Activity activity = (Activity) context;
//        activity.finish();
//        context.startActivity(intent);
        progress_product.setVisibility(View.GONE);
    }
    private Boolean validateFields() {
        product_name = field_product_name.getText().toString().trim();
        product_unite_in = field_product_unite_in.getText().toString().trim();
        product_unite_out = field_product_unite_out.getText().toString().trim();
        product_unit_rapport = field_product_unit_rapport.getText().toString().trim();
        product_quantite = field_product_quantite.getText().toString().trim();
        product_prix = field_product_prix.getText().toString().trim();
        if(product_name.isEmpty()){
            field_product_name.setError("uzuza ngaha");
            return false;
        }
        if(product_unite_in.isEmpty()){
            field_product_unite_in.setError("uzuza ngaha");
            return false;
        }
        if(product_prix.isEmpty()){
            field_product_prix.setError("uzuza ngaha");
            return false;
        }
        if(product_unite_out.isEmpty()){
            product_unite_out = product_unite_in;
        }
        if(product_unit_rapport.isEmpty()){
            product_unit_rapport = "1";
        }
        if(product_quantite.isEmpty()){
            product_quantite = "0";
        }
        return true;
    }
}
