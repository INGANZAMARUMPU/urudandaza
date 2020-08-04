package bi.konstrictor.urudandaza.dialogs;

import android.app.Dialog;
import android.app.Notification;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import bi.konstrictor.urudandaza.InkoranyaMakuru;
import bi.konstrictor.urudandaza.R;
import bi.konstrictor.urudandaza.RefreshableActivity;
import bi.konstrictor.urudandaza.models.Depense;
import bi.konstrictor.urudandaza.models.Produit;

public class DepensesForm extends Dialog {

    private final RefreshableActivity context;
    private TextView field_depense_somme, field_depense_motif;
    private Button btn_depense_cancel, btn_depense_submit, btn_depense_delete;
    private String[] arrcontact;
    private String depense_somme, depense_motif;
    private boolean edition = false;

    public DepensesForm(RefreshableActivity context) {
        super(context, R.style.Theme_AppCompat_DayNight_Dialog);
        setContentView(R.layout.form_depense);
        this.context = context;

        field_depense_somme = findViewById(R.id.field_depense_somme);
        field_depense_motif = findViewById(R.id.field_depense_motif);

        btn_depense_cancel = findViewById(R.id.btn_depense_cancel);
        btn_depense_submit = findViewById(R.id.btn_depense_submit);

        btn_depense_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btn_depense_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    private void submit() {
        if(validateFields()) {
            if (edition) {

            } else {
                double somme = Double.parseDouble(depense_somme);
                InkoranyaMakuru inkoranyaMakuru = new InkoranyaMakuru(context);
                try {
                    Dao dao_depense = inkoranyaMakuru.getDaoDepense();
                    dao_depense.create(new Depense(somme, depense_motif));
                    Toast.makeText(context, "Vyagenze neza", Toast.LENGTH_LONG).show();
                    context.refresh();
                    dismiss();
                } catch (SQLException e) {
                    Log.i("ERREUR", e.getMessage());
                    e.printStackTrace();
                    Toast.makeText(context, "Hari ikintu kutagenze neza", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private boolean validateFields() {
        depense_somme = field_depense_somme.getText().toString().trim();
        depense_motif = field_depense_motif.getText().toString().trim();
        if(depense_somme.isEmpty()) {
            field_depense_somme.setError("ko atarishe yose uzuza izina");
            return false;
        }
        if(depense_motif.isEmpty()) {
            field_depense_motif.setError("ko atarishe yose uzuza izina");
            return false;
        }
        return true;
    }

    public void build(){
        show();
    }
}
