package bi.konstrictor.urudandaza.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import bi.konstrictor.urudandaza.R;

public class ClientForm extends Dialog {

    private TextView field_client_name, field_client_tel, field_client_autres;
    private Button btn_client_cancel, btn_client_submit;

    public ClientForm(Context context) {
        super(context, R.style.Theme_AppCompat_DayNight_Dialog);
        setContentView(R.layout.form_client);

        field_client_name = findViewById(R.id.field_client_name);
        field_client_tel = findViewById(R.id.field_client_tel);
        field_client_autres = findViewById(R.id.field_client_autres);
        btn_client_cancel = findViewById(R.id.btn_client_cancel);
        btn_client_submit = findViewById(R.id.btn_client_submit);
    }
    public void build(){
        show();
    }
}
