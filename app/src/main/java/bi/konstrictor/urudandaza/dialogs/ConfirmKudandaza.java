package bi.konstrictor.urudandaza.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bi.konstrictor.urudandaza.InkoranyaMakuru;
import bi.konstrictor.urudandaza.R;
import bi.konstrictor.urudandaza.interfaces.RefreshableActivity;
import bi.konstrictor.urudandaza.models.Cloture;
import bi.konstrictor.urudandaza.models.Vente;
import bi.konstrictor.urudandaza.models.Personne;

public class ConfirmKudandaza extends Dialog {
    private RefreshableActivity context;
    private TextView lbl_vente_list;
    private AutoCompleteTextView field_vente_client;
    private EditText field_vente_payee;
    private CheckBox check_vente_expired;
    private ProgressBar progress_vente;
    private String[] arrcontact;
    private ArrayList<Vente> CART;
    private Double payee, montant;
    private String client;
    private boolean ideni = false;
    private boolean expired;

    public ConfirmKudandaza(final RefreshableActivity context, ArrayList CART, Double montant) {
        super(context, R.style.Theme_AppCompat_DayNight_Dialog);
        setContentView(R.layout.form_vente);
        this.context = context;
        this.CART = CART;
        this.montant = montant;

        lbl_vente_list = findViewById(R.id.lbl_vente_list);
        field_vente_client = findViewById(R.id.field_vente_client);
        progress_vente = findViewById(R.id.progress_vente);
        field_vente_payee = findViewById(R.id.field_vente_payee);
        check_vente_expired = findViewById(R.id.check_vente_expired);

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
        String confirmation = "";
        for(Vente as:CART){
            confirmation += "- "+as.toString()+"\n";
        }
        lbl_vente_list.setText(confirmation);
        field_vente_payee.setText(montant.toString());
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
        field_vente_client.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    progress_vente.setVisibility(View.VISIBLE);
                    loadClient();
                    field_vente_client.setAdapter(new ArrayAdapter<String>(context,
                            android.R.layout.simple_dropdown_item_1line, arrcontact));
                    progress_vente.setVisibility(View.GONE);
                    Toast.makeText(context, "Lecture des contacts",Toast.LENGTH_LONG).show();
                }
            }
        });
        check_vente_expired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check_vente_expired.isChecked()){
                    new AlertDialog.Builder(context)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Ivyononekanye")
                            .setMessage("uremeje vy'ukuri ko ivyo ari ivyononekaye?")
                            .setPositiveButton("Ego", new OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    setExpired(true);
                                }
                            })
                            .setNegativeButton("Oya", null)
                            .show();
                } else{
                    setExpired(false);
                }
            }
        });
    }

    private void setExpired(boolean expired) {
        this.expired = expired;
        if (expired){
            field_vente_client.setText("");
            field_vente_payee.setText("0");
            field_vente_client.setEnabled(false);
            field_vente_payee.setEnabled(false);
        } else {
            field_vente_client.setEnabled(true);
            field_vente_payee.setEnabled(true);
        }
    }

    private void loadClient() {
        try {
            Dao dao_clients = new InkoranyaMakuru(context).getDao(Personne.class);
            List<Personne> personnes = dao_clients.queryForAll();
            arrcontact = new String[personnes.size()];
            for (int i=0; i<personnes.size(); i++){
                arrcontact[i] = personnes.get(i).nom;
            }
        } catch (SQLException e) {
            Toast.makeText(context, "Erreur de lecture des contacts",Toast.LENGTH_LONG).show();
        }
    }
    public void chargerPersonne(){
        Personne personne = Personne.getClient(client, context);
        if (personne==null){
            ClientForm form = new ClientForm(context);
            form.setPARENT_NAME_FIELD(field_vente_client);
            form.show();
            progress_vente.setVisibility(View.GONE);
        }
    }
    public void submit(){
        progress_vente.setVisibility(View.VISIBLE);
        if (expired){
            performExpiration();
        }else{
            performSubmition();
        }
        progress_vente.setVisibility(View.GONE);
    }
    private void performExpiration() {
        InkoranyaMakuru inkoranyaMakuru = new InkoranyaMakuru(context);
        for (Vente cart : CART){
            Vente as = new Vente();
            as.expiration(cart.produit, cart.getQuantite(), inkoranyaMakuru.getLatestCloture());
            as.create(context);
            context.refresh(); dismiss();
        }
        context.refresh();
        Toast.makeText(context, "Vyakozwe", Toast.LENGTH_LONG).show();
    }
    private void performSubmition() {
        if(validateFields()) {
            Personne personne = Personne.getClient(client, context);
            if(ideni){
                personne = Personne.getClient(client, context);
                if (personne==null){
                    chargerPersonne();
                    return;
                }
            }
            InkoranyaMakuru inkoranyaMakuru = new InkoranyaMakuru(context);
            Cloture last_cloture = inkoranyaMakuru.getLatestCloture();
            for (Vente cart : CART){
                double total = cart.getTotal();
                Vente as = null;
                if (payee >= total) {
                    as = new Vente(cart.produit, cart.getQuantite(), total, personne, "", last_cloture);
                    payee -= as.getTotal();
                } else {
                    as = new Vente(cart.produit, cart.getQuantite(), payee, personne, "", last_cloture);
                    payee = 0.;
                }
                as.create(context);
                dismiss();
            }
            dismiss();
            context.refresh();
            Toast.makeText(context, "Vyaguzwe", Toast.LENGTH_LONG).show();
        }
    }

    private Boolean validateFields() {
        payee = Double.parseDouble(field_vente_payee.getText().toString());
        client = field_vente_client.getText().toString().trim();
        if(payee<montant){
            ideni = true;
            if(client.isEmpty()) {
                field_vente_client.setError("ko atarishe yose uzuza izina");
                return false;
            }
        }
        return true;
    }
    public void build(){ show(); }
}
