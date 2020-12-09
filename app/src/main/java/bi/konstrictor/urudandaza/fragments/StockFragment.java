package bi.konstrictor.urudandaza.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import bi.konstrictor.urudandaza.DetailHistActivity;
import bi.konstrictor.urudandaza.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class StockFragment extends Fragment {
    private final DetailHistActivity context;
    private View view;

    public StockFragment(DetailHistActivity context) {
        super();
        this.context = context;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_stock, container, false);
        return view;
    }
}
