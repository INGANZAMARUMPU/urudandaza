package bi.konstrictor.urudandaza;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;

import java.util.Random;

import bi.konstrictor.urudandaza.dialogs.PasswordForm;
import bi.konstrictor.urudandaza.dialogs.RechargeForm;
import bi.konstrictor.urudandaza.models.Password;

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
        Intent open_ibidandazwa = new Intent(MainDashBoardActivity.this, RemboursementActivity.class);
        startActivity(open_ibidandazwa);
    }

    public void changePassword(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_fadein));
        PasswordForm form = new PasswordForm(this);
        form.build();
    }

    public void whatsappMe(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_fadein));
        String[] url = {"https://wa.me/+25775960696", "https://wa.me/+25771208396"};
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url[new Random().nextInt(2)]));
        startActivity(i);
    }

    public void startPayment(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_fadein));
        new RechargeForm(this).build();
    }
}