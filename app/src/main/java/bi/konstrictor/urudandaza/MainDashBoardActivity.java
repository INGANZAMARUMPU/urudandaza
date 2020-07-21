package bi.konstrictor.urudandaza;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainDashBoardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dashboard);
    }
    public void startIbidandazwa(View view){
        Intent open_ibidandazwa = new Intent(MainDashBoardActivity.this, IbidandazwaActivity.class);
        startActivity(open_ibidandazwa);
    }
}