package bi.konstrictor.urudandaza.dialogs;

import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import bi.konstrictor.urudandaza.InkoranyaMakuru;
import bi.konstrictor.urudandaza.R;
import bi.konstrictor.urudandaza.StockActivity;
import bi.konstrictor.urudandaza.models.Produit;

public class ProductForm extends Dialog {

    StockActivity context;
    private TextView field_product_name, field_product_unite_in, field_product_unite_out,
            field_product_unit_rapport;
    private ProgressBar progress_product;
    private String product_name, product_unite_in, product_unite_out, product_unit_rapport;
    private Button btn_product_cancel, btn_product_submit, btn_product_delete;
    private Boolean edition = false;
    public Boolean something_changed = false;
    private Produit produit;

    public ProductForm(StockActivity context) {
        super(context, R.style.Theme_AppCompat_DayNight_Dialog);
        setContentView(R.layout.form_product);
        this.context = context;

        field_product_name = findViewById(R.id.field_product_name);
        field_product_unite_in = findViewById(R.id.field_product_unite_in);
        field_product_unite_out = findViewById(R.id.field_product_unite_out);
        field_product_unit_rapport = findViewById(R.id.field_product_unit_rapport);
        progress_product = findViewById(R.id.progress_product);
        btn_product_cancel = findViewById(R.id.btn_product_cancel);
        btn_product_submit = findViewById(R.id.btn_product_submit);
        btn_product_delete = findViewById(R.id.btn_product_delete);
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
    }
    public void setEdition(Produit produit) {
        this.edition = true;
        btn_product_delete.setVisibility(View.VISIBLE);
        this.produit = produit;

        field_product_name.setText(produit.nom);
        field_product_unite_in.setText(produit.unite_entrant);
        field_product_unite_out.setText(produit.unite_sortant);
        field_product_unit_rapport.setText(produit.rapport.toString());
    }
    public void build(){
        show();
    }
    public void submit(){
        progress_product.setVisibility(View.VISIBLE);
        if(validateFields()) {
            InkoranyaMakuru inkoranyaMakuru = new InkoranyaMakuru(context);
            if (edition) {
                produit.nom = field_product_name.getText().toString();
                produit.unite_entrant = field_product_unite_in.getText().toString();
                produit.unite_sortant = field_product_unite_out.getText().toString();
                produit.rapport = Double.parseDouble(
                    field_product_unit_rapport.getText().toString());
                produit.update(context);
                dismiss();
                context.refresh();
            } else {
                double rapport = Double.parseDouble(product_unit_rapport);
                Produit produit = new Produit(product_name, product_unite_in, product_unite_out, rapport, 0.);
                produit.create(context);
                Toast.makeText(context, "Vyagenze neza", Toast.LENGTH_LONG).show();
                dismiss();
            }
            something_changed = true;
        }
        progress_product.setVisibility(View.GONE);
    }
    private Boolean validateFields() {
        product_name = field_product_name.getText().toString().trim();
        product_unite_in = field_product_unite_in.getText().toString().trim();
        product_unite_out = field_product_unite_out.getText().toString().trim();
        product_unit_rapport = field_product_unit_rapport.getText().toString().trim();
        if(product_name.isEmpty()){
            field_product_name.setError("uzuza ngaha");
            return false;
        }
        if(product_unite_in.isEmpty()){
            field_product_unite_in.setError("uzuza ngaha");
            return false;
        }
        if(product_unite_out.isEmpty()){
            product_unite_out = product_unite_in;
        }
        if(product_unit_rapport.isEmpty()){
            product_unit_rapport = "1";
        }
        return true;
    }
}
