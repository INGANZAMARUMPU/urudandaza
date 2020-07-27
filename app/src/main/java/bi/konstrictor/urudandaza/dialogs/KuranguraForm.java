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
    private TextView lbl_kurangura_product, field_kurangura_prix, field_kurangura_total,field_kurangura_qtt;
    private String kurangura_prix, kurangura_qtt;
    private AutoCompleteTextView field_kurangura_personne;
    private ProgressBar progress_kurangura;
    public Boolean something_changed = false;
    private String[] arrcontact;
    final int PRIX=10, TOTAL=20;
    private Produit produit;

    public boolean isEdition() {
        return edition;
    }

    private boolean edition;

    public KuranguraForm(final RefreshableActivity context, Produit produit) {
        super(context, R.style.Theme_AppCompat_DayNight_Dialog);
        setContentView(R.layout.form_kurangura);
        this.context = context;
        this.produit = produit;

        lbl_kurangura_product = findViewById(R.id.lbl_kurangura_product);
        field_kurangura_qtt = findViewById(R.id.field_kurangura_qtt);
        field_kurangura_prix = findViewById(R.id.field_kurangura_prix);
        field_kurangura_total = findViewById(R.id.field_kurangura_total);
        field_kurangura_personne = findViewById(R.id.field_kurangura_personne);
        progress_kurangura = findViewById(R.id.progress_kurangura);

        Button btn_kurangura_submit = findViewById(R.id.btn_kurangura_submit);
        Button btn_kurangura_cancel = findViewById(R.id.btn_kurangura_cancel);

        lbl_kurangura_product.setText(produit.nom);
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                loadContact();
            }else {
                loadClient();
            }
        }else{
            loadContact();
        }
        field_kurangura_personne.setAdapter(new ArrayAdapter<String>(context,
                android.R.layout.simple_dropdown_item_1line, arrcontact));
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
                    }else {
                        if(quantity!=0) {
                            total = value/quantity;
                        }else{
                            total = 0.;
                        }
                        total = value*quantity;
                        field_kurangura_prix.setText(total.toString());
                    }
                }
            }
        };
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
            progress_kurangura.setVisibility(View.VISIBLE);
            if (edition) {

            } else {
                double qtt = Double.parseDouble(kurangura_qtt);
                double prix = Double.parseDouble(kurangura_prix);
                ActionStock action = new ActionStock(produit, qtt, prix);
                try {
                    Dao dao_action = new InkoranyaMakuru(context).getDaoActionStock();
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
    private void loadContact() {
        Cursor cursor = getContacts();
        arrcontact = new String[cursor.getCount()];
        int count = 0;

        while (cursor.moveToNext()) {
            String displayName = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
            arrcontact[count] = displayName + "\n" + "908228282";
            count++;
        }
    }

    private Cursor getContacts() {
        final ContentResolver cr = context.getContentResolver();
        String[] projection = { ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts._ID };
        String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = ?";
        String[] selectionArgs = { "1" };
        final Cursor contacts = cr.query(ContactsContract.Contacts.CONTENT_URI,
                projection, selection, selectionArgs, "UPPER("
                        + ContactsContract.Contacts.DISPLAY_NAME + ") ASC");
        return contacts;
    }
    private Boolean validateFields() {
        kurangura_qtt = field_kurangura_qtt.getText().toString().trim();
        kurangura_prix = field_kurangura_prix.getText().toString().trim();
        if(kurangura_prix.isEmpty()){
            field_kurangura_prix.setError("uzuza ngaha");
            return false;
        }
        if(kurangura_qtt.isEmpty()){
            field_kurangura_qtt.setError("uzuza ngaha");
            return false;
        }
        return true;
    }
}
