package bi.konstrictor.urudandaza.dialogs;

import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

import bi.konstrictor.urudandaza.R;
import bi.konstrictor.urudandaza.interfaces.Filterable;

public class FilterActionForm extends Dialog {

    private final Button btn_submit_filter, btn_cancel_filter;
    private final CheckBox check_dette, check_perimee;
    private final Filterable parent;

    public FilterActionForm(final AppCompatActivity context, final Filterable fragment) {
        super(context, R.style.Theme_AppCompat_DayNight_Dialog);
        setContentView(R.layout.form_filter);
        this.parent = fragment;

        check_dette = findViewById(R.id.check_dette);
        check_perimee = findViewById(R.id.check_perimee);

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
                parent.cancelFiltering();
                dismiss();
            }
        });
    }
    public void build(){
        show();
    }
    public void submit(){
        parent.performFiltering(
                check_dette.isChecked(),
                check_perimee.isChecked()
        );
        dismiss();
    }
}
