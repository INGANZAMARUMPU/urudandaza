package bi.konstrictor.urudandaza;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;

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
import bi.konstrictor.urudandaza.pageadapters.RemboursPageAdapter;

public class RemboursementActivity extends RefreshableActivity {

    public RemboursPageAdapter page_adapter;
    private ViewPager view_pager_remboursement;
    private TabLayout tab_layout_remboursement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remboursement);
        Toolbar toolbar = findViewById(R.id.history_toolbar);

        view_pager_remboursement = findViewById(R.id.view_pager_remboursement);
        tab_layout_remboursement = findViewById(R.id.tab_layout_remboursement);

        page_adapter = new RemboursPageAdapter(
                getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                this);
        view_pager_remboursement.setAdapter(page_adapter);
        view_pager_remboursement.setOffscreenPageLimit(2);
        tab_layout_remboursement.setupWithViewPager(view_pager_remboursement);

        tab_layout_remboursement.getTabAt(0).setText("KURANGURA");
        tab_layout_remboursement.getTabAt(1).setText("KUDANDAZA");

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
