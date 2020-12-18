package bi.konstrictor.urudandaza;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent i=new Intent(LoginActivity.this,
                MainDashBoardActivity.class);
        startActivity(i);
    }
    public void nextActivity(View v){
        Intent i=new Intent(LoginActivity.this,
                MainDashBoardActivity.class);
        startActivity(i);
    }
}
