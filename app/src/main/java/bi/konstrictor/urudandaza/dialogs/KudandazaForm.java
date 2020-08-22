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
import com.j256.ormlite.stmt.UpdateBuilder;

import java.sql.SQLException;
import java.util.List;

import bi.konstrictor.urudandaza.InkoranyaMakuru;
import bi.konstrictor.urudandaza.OnTextChangeListener;
import bi.konstrictor.urudandaza.R;
import bi.konstrictor.urudandaza.RefreshableActivity;
import bi.konstrictor.urudandaza.models.ActionStock;
import bi.konstrictor.urudandaza.models.Liquide;
import bi.konstrictor.urudandaza.models.Personne;
import bi.konstrictor.urudandaza.models.Produit;

import static android.Manifest.permission.READ_CONTACTS;

public class KudandazaForm extends Dialog {
    private RefreshableActivity context;
    private TextView lbl_kudandaza_product, field_kudandaza_prix, field_kudandaza_total,
            field_kudandaza_qtt, field_kudandaza_payee, lbl_kudandaza_unite;
    private AutoCompleteTextView field_kudandaza_personne;
    private String kudandaza_prix, kudandaza_qtt, client, kudandaza_payee;
    private ProgressBar progress_kudandaza;
    private String[] arrcontact;
    private double payee, a_payer;
    private Produit produit;
    private boolean edition = false;
    private boolean ideni = false;
    private ActionStock action_stock;

    public KudandazaForm(final RefreshableActivity context, Produit produit) {
        super(context, R.style.Theme_AppCompat_DayNight_Dialog);
        setContentView(R.layout.form_kudandaza);
        this.context = context;
        this.produit = produit;

        lbl_kudandaza_product = findViewById(R.id.lbl_kudandaza_product);
        field_kudandaza_qtt = findViewById(R.id.field_kudandaza_qtt);
        field_kudandaza_prix = findViewById(R.id.field_kudandaza_prix);
        field_kudandaza_total = findViewById(R.id.field_kudandaza_total);
        field_kudandaza_payee = findViewById(R.id.field_kudandaza_payee);
        field_kudandaza_personne = findViewById(R.id.field_kudandaza_personne);
        lbl_kudandaza_unite = findViewById(R.id.lbl_kudandaza_unite);
        progress_kudandaza = findViewById(R.id.progress_kudandaza);

        Button btn_kudandaza_submit = findViewById(R.id.btn_kudandaza_submit);
        Button btn_kudandaza_cancel = findViewById(R.id.btn_kudandaza_cancel);
        Button btn_reset_payee = findViewById(R.id.btn_reset_payee);

        lbl_kudandaza_product.setText(produit.nom);
        lbl_kudandaza_unite.setText(produit.unite_entrant);
        btn_kudandaza_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btn_kudandaza_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
        btn_reset_payee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { field_kudandaza_payee.setText("0"); }
        });
        init();
    }

    private void init() {
        field_kudandaza_qtt.addTextChangedListener(new OnTextChangeListener() {
            @Override
            public void textChanged(Editable s) {
                if(field_kudandaza_qtt.hasFocus()) {
                    String str_prix = field_kudandaza_prix.getText().toString().trim();
                    String str_total = field_kudandaza_total.getText().toString().trim();
                    if (s.length()<1) return;
                    Double quantity = Double.parseDouble(s.toString());
                    if (!str_prix.isEmpty()) {
                        Double prix = Double.parseDouble(str_prix);
                        Double total;
                        total = prix*quantity;
                        field_kudandaza_total.setText(total.toString());
                        field_kudandaza_payee.setText(total.toString());
                    } else if (!str_total.isEmpty()) {
                        Double total = Double.parseDouble(str_total);
                        Double prix;
                        prix = total/quantity;
                        field_kudandaza_prix.setText(prix.toString());
                        field_kudandaza_payee.setText(total.toString());
                    }
                }
            }
        });
        field_kudandaza_total.addTextChangedListener(new OnTextChangeListener() {
            @Override
            public void textChanged(Editable s) {
                if(field_kudandaza_total.hasFocus()) {
                    String str_qtt = field_kudandaza_qtt.getText().toString().trim();
                    if ((!str_qtt.isEmpty()) && s.length()>0) {
                        Double quantity = Double.parseDouble(str_qtt);
                        Double total = Double.parseDouble(s.toString());
                        Double prix = total/quantity;
                        field_kudandaza_prix.setText(prix.toString());
                        field_kudandaza_payee.setText(total.toString());
                    }
                }
            }
        });

        field_kudandaza_prix.addTextChangedListener(new OnTextChangeListener() {
            @Override
            public void textChanged(Editable s) {
                if (field_kudandaza_prix.hasFocus()) {
                    String str_qtt = field_kudandaza_qtt.getText().toString().trim();
                    if ((!str_qtt.isEmpty()) && s.length()>0) {
                        Double quantity = Double.parseDouble(str_qtt);
                        Double prix = Double.parseDouble(s.toString());
                        Double total = prix * quantity;
                        field_kudandaza_total.setText(total.toString());
                        field_kudandaza_payee.setText(total.toString());
                    }
                }
            }
        });
        field_kudandaza_personne.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    progress_kudandaza.setVisibility(View.VISIBLE);
                    loadClient();
                    field_kudandaza_personne.setAdapter(new ArrayAdapter<String>(context,
                            android.R.layout.simple_dropdown_item_1line, arrcontact));
                    progress_kudandaza.setVisibility(View.GONE);
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
    public void build(){ show(); }
    public void chargerPersonne(){
        Personne personne = Personne.getClient(client, context);
        if (personne==null){
            ClientForm form = new ClientForm(context);
            form.setPARENT_NAME_FIELD(field_kudandaza_personne);
            form.show();
            progress_kudandaza.setVisibility(View.GONE);
        }
    }
    public void submit(){
        if(validateFields()) {
            Personne personne = null;
            InkoranyaMakuru inkoranyaMakuru = new InkoranyaMakuru(context);
            progress_kudandaza.setVisibility(View.VISIBLE);
            double qtt = Double.parseDouble(kudandaza_qtt);
            double prix = Double.parseDouble(kudandaza_prix);
            ActionStock as = new ActionStock();
            if(ideni){
                personne = Personne.getClient(client, context);
                if (personne==null){
                    chargerPersonne();
                    return;
                }
                as.ideniKudandaza(produit, qtt, personne, payee, null, inkoranyaMakuru.getLatestCloture());
            } else {
                as.kudandaza(produit, qtt, inkoranyaMakuru.getLatestCloture());
            }
            if (edition) {
                as.cloture = action_stock.cloture;
                action_stock.update(context, as);
                dismiss();
            } else {
                try {
                    Dao dao_action = inkoranyaMakuru.getDaoActionStock();
                    dao_action.create(as);
                    Toast.makeText(context, "Vyagenze neza", Toast.LENGTH_LONG).show();
                    dismiss();
                } catch (SQLException e) {
                    Log.i("ERREUR", e.getMessage());
                    e.printStackTrace();
                    Toast.makeText(context, "Hari ikintu kutagenze neza", Toast.LENGTH_LONG).show();
                }
            }
            context.refresh();
            progress_kudandaza.setVisibility(View.GONE);
        }
    }
    private Boolean validateFields() {
        kudandaza_qtt = field_kudandaza_qtt.getText().toString().trim();
        kudandaza_prix = field_kudandaza_prix.getText().toString().trim();
        kudandaza_payee = field_kudandaza_payee.getText().toString().trim();
        client = field_kudandaza_personne.getText().toString().trim();
        if(kudandaza_prix.isEmpty()){
            field_kudandaza_prix.setError("uzuza ngaha");
            return false;
        }
        if(kudandaza_qtt.isEmpty()){
            field_kudandaza_qtt.setError("uzuza ngaha");
            return false;
        }
        if(kudandaza_payee.isEmpty()){
            field_kudandaza_payee.setError("uzuza ngaha");
            return false;
        }
        a_payer = Double.parseDouble(kudandaza_prix)*Double.parseDouble(kudandaza_qtt);
        payee = Double.parseDouble(kudandaza_payee);
        if(payee<a_payer){
            if(client.isEmpty()) {
                field_kudandaza_personne.setError("ko atarishe yose uzuza izina");
                return false;
            } else {
                ideni = true;
            }
        }
        return true;
    }

    public void setEdition(ActionStock as) {
        this.edition = true;
        this.action_stock = as;
        Double qtt = Math.abs(as.getQuantite());
        lbl_kudandaza_product.setText(as.produit.nom);
        field_kudandaza_qtt.setText(qtt.toString());
        field_kudandaza_prix.setText(as.getPrix().toString());
        field_kudandaza_total.setText(as.getAchatTotal().toString());
        field_kudandaza_payee.setText(as.payee.toString());
        try {
            field_kudandaza_personne.setText(as.personne.nom);
        } catch (NullPointerException e){ }
    }
}
