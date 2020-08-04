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

import bi.konstrictor.urudandaza.InkoranyaMakuru;
import bi.konstrictor.urudandaza.R;
import bi.konstrictor.urudandaza.VenteActivity;
import bi.konstrictor.urudandaza.models.ActionStock;
import bi.konstrictor.urudandaza.models.Produit;
import bi.konstrictor.urudandaza.models.ProxyAction;

public class AdaptateurVente extends RecyclerView.Adapter<AdaptateurVente.ViewHolder> {

    private VenteActivity context;
    private ArrayList<Produit> stocks;

    public AdaptateurVente(VenteActivity context, ArrayList<Produit> stocks) {
        this.context = context;
        this.stocks = stocks;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_vente, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Produit produit = stocks.get(position);
        holder.lbl_card_vente.setText(produit.nom);
        try {
            Double qtt = context.getCartItem(produit).quantite;
            holder.field_vente_qtt.setText(qtt.toString());
        }catch (Exception e){
            holder.field_vente_qtt.setText("0");
        }
        holder.lbl_card_vente_prix.setText(produit.prix.toString());
        holder.btn_vente_qtt_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.button_zoomin));
                Double qtt = Double.parseDouble(holder.field_vente_qtt.getText().toString());
                if(qtt+1<=produit.quantite){
                    qtt++;
                    holder.field_vente_qtt.setText(qtt.toString());
                    addTocart(produit, qtt);
                }
            }
        });
        holder.btn_vente_qtt_moins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.button_zoomout));
                Double qtt = Double.parseDouble(holder.field_vente_qtt.getText().toString());
                if(qtt>0){
                    qtt--;
                    holder.field_vente_qtt.setText(qtt.toString());
                    addTocart(produit, qtt);
                }else{
                    context.removeFromCart(produit);
                }
            }
        });
        holder.lbl_card_vente_prix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.button_fadein));
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Igiciro gishasha");
                final EditText input = new EditText(context);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setView(input);

                builder.setPositiveButton("Sawa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Double prix = Double.parseDouble(input.getText().toString());
                        produit.prix = prix;
                        InkoranyaMakuru inkoranyaMakuru = new InkoranyaMakuru(context);
                        try {
                            Dao<Produit, Integer> dao_protuit = inkoranyaMakuru.getDaoProduit();
                            dao_protuit.update(produit);
                            context.refresh();
                        } catch (SQLException e) {
                            Log.i("ERREUR", e.getMessage());
                            e.printStackTrace();
                            Toast.makeText(context, "Hari ikintu kutagenze neza", Toast.LENGTH_LONG).show();
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
        });
        holder.field_vente_qtt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                String str_new_val = s.toString().trim();
                if (str_new_val.isEmpty()){
                    context.removeFromCart(produit);
                    return;
                }
                Double new_val = Double.parseDouble(str_new_val);
                if (new_val<0 | new_val>produit.quantite){
                    context.removeFromCart(produit);
                    return;
                }
                addTocart(produit, new_val);
            }
        });
        if(context.isINTEGER_MODE()){
            holder.field_vente_qtt.setFocusable(false);
        }else{
            holder.field_vente_qtt.setFocusable(true);
            holder.field_vente_qtt.setFocusableInTouchMode(true);
        }
    }
    private void addTocart(Produit produit, Double quantite) {
        ProxyAction as = new ProxyAction();
        as.kudandaza(produit, quantite, new InkoranyaMakuru(context).getLatestCloture());
        context.addToCart(as);
    }

    @Override
        public int getItemCount() {
            return stocks.size();
        }

        public void setData(ArrayList<Produit> produit) {
            this.stocks = produit;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView lbl_card_vente, lbl_card_vente_prix;
            Button btn_vente_qtt_moins, btn_vente_qtt_plus;
            EditText field_vente_qtt;
            View view;

            public ViewHolder(final View itemView) {
                super(itemView);
                this.view = itemView;
                lbl_card_vente = itemView.findViewById(R.id.lbl_card_vente);
                lbl_card_vente_prix = itemView.findViewById(R.id.lbl_card_vente_prix);
                field_vente_qtt = itemView.findViewById(R.id.field_vente_qtt);
                btn_vente_qtt_moins = itemView.findViewById(R.id.btn_vente_qtt_moins);
                btn_vente_qtt_plus = itemView.findViewById(R.id.btn_vente_qtt_plus);

            }
        }
    }
