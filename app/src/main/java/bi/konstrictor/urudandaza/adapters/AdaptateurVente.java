package bi.konstrictor.urudandaza.adapters;

import android.content.DialogInterface;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import bi.konstrictor.urudandaza.InkoranyaMakuru;
import bi.konstrictor.urudandaza.R;
import bi.konstrictor.urudandaza.VenteActivity;
import bi.konstrictor.urudandaza.models.ActionStock;
import bi.konstrictor.urudandaza.models.Produit;

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
        holder.view.clearFocus();
        try {
            Double qtt = context.getCartItem(produit).getQuantite();
            holder.field_vente_qtt.setText(qtt.toString());
        }catch (Exception e){
            holder.field_vente_qtt.setText("0");
        }
        holder.lbl_card_vente_prix.setText(produit.prix.toString());
        holder.btn_vente_qtt_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.view.requestFocus();
                Double qtt = Double.parseDouble(holder.field_vente_qtt.getText().toString());
                if(qtt+1<=produit.quantite && produit.prix>0){
                    qtt++;
                    holder.field_vente_qtt.setText(qtt.toString());
                    addTocart(produit, qtt);
                }
            }
        });
        holder.btn_vente_qtt_moins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.view.requestFocus();
                Double qtt = Double.parseDouble(holder.field_vente_qtt.getText().toString());
                if(qtt>0  && produit.prix>0){
                    qtt--;
                    holder.field_vente_qtt.setText(qtt.toString());
                    addTocart(produit, qtt);
                }else{
                    context.removeFromCart(produit);
                }
            }
        });
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.view.requestFocus();
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
                        produit.update(context);
                        context.refresh();
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
        if(context.isINTEGER_MODE()){
            holder.field_vente_qtt.setFocusable(false);
            holder.field_vente_qtt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { }
                @Override
                public void afterTextChanged(Editable s) {
                    String str_new_val = s.toString().trim();
                    if (str_new_val.isEmpty()) {
                        context.removeFromCart(produit);
                        return;
                    }
                    Double new_val = Double.parseDouble(str_new_val);
                    if (new_val < 0 | new_val > produit.quantite) {
                        context.removeFromCart(produit);
                        return;
                    }
                    addTocart(produit, new_val);
                }
            });
        }else{
            holder.field_vente_qtt.setFocusable(true);
            holder.field_vente_qtt.setFocusableInTouchMode(true);
        }
    }
    private void addTocart(Produit produit, Double quantite) {
        if(quantite<=0) {
            context.removeFromCart(produit);
            return;
        }
        ActionStock as = new ActionStock();
        as.kudandaza(produit, quantite, null, null, new InkoranyaMakuru(context).getLatestCloture());
        context.addToCart(as);
    }

    @Override
        public int getItemCount() {
            return stocks.size();
        }

        public void setData(ArrayList<Produit> produit) {
            this.stocks = produit;
            notifyDataSetChanged();
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
