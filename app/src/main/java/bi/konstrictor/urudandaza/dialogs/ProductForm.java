package bi.konstrictor.urudandaza.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import bi.konstrictor.urudandaza.R;

public class ProductForm extends Dialog {

    private TextView field_product_name, field_product_unite_in, field_product_unite_out, field_product_unit_rapport;
    private Button btn_product_cancel, btn_product_submit;

    public ProductForm(Context context) {
        super(context, R.style.Theme_AppCompat_DayNight_Dialog);
        setContentView(R.layout.form_product);

        field_product_name = findViewById(R.id.field_product_name);
        field_product_unite_in = findViewById(R.id.field_product_unite_in);
        field_product_unite_out = findViewById(R.id.field_product_unite_out);
        field_product_unit_rapport = findViewById(R.id.field_product_unit_rapport);
        btn_product_cancel = findViewById(R.id.btn_product_cancel);
        btn_product_submit = findViewById(R.id.btn_product_submit);
    }
    public void build(){
        show();
    }
}
