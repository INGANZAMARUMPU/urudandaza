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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bi.konstrictor.urudandaza.InkoranyaMakuru;
import bi.konstrictor.urudandaza.R;
import bi.konstrictor.urudandaza.RefreshableActivity;
import bi.konstrictor.urudandaza.models.ActionStock;
import bi.konstrictor.urudandaza.models.Personne;
import bi.konstrictor.urudandaza.models.Produit;

import static android.Manifest.permission.READ_CONTACTS;

public class VenteForm extends Dialog {
    private RefreshableActivity context;
    private TextView lbl_vente_list;
    private AutoCompleteTextView field_vente_client;
    private ProgressBar progress_vente;
    private String[] arrcontact;
    final int PRIX=10, TOTAL=20;
    private ArrayList<ActionStock> CART;
    private boolean edition;

    public VenteForm(final RefreshableActivity context, ArrayList CART) {
        super(context, R.style.Theme_AppCompat_DayNight_Dialog);
        setContentView(R.layout.form_vente);
        this.context = context;
        this.CART = CART;

        lbl_vente_list = findViewById(R.id.lbl_vente_list);
        field_vente_client = findViewById(R.id.field_vente_client);
        progress_vente = findViewById(R.id.progress_vente);

        Button btn_vente_submit = findViewById(R.id.btn_vente_submit);
        Button btn_vente_cancel = findViewById(R.id.btn_vente_cancel);

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

        init();
    }

    private void init() {
        field_vente_client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress_vente.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (context.checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                        loadContact();
                    }else {
                        loadClient();
                    }
                }else{
                    loadContact();
                }
                field_vente_client.setAdapter(new ArrayAdapter<String>(context,
                        android.R.layout.simple_dropdown_item_1line, arrcontact));
                progress_vente.setVisibility(View.GONE);
            }
        });
        String confirmation = "";
        for(ActionStock as:CART){
            confirmation += "- "+as.toString()+"\n";
        }
        lbl_vente_list.setText(confirmation);
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
        Toast.makeText(context, "Vyaguzwe", Toast.LENGTH_LONG).show();
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
        String[] projection = { ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts._ID };
        String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = ?";
        String[] selectionArgs = { "1" };
        final Cursor contacts = cr.query(ContactsContract.Contacts.CONTENT_URI,
                projection, selection, selectionArgs, "UPPER("
                        + ContactsContract.Contacts.DISPLAY_NAME + ") ASC");
        return contacts;
    }
    private Boolean validateFields() {
//        vente_qtt = field_vente_qtt.getText().toString().trim();
//        if(vente_qtt.isEmpty()){
//            field_vente_qtt.setError("uzuza ngaha");
//            return false;
//        }
        return true;
    }
}
