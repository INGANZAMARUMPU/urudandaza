package bi.konstrictor.urudandaza;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import bi.konstrictor.urudandaza.dialogs.KuranguraForm;
import bi.konstrictor.urudandaza.dialogs.ProductForm;
import bi.konstrictor.urudandaza.models.ActionStock;
import bi.konstrictor.urudandaza.models.Produit;

class AdaptateurVente extends RecyclerView.Adapter<AdaptateurVente.ViewHolder> {

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
            holder.lbl_card_vente_prix.setText(stocks.get(position).prix.toString());
            holder.btn_card_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeFromCart(holder, stocks.get(position));
                    holder.nbr_vente_qtt.setValue(0);
                    holder.field_vente_qtt.setText("");
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
                            if (new_val>0){
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
                        if (new_val<=0){
                            removeFromCart(holder, stocks.get(position));
                            return;
                        }
                        addTocart(stocks.get(position), new_val);
                    }
                });
            }
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.button_fadein));
//                    if(context.EDITION) {
//                    }
                }
            });
        }

    private void removeFromCart(ViewHolder holder, Produit produit) {
        holder.btn_card_edit.setVisibility(View.VISIBLE);
        holder.layout_card_edit.setVisibility(View.GONE);
        context.removeFromCart(produit);
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
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
            TextView lbl_card_vente, lbl_card_vente_prix, lbl_card_unite_out;
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
                lbl_card_unite_out = itemView.findViewById(R.id.lbl_card_unite_out);
                btn_card_edit = itemView.findViewById(R.id.btn_card_edit);
                btn_card_close = itemView.findViewById(R.id.btn_card_close);
                layout_card_edit = itemView.findViewById(R.id.layout_card_edit);
                nbr_vente_qtt = itemView.findViewById(R.id.nbr_vente_qtt);
                field_vente_qtt = itemView.findViewById(R.id.field_vente_qtt);

                btn_card_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btn_card_edit.setVisibility(View.GONE);
                        layout_card_edit.setVisibility(View.VISIBLE);
                        if(field_vente_qtt.getVisibility()==View.VISIBLE) {
                            field_vente_qtt.requestFocus();
                            field_vente_qtt.setFocusable(true);
                            field_vente_qtt.setFocusableInTouchMode(true);
                            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                        }
                    }
                });
                nbr_vente_qtt.setMaxValue(100);
                nbr_vente_qtt.setMinValue(0);
            }
        }
    }
