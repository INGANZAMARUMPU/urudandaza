package bi.konstrictor.urudandaza.dialogs;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.widget.Button;
import android.widget.TextView;

import bi.konstrictor.urudandaza.R;
import bi.konstrictor.urudandaza.RefreshableActivity;

public class ClientForm extends Dialog {

    private final RefreshableActivity context;
    private TextView field_client_name, field_client_tel, field_client_autres;
    private Button btn_client_cancel, btn_client_submit;
    private String[] arrcontact;

    public ClientForm(RefreshableActivity context) {
        super(context, R.style.Theme_AppCompat_DayNight_Dialog);
        setContentView(R.layout.form_client);
        this.context = context;

        field_client_name = findViewById(R.id.field_client_name);
        field_client_tel = findViewById(R.id.field_client_tel);
        field_client_autres = findViewById(R.id.field_client_autres);
        btn_client_cancel = findViewById(R.id.btn_client_cancel);
        btn_client_submit = findViewById(R.id.btn_client_submit);
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
    public void build(){
        show();
    }
}
