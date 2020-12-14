package bi.konstrictor.urudandaza.dialogs;

import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
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
    private Spinner spinner_product;

    public FilterClotureForm(final ClotureActivity context) {
        super(context, R.style.Theme_AppCompat_DayNight_Dialog);
        setContentView(R.layout.form_filter_clotures);
        this.context = context;

        date_du = findViewById(R.id.date_picker_du);
        date_au = findViewById(R.id.date_picker_au);
        spinner_product = findViewById(R.id.spinner_product);

        spinner_product.setAdapter(new ArrayAdapter(
                context,
                R.layout.support_simple_spinner_dropdown_item,
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
        Intent intent = new Intent(context, DetailHistActivity.class);
        HashMap<String, String> filters = new HashMap<>();
        filters.put("produit_id", ((Produit) spinner_product.getSelectedItem()).id.toString());
        intent.putExtra("filters", filters);
        ArrayList<Date> dates = new ArrayList<>();
        dates.add(getDateFromPickers(date_du));
        dates.add(getDateFromPickers(date_au));
        intent.putExtra("dates", dates);
        context.startActivityForResult(intent, context.CLOTURE_CODE);
        dismiss();
    }

    private Date getDateFromPickers(DatePicker datePicker) {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();
        return new Date(year-1900, month, day);
    }
}
