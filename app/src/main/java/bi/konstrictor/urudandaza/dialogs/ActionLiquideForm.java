package bi.konstrictor.urudandaza.dialogs;

import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.sql.SQLException;

import bi.konstrictor.urudandaza.InkoranyaMakuru;
import bi.konstrictor.urudandaza.R;
import bi.konstrictor.urudandaza.RefreshableActivity;
import bi.konstrictor.urudandaza.models.Liquide;
import bi.konstrictor.urudandaza.models.Personne;

public class ActionLiquideForm extends Dialog {

    private final RefreshableActivity context;
    private TextView field_liquide_somme, field_liquide_motif;
    private Button btn_liquide_cancel, btn_liquide_submit, btn_liquide_delete;
    private RadioGroup radio_group_action;
    private RadioButton radio_out, radio_in;
    private String[] arrcontact;
    private String liquide_somme, liquide_motif;

    private boolean edition = false;
    private Liquide liquide;

    public ActionLiquideForm(RefreshableActivity context) {
        super(context, R.style.Theme_AppCompat_DayNight_Dialog);
        setContentView(R.layout.form_liquide);
        this.context = context;

        field_liquide_somme = findViewById(R.id.field_liquide_somme);
        field_liquide_motif = findViewById(R.id.field_liquide_motif);
        radio_in = findViewById(R.id.radio_in);
        radio_out = findViewById(R.id.radio_out);
        radio_group_action = findViewById(R.id.radio_group_action);

        btn_liquide_cancel = findViewById(R.id.btn_liquide_cancel);
        btn_liquide_submit = findViewById(R.id.btn_liquide_submit);

        btn_liquide_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btn_liquide_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    public void setEdition(Liquide liquide) {
        this.edition = true;
        this.liquide = liquide;
        Double montant = Math.abs(liquide.montant);
        field_liquide_somme.setText(montant.toString());
        field_liquide_motif.setText(liquide.motif);
        if(liquide.montant<0){
            radio_out.setChecked(true);
        }else{
            radio_in.setChecked(true);
        }
    }

    private void submit() {
        if(validateFields()) {
            double somme = Double.parseDouble(liquide_somme);
            if(radio_out.isChecked()){
                somme = -somme;
            }
            InkoranyaMakuru inkoranyaMakuru = new InkoranyaMakuru(context);
            if (edition) {
                try {
                    UpdateBuilder<Liquide, Integer> update = inkoranyaMakuru.getDaoLiquide().updateBuilder();
                    update.where().eq("id", liquide.id);
                    update.updateColumnValue("montant" , field_liquide_somme.getText());
                    update.updateColumnValue("motif" , field_liquide_motif.getText());
                    update.update();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    Dao dao_liquide = inkoranyaMakuru.getDaoLiquide();
                    dao_liquide.create(new Liquide(somme, liquide_motif));
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
        liquide_somme = field_liquide_somme.getText().toString().trim();
        liquide_motif = field_liquide_motif.getText().toString().trim();
        if(liquide_somme.isEmpty()) {
            field_liquide_somme.setError("banza mwuzuze aha");
            return false;
        }
        if(liquide_motif.isEmpty()) {
            field_liquide_motif.setError("banza mwuzuze aha");
            return false;
        }
        return true;
    }

    public void build(){
        show();
    }
}
