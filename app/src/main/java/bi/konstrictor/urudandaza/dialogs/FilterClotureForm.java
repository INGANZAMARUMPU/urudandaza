package bi.konstrictor.urudandaza.dialogs;

import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;

import bi.konstrictor.urudandaza.R;
import bi.konstrictor.urudandaza.interfaces.Filterable;

public class FilterClotureForm extends Dialog {

    private final Button btn_submit_filter, btn_cancel_filter;
    private final DatePicker date_du, date_au;

    public FilterClotureForm(final AppCompatActivity context) {
        super(context, R.style.Theme_AppCompat_DayNight_Dialog);
        setContentView(R.layout.form_clotures);
        date_du = findViewById(R.id.date_picker_du);
        date_au = findViewById(R.id.date_picker_au);

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
        dismiss();
    }
}
