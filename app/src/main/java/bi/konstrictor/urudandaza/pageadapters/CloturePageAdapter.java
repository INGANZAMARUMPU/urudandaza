package bi.konstrictor.urudandaza.pageadapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import bi.konstrictor.urudandaza.DetailHistActivity;
import bi.konstrictor.urudandaza.fragments.ClotureFragment;
import bi.konstrictor.urudandaza.fragments.StockFragment;

public class CloturePageAdapter extends FragmentStatePagerAdapter {
    private ClotureFragment cloture_fragment;
    private StockFragment stock_fragment;
    DetailHistActivity context;

    public CloturePageAdapter(FragmentManager fm, int behavior, DetailHistActivity context) {
        super(fm, behavior);
        this.cloture_fragment = new ClotureFragment(context);
        this.stock_fragment = new StockFragment(context);
    }
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 1:
                return stock_fragment;
            default:
                return cloture_fragment;
        }
    }
    @Override
    public int getCount() {
        return 2;
    }
}
