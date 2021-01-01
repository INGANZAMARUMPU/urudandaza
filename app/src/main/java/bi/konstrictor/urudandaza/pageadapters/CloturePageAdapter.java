package bi.konstrictor.urudandaza.pageadapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import bi.konstrictor.urudandaza.DetailHistActivity;
import bi.konstrictor.urudandaza.fragments.AchatFragment;
import bi.konstrictor.urudandaza.fragments.StockFragment;
import bi.konstrictor.urudandaza.fragments.VenteFragment;

public class CloturePageAdapter extends FragmentStatePagerAdapter {
    private AchatFragment achat_fragment;
    private VenteFragment vente_fragment;
    private StockFragment stock_fragment;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    DetailHistActivity context;
    private int tabs_number = 3;

    public CloturePageAdapter(FragmentManager fm, int behavior, DetailHistActivity context) {
        super(fm, behavior);
        fragments.add(new AchatFragment(context));
        fragments.add(new VenteFragment(context));
        fragments.add(new StockFragment(context, context.cloture));
    }
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }
    @Override
    public int getCount() {
        return fragments.size();
    }

    public void removeTabPage(int i) {
        fragments.remove(i);
        notifyDataSetChanged();
    }
}
