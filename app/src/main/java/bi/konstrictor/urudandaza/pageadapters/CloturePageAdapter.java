package bi.konstrictor.urudandaza.pageadapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import bi.konstrictor.urudandaza.DetailHistActivity;
import bi.konstrictor.urudandaza.fragments.AchatFragment;
import bi.konstrictor.urudandaza.fragments.StockFragment;
import bi.konstrictor.urudandaza.fragments.VenteFragment;

public class CloturePageAdapter extends FragmentStatePagerAdapter {
    private AchatFragment achat_fragment;
    private VenteFragment vente_fragment;
    private StockFragment stock_fragment;
    DetailHistActivity context;
    private int tabs_number = 3;

    public CloturePageAdapter(FragmentManager fm, int behavior, DetailHistActivity context) {
        super(fm, behavior);
        this.achat_fragment = new AchatFragment(context);
        this.vente_fragment = new VenteFragment(context);
        this.stock_fragment = new StockFragment(context, context.cloture);
    }
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 1:
                return vente_fragment;
            case 2:
                return stock_fragment;
            default:
                return achat_fragment;
        }
    }
    @Override
    public int getCount() {
        return tabs_number;
    }

    public void removeTabPage(int i) {
        tabs_number -= 1;
        notifyDataSetChanged();
    }
}
