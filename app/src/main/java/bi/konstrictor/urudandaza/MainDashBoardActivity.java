package bi.konstrictor.urudandaza;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;

import static android.Manifest.permission.READ_CONTACTS;

public class MainDashBoardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dashboard);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{READ_CONTACTS}, 10);
            }
        }
        new InkoranyaMakuru(this).getLatestCloture();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void startStock(View view){
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_fadein));
        Intent open_ibidandazwa = new Intent(MainDashBoardActivity.this, StockActivity.class);
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
        Intent open_ibidandazwa = new Intent(MainDashBoardActivity.this, ClotureActivity.class);
        startActivity(open_ibidandazwa);
    }

    public void startDepenses(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_fadein));
        Intent open_depenses = new Intent(MainDashBoardActivity.this, LiquideActivity.class);
        startActivity(open_depenses);
    }

    public void fermer(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_fadein));
    }

    public void startDettes(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_fadein));
        Intent open_ibidandazwa = new Intent(MainDashBoardActivity.this, ClotureActivity.class);
        startActivity(open_ibidandazwa);
    }
}