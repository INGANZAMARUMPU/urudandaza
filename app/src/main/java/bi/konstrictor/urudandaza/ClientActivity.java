package bi.konstrictor.urudandaza;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import bi.konstrictor.urudandaza.dialogs.ClientForm;
import bi.konstrictor.urudandaza.dialogs.ProductForm;

public class ClientActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        Toolbar toolbar = findViewById(R.id.client_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public void startAddClient(View view){
        view.startAnimation(AnimationUtils.loadAnimation(ClientActivity.this, R.anim.button_fadein));
        ClientForm client_form = new ClientForm(ClientActivity.this);
        client_form.show();
    }
}
