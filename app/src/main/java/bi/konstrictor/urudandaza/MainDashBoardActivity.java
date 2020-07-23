package bi.konstrictor.urudandaza;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;

public class MainDashBoardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dashboard);
    }
    public void startStock(View view){
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_fadein));
        Intent open_ibidandazwa = new Intent(MainDashBoardActivity.this, ProductActivity.class);
        startActivity(open_ibidandazwa);
    }
    public void startVente(View view){
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_fadein));
        Intent open_ibidandazwa = new Intent(MainDashBoardActivity.this, VenteActivity.class);
        startActivity(open_ibidandazwa);
    }
    public void startClient(View view){
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_fadein));
        Intent open_ibidandazwa = new Intent(MainDashBoardActivity.this, ClientActivity.class);
        startActivity(open_ibidandazwa);
    }
    public void startHistory(View view){
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_fadein));
        Intent open_ibidandazwa = new Intent(MainDashBoardActivity.this, HistoryActivity.class);
        startActivity(open_ibidandazwa);
    }
}