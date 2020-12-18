package bi.konstrictor.urudandaza.dialogs;

import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import bi.konstrictor.urudandaza.ClotureActivity;
import bi.konstrictor.urudandaza.DetailHistActivity;
import bi.konstrictor.urudandaza.R;
import bi.konstrictor.urudandaza.models.Produit;

public class FilterClotureForm extends Dialog {

    private final ClotureActivity context;
    private Button btn_submit_filter, btn_cancel_filter;
    private DatePicker date_du, date_au;
    private AutoCompleteTextView spinner_product;

    public FilterClotureForm(final ClotureActivity context) {
        super(context, R.style.Theme_AppCompat_DayNight_Dialog);
        setContentView(R.layout.form_filter_clotures);
        this.context = context;

        date_du = findViewById(R.id.date_picker_du);
        date_au = findViewById(R.id.date_picker_au);
        spinner_product = findViewById(R.id.spinner_product);

        spinner_product.setAdapter(new ArrayAdapter(
                context,
                android.R.layout.simple_dropdown_item_1line,
                context.produits));

        btn_cancel_filter = findViewById(R.id.btn_cancel_filter);
        btn_submit_filter = findViewById(R.id.btn_submit_filter);

        btn_submit_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
        btn_cancel_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
    public void build(){
        show();
    }
    public void submit(){
        if(getSelectedProduit() == -1) return;
        Intent intent = new Intent(context, DetailHistActivity.class);
        HashMap<String, String> filters = new HashMap<>();
        filters.put("produit_id", getSelectedProduit().toString());
        intent.putExtra("filters", filters);
        ArrayList<Date> dates = new ArrayList<>();
        dates.add(getDateFromPickers(date_du));
        dates.add(getDateFromPickers(date_au));
        intent.putExtra("dates", dates);
        context.startActivityForResult(intent, context.CLOTURE_CODE);
        dismiss();
    }
    private Integer getSelectedProduit(){
        for (Produit p : context.produits){
            if (p.toString().equals(spinner_product.getText().toString())){
                return p.id;
            }
        }
        return -1;
    }
    private Date getDateFromPickers(DatePicker datePicker) {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();
        return new Date(year-1900, month, day);
    }
}
