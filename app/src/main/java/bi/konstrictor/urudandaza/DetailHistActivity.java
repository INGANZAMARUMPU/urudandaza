package bi.konstrictor.urudandaza;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import bi.konstrictor.urudandaza.dialogs.FilterActionForm;
import bi.konstrictor.urudandaza.fragments.AchatFragment;
import bi.konstrictor.urudandaza.interfaces.RefreshableActivity;
import bi.konstrictor.urudandaza.models.Achat;
import bi.konstrictor.urudandaza.models.Cloture;
import bi.konstrictor.urudandaza.models.Vente;
import bi.konstrictor.urudandaza.pageadapters.CloturePageAdapter;

public class DetailHistActivity extends RefreshableActivity {

    public CloturePageAdapter cloture_adapter;
    private ViewPager view_pager_cloture;
    private TabLayout tab_layout_cloture;
    public HashMap<String, String> filters;
    public Boolean is_dette;
    public Cloture cloture;
    public ArrayList<Achat> achats = new ArrayList<>();
    public ArrayList<Vente> ventes = new ArrayList<>();
    public ArrayList<Date> dates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_det_hist);
        Toolbar toolbar = findViewById(R.id.history_toolbar);

        filters = (HashMap<String, String>) getIntent().getSerializableExtra("filters");
        dates = (ArrayList<Date>) getIntent().getSerializableExtra("dates");

        is_dette = getIntent().getExtras().getBoolean("is_dette");
        cloture = (Cloture) getIntent().getSerializableExtra("cloture");

        view_pager_cloture = findViewById(R.id.view_pager_cloture);
        tab_layout_cloture = findViewById(R.id.tab_layout_cloture);

        cloture_adapter = new CloturePageAdapter(
                getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                this);
        view_pager_cloture.setAdapter(cloture_adapter);
        view_pager_cloture.setOffscreenPageLimit(2);
        tab_layout_cloture.setupWithViewPager(view_pager_cloture);

        if (cloture == null){
            tab_layout_cloture.removeTabAt(2);
            cloture_adapter.removeTabPage(2);
//            tab_layout_cloture.setVisibility(View.GONE);
            tab_layout_cloture.getTabAt(0).setText("KURANGURA");
            tab_layout_cloture.getTabAt(1).setText("KUDANDAZA");
        }else{
            tab_layout_cloture.getTabAt(0).setText("KURANGURA");
            tab_layout_cloture.getTabAt(1).setText("KUDANDAZA");
            tab_layout_cloture.getTabAt(2).setText("STOCK");
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onCreateOptionsMenu( Menu menu) {
        if (is_dette) {
            getMenuInflater().inflate(R.menu.pay_menu, menu);
            return true;
        }
        getMenuInflater().inflate(R.menu.history_menu, menu);
        return true;
    }
    @Override
    public void refresh() {

    }
}
