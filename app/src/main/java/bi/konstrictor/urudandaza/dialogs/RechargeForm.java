package bi.konstrictor.urudandaza.dialogs;

import android.app.Dialog;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;

import bi.konstrictor.urudandaza.Globals;
import bi.konstrictor.urudandaza.InkoranyaMakuru;
import bi.konstrictor.urudandaza.R;
import bi.konstrictor.urudandaza.models.Account;
import bi.konstrictor.urudandaza.models.Password;

public class RechargeForm extends Dialog {

    private final AppCompatActivity context;
    private TextView lbl_uuid, field_code_recharge;
    private Button btn_cancel, btn_submit;
    private String code_recharge;

    public RechargeForm(AppCompatActivity context) {
        super(context, R.style.Theme_AppCompat_DayNight_Dialog);
        setContentView(R.layout.form_payment);
        this.context = context;

        field_code_recharge = findViewById(R.id.field_code_recharge);
        lbl_uuid = findViewById(R.id.lbl_uuid);

        String android_id = Settings.Secure.getString(getContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        lbl_uuid.setText(android_id);

        btn_cancel = findViewById(R.id.btn_cancel);
        btn_submit = findViewById(R.id.btn_submit);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    private void submit() {
        if(validateFields()) {
        }
    }

    private boolean validateFields() {
        code_recharge = field_code_recharge.getText().toString().trim();
        if(code_recharge.isEmpty()) {
            field_code_recharge.setError("banza mwuzuze aha");
            return false;
        }
        return true;
    }

    public void build(){
        show();
    }
}
