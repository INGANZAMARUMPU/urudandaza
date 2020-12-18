package bi.konstrictor.urudandaza.dialogs;

import android.app.Dialog;
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

public class PasswordForm extends Dialog {

    private final AppCompatActivity context;
    private TextView field_admin_password, field_admin_new_password, field_admin_new_password2;
    private Button btn_cancel, btn_submit;
    private String password, new_password, new_password2;

    public PasswordForm(AppCompatActivity context) {
        super(context, R.style.Theme_AppCompat_DayNight_Dialog);
        setContentView(R.layout.form_password);
        this.context = context;

        field_admin_password = findViewById(R.id.field_admin_password);
        field_admin_new_password = findViewById(R.id.field_admin_new_password);
        field_admin_new_password2 = findViewById(R.id.field_admin_new_password2);

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
            InkoranyaMakuru db = new InkoranyaMakuru(context);
            try {
                Dao<Password, Integer> dao = db.getDao(Password.class);
                Account admin = new Account().getAdminAccount(context);
                String str_signature = Globals.hash(this.password);
                Password password;
                ArrayList<Password> signatures = (ArrayList<Password>)
                        dao.queryForEq("account_id", admin.id);
                if(signatures.size()>0) {
                    password = signatures.get(signatures.size()-1);
                    if (password.getSignature().equals(str_signature)) {
                        password.setHash(new_password);
                        password.update(context);
                    } else {
                        field_admin_password.setError("mwihenze code yahoramwo");
                    }
                } else {
                    password = new Password(new_password, admin);
                    password.create(context);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean validateFields() {
        password = field_admin_password.getText().toString().trim();
        new_password = field_admin_new_password.getText().toString().trim();
        new_password2 = field_admin_new_password2.getText().toString().trim();
//        if(password.isEmpty()) {
//            field_admin_password.setError("banza mwuzuze aha");
//            return false;
//        }
        if(new_password.isEmpty()) {
            field_admin_new_password.setError("banza mwuzuze aha");
            return false;
        }
        if(new_password2.isEmpty()) {
            field_admin_new_password2.setError("banza mwuzuze aha");
            return false;
        }
        if(!new_password.equalsIgnoreCase(new_password2)) {
            field_admin_password.setError("itegereza gusa n'iyo nshasha");
            return false;
        }
        return true;
    }

    public void build(){
        show();
    }
}
