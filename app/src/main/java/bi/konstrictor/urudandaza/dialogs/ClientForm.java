package bi.konstrictor.urudandaza.dialogs;

import android.app.Dialog;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

import bi.konstrictor.urudandaza.R;
import bi.konstrictor.urudandaza.RefreshableActivity;
import bi.konstrictor.urudandaza.models.Personne;

import static android.Manifest.permission.READ_CONTACTS;

public class ClientForm extends Dialog {

    private final RefreshableActivity context;
    private TextView field_client_tel, field_client_autres;
    private AutoCompleteTextView field_client_name;
    private TextView PARENT_NAME_FIELD;
    private Button btn_client_cancel, btn_client_submit;
    private String[] arrcontact;
    HashMap<String, String> dict_contact;
    private boolean EDIT_MODE = false;
    private Personne client;

    public ClientForm(RefreshableActivity context) {
        super(context, R.style.Theme_AppCompat_DayNight_Dialog);
        setContentView(R.layout.form_client);
        this.context = context;

        field_client_name = findViewById(R.id.field_client_name);
        field_client_tel = findViewById(R.id.field_client_tel);
        field_client_autres = findViewById(R.id.field_client_autres);
        btn_client_cancel = findViewById(R.id.btn_client_cancel);
        btn_client_submit = findViewById(R.id.btn_client_submit);
        btn_client_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
        btn_client_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        field_client_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                field_client_tel.setText(dict_contact.get(selected));
            }
        });
    }

    private void submit() {
        if (validateFields()){
            if(EDIT_MODE){
                client.nom = field_client_name.getText().toString();
                client.phone = field_client_tel.getText().toString();
                client.autres = field_client_autres.getText().toString();
                client.update(context);
                context.refresh();
                dismiss();
            }else{
                new Personne(
                    field_client_name.getText().toString(),
                    field_client_tel.getText().toString(),
                    field_client_autres.getText().toString()).create(context);
//                    if(PARENT_NAME_FIELD == null) context.refresh();
//                    else
                    PARENT_NAME_FIELD.setText(field_client_name.getText());
                dismiss();
            }
        }
    }

    private boolean validateFields() {
        String nom = field_client_name.getText().toString().trim();
        String tel = field_client_tel.getText().toString().trim();
        if(nom.isEmpty()){
            field_client_name.setError("uzuza ngaha");
            return false;
        }
        if(tel.isEmpty()){
            field_client_tel.setError("uzuza ngaha");
            return false;
        }
        return true;
    }
    private void loadContact() {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = new String[] {
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER };

        Cursor people = context.getContentResolver().query(uri, projection, null, null, null);
        int indexName = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        int indexNumber = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        arrcontact = new String[people.getCount()];
        int count = 0;
        dict_contact = new HashMap<>();
        while (people.moveToNext()) {
            String name = people.getString(indexName);
            String number = people.getString(indexNumber);
            String contact = name + "" + number;
            dict_contact.put(name, number);
            arrcontact[count] = name;
            count++;
        }
    }
    public void show(){
        super.show();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                loadContact();
            }
        }else{
            loadContact();
        }
        field_client_name.setAdapter(new ArrayAdapter<String>(context,
                android.R.layout.simple_dropdown_item_1line, arrcontact));
    }
    public void setClient(Personne client) {
        this.client = client;
        field_client_name.setText(client.nom);
        field_client_tel.setText(client.phone);
        field_client_autres.setText(client.autres);
        this.EDIT_MODE = true;
    }

    public void setPARENT_NAME_FIELD(TextView PARENT_NAME_FIELD) {
        this.PARENT_NAME_FIELD = PARENT_NAME_FIELD;
        field_client_name.setText(PARENT_NAME_FIELD.getText());
    }
}
