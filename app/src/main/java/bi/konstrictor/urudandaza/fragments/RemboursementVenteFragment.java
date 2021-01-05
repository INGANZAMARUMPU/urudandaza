package bi.konstrictor.urudandaza.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.ArrayList;

import bi.konstrictor.urudandaza.InkoranyaMakuru;
import bi.konstrictor.urudandaza.R;
import bi.konstrictor.urudandaza.RemboursementActivity;
import bi.konstrictor.urudandaza.adapters.AdaptateurRemboursVente;
import bi.konstrictor.urudandaza.dialogs.FilterActionForm;
import bi.konstrictor.urudandaza.interfaces.Filterable;
import bi.konstrictor.urudandaza.interfaces.RemboursementFragment;
import bi.konstrictor.urudandaza.models.Account;
import bi.konstrictor.urudandaza.models.Cloture;
import bi.konstrictor.urudandaza.models.Password;
import bi.konstrictor.urudandaza.models.RemboursementAchat;
import bi.konstrictor.urudandaza.models.RemboursementVente;
import bi.konstrictor.urudandaza.models.Vente;

/**
 * A simple {@link Fragment} subclass.
 */
public class RemboursementVenteFragment extends Fragment implements Filterable, RemboursementFragment {
    private final RemboursementActivity context;
    TextView lbl_remboursement_total;
    private View view;

    RecyclerView recycler_rembours;
    Button btn_valider;
    private ArrayList<RemboursementVente> ventes = new ArrayList<>();;
    private Double vente_tot = 0.;
    private AdaptateurRemboursVente adaptateur;
    public Cloture cloture = null;
    private ArrayList<RemboursementVente> remboursements;

    public RemboursementVenteFragment(RemboursementActivity context){
        super();
        this.context = context;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_remboursement, container, false);
        setHasOptionsMenu(true);

        lbl_remboursement_total = view.findViewById(R.id.lbl_remboursement_total);
        btn_valider = view.findViewById(R.id.btn_valider);

        recycler_rembours = view.findViewById(R.id.recycler_rembours);
        recycler_rembours.setLayoutManager(new GridLayoutManager(context, 1));

        adaptateur = new AdaptateurRemboursVente(context, ventes, this);
        recycler_rembours.addItemDecoration(new DividerItemDecoration(recycler_rembours.getContext(), DividerItemDecoration.VERTICAL));
        recycler_rembours.setAdapter(adaptateur);

        btn_valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performValidation();
            }
        });

        chargerRembours();
        return view;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_filtre)
            new FilterActionForm(context, this).show();
        return super.onOptionsItemSelected(item);
    }
    public void setTot(Double vente_tot) {
        this.vente_tot = vente_tot;
        lbl_remboursement_total.setText(vente_tot.toString());
    }


    private void performValidation() {
        if(remboursements != null){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Ayarishwe");
            builder.setMessage("nimba wemeje ko ayo mahera yarishwe shiramwo code y'umukoresha:");
            final EditText input = new EditText(context);
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            builder.setView(input);
            builder.setPositiveButton("Sawa", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Account admin = new Account().getAdminAccount(context);
                    if(Password.authenticate(context, input.getText().toString(), admin)){
                        for (RemboursementVente rembours: remboursements) {
                            if(!rembours.is_valid()) {
                                if (rembours.sign(context, admin))
                                    rembours.update(context);
                            }
                        }
                        context.refresh();
                    } else {
                        Toast.makeText(context, "mot de passe incorrect", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            builder.setNegativeButton("Reka", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        }
    }
    public void addToTotals(Double prix) {
        setTot(vente_tot + prix);
    }
    private void chargerRembours() {
        try {
            Dao dao_as = new InkoranyaMakuru(context).getDao(RemboursementVente.class);
            remboursements = (ArrayList<RemboursementVente>) dao_as.queryForAll();
            adaptateur.setData(remboursements);
        } catch (SQLException e) {
            Toast.makeText(context, "Erreur de connection à la base", Toast.LENGTH_LONG).show();
        }
    }
    public void refresh() {
        chargerRembours();
    }

    @Override
    public void performFiltering(Boolean dette, Boolean perimee) {
//        ArrayList<RemboursementVente> filtered = new ArrayList<>();
//        for (RemboursementVente as : ventes){
//            if(dette && as.getReste()>0) {
//                filtered.add(as);
//            }
//        }
//        adaptateur.setData(filtered);
    }

    @Override
    public void cancelFiltering() {
        adaptateur.setData(ventes);
        return;
    }
}
