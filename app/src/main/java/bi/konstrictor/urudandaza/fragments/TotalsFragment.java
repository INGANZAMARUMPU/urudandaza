package bi.konstrictor.urudandaza.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import bi.konstrictor.urudandaza.ClotureActivity;
import bi.konstrictor.urudandaza.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class TotalsFragment extends Fragment {

    public TextView lbl_hist_achat_tot, lbl_hist_achat_rest, lbl_hist_vente_tot, lbl_hist_vente_reste;
    private View view;

    public TotalsFragment() {
        super();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_totals, container, false);
        lbl_hist_achat_tot = view.findViewById(R.id.lbl_hist_achat_tot);
        lbl_hist_achat_rest = view.findViewById(R.id.lbl_hist_achat_rest);
        lbl_hist_vente_tot = view.findViewById(R.id.lbl_hist_vente_tot);
        lbl_hist_vente_reste = view.findViewById(R.id.lbl_hist_vente_reste);

        return view;
    }
}
