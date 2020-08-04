package bi.konstrictor.urudandaza.adapters;

import android.content.DialogInterface;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;

import bi.konstrictor.urudandaza.ClientActivity;
import bi.konstrictor.urudandaza.InkoranyaMakuru;
import bi.konstrictor.urudandaza.R;
import bi.konstrictor.urudandaza.VenteActivity;
import bi.konstrictor.urudandaza.models.Personne;
import bi.konstrictor.urudandaza.models.ProxyAction;

public class AdaptateurClient extends RecyclerView.Adapter<AdaptateurClient.ViewHolder> {

    private ClientActivity context;
    private ArrayList<Personne> clients;

    public AdaptateurClient(ClientActivity context, ArrayList<Personne> clients) {
        this.context = context;
        this.clients = clients;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_client, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Personne client = clients.get(position);
        holder.lbl_card_client_name.setText(client.nom);
        holder.lbl_card_client_infos.setText(client.autres);
        holder.lbl_card_client_tel.setText(client.phone);
    }

    @Override
    public int getItemCount() {
        return clients.size();
    }
    public void setData(ArrayList<Personne> produit) {
            this.clients = produit;
        }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView lbl_card_client_name, lbl_card_client_tel, lbl_card_client_infos;
        Button btn_card_client_edit;
        View view;

        public ViewHolder(final View itemView) {
            super(itemView);
            this.view = itemView;
            lbl_card_client_name = itemView.findViewById(R.id.lbl_card_client_name);
            lbl_card_client_tel = itemView.findViewById(R.id.lbl_card_client_tel);
            lbl_card_client_infos = itemView.findViewById(R.id.lbl_card_client_infos);
            btn_card_client_edit = itemView.findViewById(R.id.btn_card_client_edit);
        }
    }
}
