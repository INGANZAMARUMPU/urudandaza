package bi.konstrictor.urudandaza;

import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import bi.konstrictor.urudandaza.interfaces.RefreshableActivity;
import bi.konstrictor.urudandaza.models.Achat;
import bi.konstrictor.urudandaza.models.Cloture;
import bi.konstrictor.urudandaza.models.Vente;
import bi.konstrictor.urudandaza.pageadapters.CloturePageAdapter;

public class RemboursementActivity extends RefreshableActivity {

    public CloturePageAdapter cloture_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remboursement);
        Toolbar toolbar = findViewById(R.id.history_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onCreateOptionsMenu( Menu menu) {
        getMenuInflater().inflate(R.menu.history_menu, menu);
        return true;
    }
    @Override
    public void refresh() {
    }
}
