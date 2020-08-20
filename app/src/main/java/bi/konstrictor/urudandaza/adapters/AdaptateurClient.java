package bi.konstrictor.urudandaza.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import bi.konstrictor.urudandaza.ClientActivity;
import bi.konstrictor.urudandaza.DetailHistActivity;
import bi.konstrictor.urudandaza.R;
import bi.konstrictor.urudandaza.dialogs.ClientForm;
import bi.konstrictor.urudandaza.models.Personne;

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
        final Personne client = clients.get(position);
        holder.lbl_card_client_name.setText(client.nom);
        if((client.autres!=null) && (!client.autres.trim().isEmpty()))
            holder.lbl_card_client_infos.setText(client.autres);
        if((client.phone!=null) && (!client.phone.trim().isEmpty()))
            holder.lbl_card_client_tel.setText(client.phone);
        holder.btn_card_client_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClientForm form = new ClientForm(context);
                form.setClient(client);
                form.show();
            }
        });
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.button_fadein));
                Intent intent = new Intent(context, DetailHistActivity.class);
                intent.putExtra("filtre", "personne_id");
                intent.putExtra("valeur", client.id.toString());
                context.startActivity(intent);
            }
        });
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
