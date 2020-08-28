package bi.konstrictor.urudandaza;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import bi.konstrictor.urudandaza.fragments.ESFragment;
import bi.konstrictor.urudandaza.fragments.FinalFragment;
import bi.konstrictor.urudandaza.fragments.TotalsFragment;

public class PageAdapter extends FragmentStatePagerAdapter {
    private ESFragment es_fragment;
    private FinalFragment final_fragment;
    private TotalsFragment totals_fragment;
    ClotureActivity context;

    public PageAdapter(FragmentManager fm, int behavior, ClotureActivity context) {
        super(fm, behavior);
        this.es_fragment = context.es_fragment;
        this.final_fragment = context.final_fragment;
        this.totals_fragment = context.totals_fragment;
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
