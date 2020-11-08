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
import bi.konstrictor.urudandaza.models.Cloture;
import bi.konstrictor.urudandaza.models.Liquide;
import bi.konstrictor.urudandaza.models.Personne;
import bi.konstrictor.urudandaza.models.Produit;

import static android.Manifest.permission.READ_CONTACTS;

public class KuranguraForm extends Dialog {
    private RefreshableActivity context;
    private TextView lbl_kurangura_product, field_kurangura_prix, field_kurangura_total,
            field_kurangura_qtt, field_kurangura_payee, lbl_kurangura_unite,
            field_kurangura_qtt_supl, lbl_kurangura_unite_sortant;
    private AutoCompleteTextView field_kurangura_personne;
    private String kurangura_prix, kurangura_qtt, client, kurangura_payee, kurangura_qtt_suppl;
    private Double quantite;
    private ProgressBar progress_kurangura;
    private String[] arrcontact;
    private double payee, a_payer;
    private Produit produit;
    private boolean edition = false;
    private boolean ideni = false;
    private ActionStock action_stock;

    public KuranguraForm(final RefreshableActivity context, Produit produit) {
        super(context, R.style.Theme_AppCompat_DayNight_Dialog);
        setContentView(R.layout.form_kurangura);
        this.context = context;
        this.produit = produit;

        lbl_kurangura_product = findViewById(R.id.lbl_kurangura_product);
        field_kurangura_qtt = findViewById(R.id.field_kurangura_qtt);
        field_kurangura_qtt_supl = findViewById(R.id.field_kurangura_qtt_supl);
        field_kurangura_prix = findViewById(R.id.field_kurangura_prix);
        field_kurangura_total = findViewById(R.id.field_kurangura_total);
        field_kurangura_payee = findViewById(R.id.field_kurangura_payee);
        field_kurangura_personne = findViewById(R.id.field_kurangura_personne);
        field_kurangura_personne = findViewById(R.id.field_kurangura_personne);
        lbl_kurangura_unite = findViewById(R.id.lbl_kurangura_unite);
        lbl_kurangura_unite_sortant = findViewById(R.id.lbl_kurangura_unite_sortant);
        progress_kurangura = findViewById(R.id.progress_kurangura);

        Button btn_kurangura_submit = findViewById(R.id.btn_kurangura_submit);
        Button btn_kurangura_cancel = findViewById(R.id.btn_kurangura_cancel);
        Button btn_reset_payee = findViewById(R.id.btn_reset_payee);

        lbl_kurangura_product.setText(produit.nom);
        lbl_kurangura_unite.setText(produit.unite_entrant);
        if (produit.rapport==1)
            lbl_kurangura_unite_sortant.setVisibility(View.GONE);
        else
            lbl_kurangura_unite_sortant.setText(produit.unite_sortant);
        field_kurangura_prix.setHint("igiciro ca "+produit.unite_entrant+" imwe");
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

    private void evalQtt(){
        String str_qtt = field_kurangura_qtt.getText().toString().trim();
        String str_qtt_suppl = field_kurangura_qtt_supl.getText().toString().trim();
        Double qtt, qtt_suppl;
        if (str_qtt.isEmpty()) qtt = 0.0;
        else qtt = Double.parseDouble(str_qtt)*produit.rapport;
        if (str_qtt_suppl.isEmpty()) qtt_suppl = 0.0;
        else qtt_suppl = Double.parseDouble(str_qtt_suppl);
        quantite = qtt+qtt_suppl;
    }
    private void init() {

//        field_kurangura_qtt.addTextChangedListener(new OnTextChangeListener() {
//            @Override
//            public void textChanged(Editable s) {
//                if(field_kurangura_qtt.hasFocus()) {
//                    String str_prix = field_kurangura_prix.getText().toString().trim();
//                    String str_total = field_kurangura_total.getText().toString().trim();
//                    if (s.length()<1) return;
//                    Double quantity = Double.parseDouble(s.toString());
//                    if (!str_prix.isEmpty()) {
//                        Double prix = Double.parseDouble(str_prix);
//                        Double total;
//                        total = prix*quantity;
//                        field_kurangura_total.setText(total.toString());
//                        field_kurangura_payee.setText(total.toString());
//                    } else if (!str_total.isEmpty()) {
//                        Double total = Double.parseDouble(str_total);
//                        Double prix;
//                        prix = total/quantity;
//                        field_kurangura_prix.setText(prix.toString());
//                        field_kurangura_payee.setText(total.toString());
//                    }
//                }
//            }
//        });
        field_kurangura_total.addTextChangedListener(new OnTextChangeListener() {
            @Override
            public void textChanged(Editable s) {
                if(field_kurangura_total.hasFocus()) {
                    if (s.length()>0) {
                        evalQtt();
                        Double total = Double.parseDouble(s.toString());
                        Double prix = total/(quantite/produit.rapport);
                        field_kurangura_prix.setText(prix.toString());
                        field_kurangura_payee.setText(total.toString());
                    }
                }
            }
        });

        field_kurangura_prix.addTextChangedListener(new OnTextChangeListener() {
            @Override
            public void textChanged(Editable s) {
                if (field_kurangura_prix.hasFocus()) {
                    if (s.length()>0) {
                        evalQtt();
                        Double prix = Double.parseDouble(s.toString());
                        Double total = prix *(quantite/produit.rapport);
                        field_kurangura_total.setText(total.toString());
                        field_kurangura_payee.setText(total.toString());
                    }
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
    public void build(){ show(); }
    public void chargerPersonne(){
        Personne personne = Personne.getClient(client, context);
        if (personne==null){
            ClientForm form = new ClientForm(context);
            form.setPARENT_NAME_FIELD(field_kurangura_personne);
            form.show();
            progress_kurangura.setVisibility(View.GONE);
        }
    }
    public void submit(){
        if(validateFields()) {
            Personne personne = null;
            Cloture cloture = new InkoranyaMakuru(context).getLatestCloture();
            progress_kurangura.setVisibility(View.VISIBLE);
            double qtt = Double.parseDouble(kurangura_qtt);
            double qtt_suppl = Double.parseDouble(kurangura_qtt_suppl);
            double prix = Double.parseDouble(kurangura_prix);
            if(ideni){
                personne = Personne.getClient(client, context);
                if (personne==null){
                    chargerPersonne();
                    return;
                }
            }
            if (edition) {
                action_stock.kurangura(produit, qtt, qtt_suppl, prix, personne, payee, null, cloture);
                action_stock.update(context);
                dismiss();
            } else {
                try {
                    ActionStock as = new ActionStock();
                    as.kurangura(produit, qtt, qtt_suppl, prix, personne, payee, null, cloture);
                    Dao dao_action = new InkoranyaMakuru(context).getDaoActionStock();
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
            progress_kurangura.setVisibility(View.GONE);
        }
    }
    private Boolean validateFields() {
        kurangura_qtt = field_kurangura_qtt.getText().toString().trim();
        kurangura_qtt_suppl = field_kurangura_qtt_supl.getText().toString().trim();
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
        if(kurangura_qtt_suppl.isEmpty()){
            field_kurangura_qtt.setText(0);
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

    public void setEdition(ActionStock as) {
        this.edition = true;
        this.action_stock = as;
        field_kurangura_qtt_supl.setText(Integer.toString(as.getQuantiteSuppl()));
        lbl_kurangura_product.setText(as.produit.nom);
        field_kurangura_qtt.setText(Integer.toString(as.getQuantite().intValue()));
        field_kurangura_prix.setText(as.getPrix().toString());
        field_kurangura_total.setText(as.getAchatTotal().toString());
        field_kurangura_payee.setText(as.payee.toString());
        try {
            field_kurangura_personne.setText(as.personne.nom);
        } catch (NullPointerException e){ }
    }
}
