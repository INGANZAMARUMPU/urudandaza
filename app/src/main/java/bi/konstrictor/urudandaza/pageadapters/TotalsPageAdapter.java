package bi.konstrictor.urudandaza.pageadapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import bi.konstrictor.urudandaza.ClotureActivity;
import bi.konstrictor.urudandaza.fragments.ESFragment;
import bi.konstrictor.urudandaza.fragments.FinalFragment;
import bi.konstrictor.urudandaza.fragments.TotalsFragment;

public class TotalsPageAdapter extends FragmentStatePagerAdapter {
    private ESFragment es_fragment;
    private FinalFragment final_fragment;
    private TotalsFragment totals_fragment;
    ClotureActivity context;

    public TotalsPageAdapter(FragmentManager fm, int behavior, ClotureActivity context) {
        super(fm, behavior);
        this.es_fragment = new ESFragment(context);
        this.final_fragment = new FinalFragment(context);
        this.totals_fragment = new TotalsFragment(context);
    }
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 1:
                return es_fragment;
            case 2:
                return final_fragment;
            default:
                return totals_fragment;
        }
    }
    @Override
    public int getCount() {
        return 3;
    }
}
