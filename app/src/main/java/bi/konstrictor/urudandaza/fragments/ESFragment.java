package bi.konstrictor.urudandaza.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import bi.konstrictor.urudandaza.ClotureActivity;
import bi.konstrictor.urudandaza.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ESFragment extends Fragment {
    private View view;

    public ESFragment() {
        super();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_e_s, container, false);
        return view;
    }
}
