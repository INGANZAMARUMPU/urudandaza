package bi.konstrictor.urudandaza.dialogs;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
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

import java.sql.SQLException;
import java.util.List;

import bi.konstrictor.urudandaza.InkoranyaMakuru;
import bi.konstrictor.urudandaza.R;
import bi.konstrictor.urudandaza.RefreshableActivity;
import bi.konstrictor.urudandaza.models.ActionStock;
import bi.konstrictor.urudandaza.models.Personne;
import bi.konstrictor.urudandaza.models.Produit;

import static android.Manifest.permission.READ_CONTACTS;

public class KuranguraForm extends Dialog {
    private RefreshableActivity context;
    private TextView lbl_kurangura_product, field_kurangura_prix, field_kurangura_total,
            field_kurangura_qtt, field_kurangura_payee, lbl_kurangura_unite;
    private AutoCompleteTextView field_kurangura_personne;
    private String kurangura_prix, kurangura_qtt, client, kurangura_payee;
    private ProgressBar progress_kurangura;
    public Boolean something_changed = false;
    private String[] arrcontact;
    final int PRIX=10, TOTAL=20;
    private double payee, a_payer;
    private Produit produit;
    private boolean edition = false;
    private boolean ideni = false;

    public KuranguraForm(final RefreshableActivity context, Produit produit) {
        super(context, R.style.Theme_AppCompat_DayNight_Dialog);
        setContentView(R.layout.form_kurangura);
        this.context = context;
        this.produit = produit;

        lbl_kurangura_product = findViewById(R.id.lbl_kurangura_product);
        field_kurangura_qtt = findViewById(R.id.field_kurangura_qtt);
        field_kurangura_prix = findViewById(R.id.field_kurangura_prix);
        field_kurangura_total = findViewById(R.id.field_kurangura_total);
        field_kurangura_payee = findViewById(R.id.field_kurangura_payee);
        field_kurangura_personne = findViewById(R.id.field_kurangura_personne);
        lbl_kurangura_unite = findViewById(R.id.lbl_kurangura_unite);
        progress_kurangura = findViewById(R.id.progress_kurangura);

        Button btn_kurangura_submit = findViewById(R.id.btn_kurangura_submit);
        Button btn_kurangura_cancel = findViewById(R.id.btn_kurangura_cancel);
        Button btn_reset_payee = findViewById(R.id.btn_reset_payee);

        lbl_kurangura_product.setText(produit.nom);
        lbl_kurangura_unite.setText(produit.unite_entrant);
        btn_kurangura_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btn_kurangura_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
        btn_reset_payee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { field_kurangura_payee.setText("0"); }
        });
        init();
    }

    private void init() {
        final TextWatcher calculer_prix = calculer(PRIX);
        final TextWatcher calculer_total = calculer(TOTAL);

        field_kurangura_prix.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    field_kurangura_prix.addTextChangedListener(calculer_total);
                }else{
                    field_kurangura_prix.removeTextChangedListener(calculer_total);
                }
            }
        });
        field_kurangura_total.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    field_kurangura_total.addTextChangedListener(calculer_prix);
                }else{
                    field_kurangura_total.removeTextChangedListener(calculer_prix);
                }
            }
        });
        field_kurangura_personne.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    progress_kurangura.setVisibility(View.VISIBLE);
                    loadClient();
                    field_kurangura_personne.setAdapter(new ArrayAdapter<String>(context,
                            android.R.layout.simple_dropdown_item_1line, arrcontact));
                    progress_kurangura.setVisibility(View.GONE);
                }
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
    private Double getChampKuranguraQuantite(){
        Double quantite = 0.;
        try {
            quantite = Double.parseDouble(field_kurangura_qtt.getText().toString());
        }catch (Exception e){
        }
        return quantite;
    }
    private TextWatcher calculer(final int type) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0){
                    Double quantity = getChampKuranguraQuantite();
                    Double value = Double.parseDouble(s.toString());
                    Double total;
                    if(type==TOTAL) {
                        total = value*quantity;
                        field_kurangura_total.setText(total.toString());
                        field_kurangura_payee.setText(total.toString());
                    }else {
                        if(quantity!=0) {
                            total = value/quantity;
                        }else{
                            total = 0.;
                        }
                        field_kurangura_payee.setText(value.toString());
                        field_kurangura_prix.setText(total.toString());
                    }
                }
            }
        };
    }

    public void build(){ show(); }
    public void submit(){
        if(validateFields()) {
            progress_kurangura.setVisibility(View.VISIBLE);
            if (edition) {

            } else {
                double qtt = Double.parseDouble(kurangura_qtt);
                double prix = Double.parseDouble(kurangura_prix);
                InkoranyaMakuru inkoranyaMakuru = new InkoranyaMakuru(context);
                try {
                    ActionStock action = new ActionStock();
                    if(ideni){
                        Personne personne = Personne.getClient(client, context);
                        if (personne==null){
                            ClientForm form = new ClientForm(context);
                            form.setPARENT_NAME_FIELD(field_kurangura_personne);
                            form.show();
                            progress_kurangura.setVisibility(View.GONE);
                            return;
                        }
                        action.ideniKurangura(produit, qtt, prix, personne, payee, null, inkoranyaMakuru.getLatestCloture());
                    }else {
                        action.kurangura(produit, qtt, prix, payee, inkoranyaMakuru.getLatestCloture());
                    }
                    Dao dao_action = inkoranyaMakuru.getDaoActionStock();
                    dao_action.create(action);
                    Toast.makeText(context, "Vyagenze neza", Toast.LENGTH_LONG).show();
                    dismiss();
                } catch (SQLException e) {
                    Log.i("ERREUR", e.getMessage());
                    e.printStackTrace();
                    Toast.makeText(context, "Hari ikintu kutagenze neza", Toast.LENGTH_LONG).show();
                }
            }
            context.refresh();
            progress_kurangura.setVisibility(View.GONE);
        }
    }
    private Boolean validateFields() {
        kurangura_qtt = field_kurangura_qtt.getText().toString().trim();
        kurangura_prix = field_kurangura_prix.getText().toString().trim();
        kurangura_payee = field_kurangura_payee.getText().toString().trim();
        client = field_kurangura_personne.getText().toString().trim();
        if(kurangura_prix.isEmpty()){
            field_kurangura_prix.setError("uzuza ngaha");
            return false;
        }
        if(kurangura_qtt.isEmpty()){
            field_kurangura_qtt.setError("uzuza ngaha");
            return false;
        }
        if(kurangura_payee.isEmpty()){
            field_kurangura_payee.setError("uzuza ngaha");
            return false;
        }
        a_payer = Double.parseDouble(kurangura_prix)*Double.parseDouble(kurangura_qtt);
        payee = Double.parseDouble(kurangura_payee);
        if(payee<a_payer){
            if(client.isEmpty()) {
                field_kurangura_personne.setError("ko atarishe yose uzuza izina");
                return false;
            } else {
                ideni = true;
            }
        }
        return true;
    }

    public void setEdition(boolean edition, ActionStock as) {
        this.edition = edition;
        lbl_kurangura_product.setText(as.produit.nom);
        field_kurangura_qtt.setText(as.quantite.toString());
        field_kurangura_prix.setText(as.prix.toString());
        field_kurangura_total.setText(as.total().toString());
        field_kurangura_payee.setText(as.payee.toString());
        try {
            field_kurangura_personne.setText(as.personne.nom);
        } catch (NullPointerException e){ }
    }
}
