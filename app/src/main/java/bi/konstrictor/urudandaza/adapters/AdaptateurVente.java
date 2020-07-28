package bi.konstrictor.urudandaza.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

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
        holder.lbl_card_vente.setText(stocks.get(position).nom);
        holder.lbl_card_unite_out.setText(stocks.get(position).unite_sortant);
        holder.lbl_card_vente_mesure.setText(stocks.get(position).unite_sortant);
        holder.lbl_card_vente_qtt.setText(stocks.get(position).quantite.toString());
        holder.lbl_card_vente_prix.setText(stocks.get(position).prix.toString());
        holder.btn_card_edit.setVisibility(View.VISIBLE);
        holder.layout_card_edit.setVisibility(View.GONE);
        holder.btn_card_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFromCart(holder, stocks.get(position));
                holder.nbr_vente_qtt.setValue(0);
                holder.field_vente_qtt.setText("");
                removeFocus(holder.field_vente_qtt);
            }
        });
        holder.field_vente_qtt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    addFocus(v);
                }else{
                    removeFocus(v);
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
                        String str = input.getText().toString();
                        Toast.makeText(context, str, Toast.LENGTH_LONG).show();
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
        if(context.getINTEGER_MODE()){
            holder.nbr_vente_qtt.setVisibility(View.VISIBLE);
            holder.field_vente_qtt.setVisibility(View.GONE);
            holder.nbr_vente_qtt.setOnScrollListener(new NumberPicker.OnScrollListener() {
                @Override
                public void onScrollStateChange(NumberPicker view, int scrollState) {
                    if(scrollState==SCROLL_STATE_IDLE){
                        int new_val = view.getValue();
                        if (new_val>0 & new_val<=stocks.get(position).quantite){
                            addTocart(stocks.get(position), new_val*1.);
                        } else {
                            removeFromCart(holder, stocks.get(position));
                        }
                    }
                }
            });
        }else{
            holder.nbr_vente_qtt.setVisibility(View.GONE);
            holder.field_vente_qtt.setVisibility(View.VISIBLE);
            holder.field_vente_qtt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { }
                @Override
                public void afterTextChanged(Editable s) {
                    String str_new_val = s.toString().trim();
                    if (str_new_val.isEmpty()){
                        removeFromCart(holder, stocks.get(position));
                        return;
                    }
                    Double new_val = Double.parseDouble(str_new_val);
                    if (new_val<0 | new_val>stocks.get(position).quantite){
                        removeFromCart(holder, stocks.get(position));
                        return;
                    }
                    addTocart(stocks.get(position), new_val);
                }
            });
        }

        holder.btn_card_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.btn_card_edit.setVisibility(View.GONE);
                holder.layout_card_edit.setVisibility(View.VISIBLE);
                holder.field_vente_qtt.requestFocus();
            }
        });
    }
    private void removeFocus(View view){
        view.clearFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }
    private void addFocus(View view){
        view.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
    }
    private void removeFromCart(ViewHolder holder, Produit produit) {
        holder.btn_card_edit.setVisibility(View.VISIBLE);
        holder.layout_card_edit.setVisibility(View.GONE);
        context.removeFromCart(produit);
    }

    private void addTocart(Produit produit, Double quantite) {
        ActionStock as = new ActionStock(produit, quantite);
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
            TextView lbl_card_vente, lbl_card_vente_prix, lbl_card_unite_out, lbl_card_vente_mesure,
                    lbl_card_vente_qtt;
            Button btn_card_edit, btn_card_close;
            LinearLayout layout_card_edit;
            EditText field_vente_qtt;
            NumberPicker nbr_vente_qtt;
            Boolean IS_SELECTED = false;
            View view;

            public ViewHolder(final View itemView) {
                super(itemView);
                this.view = itemView;
                lbl_card_vente = itemView.findViewById(R.id.lbl_card_vente);
                lbl_card_vente_prix = itemView.findViewById(R.id.lbl_card_vente_prix);
                lbl_card_vente_mesure = itemView.findViewById(R.id.lbl_card_vente_mesure);
                lbl_card_unite_out = itemView.findViewById(R.id.lbl_card_unite_out);
                lbl_card_vente_qtt = itemView.findViewById(R.id.lbl_card_vente_qtt);
                btn_card_edit = itemView.findViewById(R.id.btn_card_edit);
                btn_card_close = itemView.findViewById(R.id.btn_card_close);
                layout_card_edit = itemView.findViewById(R.id.layout_card_edit);
                nbr_vente_qtt = itemView.findViewById(R.id.nbr_vente_qtt);
                field_vente_qtt = itemView.findViewById(R.id.field_vente_qtt);
                nbr_vente_qtt.setMaxValue(100);
                nbr_vente_qtt.setMinValue(0);
            }
        }
    }
