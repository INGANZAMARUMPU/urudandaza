package bi.konstrictor.urudandaza.dialogs;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bi.konstrictor.urudandaza.InkoranyaMakuru;
import bi.konstrictor.urudandaza.R;
import bi.konstrictor.urudandaza.RefreshableActivity;
import bi.konstrictor.urudandaza.VenteActivity;
import bi.konstrictor.urudandaza.models.ActionStock;
import bi.konstrictor.urudandaza.models.Personne;
import bi.konstrictor.urudandaza.models.Produit;

import static android.Manifest.permission.READ_CONTACTS;

public class VenteForm extends Dialog {
    private VenteActivity context;
    private TextView lbl_vente_list;
    private AutoCompleteTextView field_vente_client;
    private EditText field_vente_payee;
    private ProgressBar progress_vente;
    private String[] arrcontact;
    final int PRIX=10, TOTAL=20;
    private ArrayList<ActionStock> CART;
    private boolean edition;
    private double payee;
    private String client;

    public VenteForm(final VenteActivity context, ArrayList CART) {
        super(context, R.style.Theme_AppCompat_DayNight_Dialog);
        setContentView(R.layout.form_vente);
        this.context = context;
        this.CART = CART;

        lbl_vente_list = findViewById(R.id.lbl_vente_list);
        field_vente_client = findViewById(R.id.field_vente_client);
        progress_vente = findViewById(R.id.progress_vente);
        field_vente_payee = findViewById(R.id.field_vente_payee);

        Button btn_vente_submit = findViewById(R.id.btn_vente_submit);
        Button btn_vente_cancel = findViewById(R.id.btn_vente_cancel);
        Button btn_reset_payee = findViewById(R.id.btn_reset_payee);

        btn_vente_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btn_vente_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
        btn_reset_payee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { field_vente_payee.setText("0"); }
        });

        init();
    }

    private void init() {
        field_vente_client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress_vente.setVisibility(View.VISIBLE);
                loadClient();
                field_vente_client.setAdapter(new ArrayAdapter<String>(context,
                        android.R.layout.simple_dropdown_item_1line, arrcontact));
                progress_vente.setVisibility(View.GONE);
            }
        });
        String confirmation = "";
        for(ActionStock as:CART){
            confirmation += as.toString()+"\n";
        }
        lbl_vente_list.setText(confirmation);
        field_vente_payee.setText(context.getMONTANT().toString());
        field_vente_payee.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()<1) field_vente_payee.setText("0");
            }
        });
    }

    private void loadClient() {
        try {
            Dao dao_clients = new InkoranyaMakuru(context).getDaoPersonne();
            List<Personne> personnes = dao_clients.queryForAll();
            arrcontact = new String[personnes.size()];
            for (int i=0; i<personnes.size(); i++){
                arrcontact[i] = personnes.get(i).nom;
            }
        } catch (SQLException e) {
            Toast.makeText(context, "Erreur de lecture des contacts",Toast.LENGTH_LONG).show();
        }
    }
    public void build(){ show(); }
    public void submit(){
        if(validateFields()) {
            progress_vente.setVisibility(View.VISIBLE);
            Personne personne = Personne.getClient(client, context);
            Log.i("===== PERSONNE ===== ", personne.toString());
            for (ActionStock as : CART){
                as.personne = personne;
                Double a_payer = as.produit.prix*as.quantite;
                if (payee>=a_payer) {
                    as.payee = a_payer;
                    payee -= a_payer;
                } else {
                    as.payee = payee;
                    payee = 0;
                }
                try {
                    Dao dao_action = new InkoranyaMakuru(context).getDaoActionStock();
                    as.quantite = -as.quantite;
                    Log.i("===== AS ===== ", as.toString());
                    dao_action.create(as);
                } catch (SQLException e) {
                    Log.i("===== ERREUR ==== ", e.getMessage());
                    e.printStackTrace();
                    Toast.makeText(context, "Hari ikintu kutagenze neza", Toast.LENGTH_LONG).show();
                    break;
                }
            }
            progress_vente.setVisibility(View.GONE);
            dismiss();
            context.refresh();
            Toast.makeText(context, "Vyaguzwe", Toast.LENGTH_LONG).show();
        }
    }
    private Boolean validateFields() {
        payee = Double.parseDouble(field_vente_payee.getText().toString());
        client = field_vente_client.getText().toString().trim();
        if(payee<context.getMONTANT()){
            if(client.isEmpty()) {
                field_vente_client.setError("ko atarishe yose uzuza izina");
                return false;
            }
        }
        return true;
    }
}
