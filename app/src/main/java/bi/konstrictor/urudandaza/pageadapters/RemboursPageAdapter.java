package bi.konstrictor.urudandaza.pageadapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import bi.konstrictor.urudandaza.RemboursementActivity;
import bi.konstrictor.urudandaza.fragments.AchatFragment;
import bi.konstrictor.urudandaza.fragments.RemboursementAchatFragment;
import bi.konstrictor.urudandaza.fragments.RemboursementVenteFragment;
import bi.konstrictor.urudandaza.fragments.StockFragment;
import bi.konstrictor.urudandaza.fragments.VenteFragment;

public class RemboursPageAdapter extends FragmentStatePagerAdapter {
    private AchatFragment achat_fragment;
    private VenteFragment vente_fragment;
    private StockFragment stock_fragment;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    RemboursementActivity context;
    private int tabs_number = 3;

    public RemboursPageAdapter(FragmentManager fm, int behavior, RemboursementActivity context) {
        super(fm, behavior);
        fragments.add(new RemboursementAchatFragment(context));
        fragments.add(new RemboursementVenteFragment(context));
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
