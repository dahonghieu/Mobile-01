package com.example.dahonghieu_2122110267;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.text.DecimalFormat;
import java.util.List;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.Holder> {

    public interface OnChanged { void onChanged(); }

    private final List<CartManager.CartItem> items;
    private final OnChanged onChanged;

    public CartListAdapter(List<CartManager.CartItem> items, OnChanged onChanged) {
        this.items = items;
        this.onChanged = onChanged;
    }

    @NonNull @Override public Holder onCreateViewHolder(@NonNull ViewGroup p, int v) {
        View view = LayoutInflater.from(p.getContext()).inflate(R.layout.item_cart, p, false);
        return new Holder(view);
    }

    @Override public void onBindViewHolder(@NonNull Holder h, int pos) {
        CartManager.CartItem c = items.get(pos);
        Glide.with(h.itemView).load(c.product.getImage()).placeholder(R.drawable.prod_1).into(h.img);
        h.name.setText(c.product.getName());
        h.price.setText(new DecimalFormat("#,###đ").format(c.product.getPrice()));
        h.qty.setText("x" + c.qty);

        h.btnDelete.setOnClickListener(v -> {
            CartManager.get().remove(c.product.getId());
            items.remove(pos);
            notifyItemRemoved(pos);
            if (onChanged != null) onChanged.onChanged();
        });
    }

    @Override public int getItemCount() { return items.size(); }

    static class Holder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name, price, qty;
        ImageButton btnDelete;
        Holder(@NonNull View v) {
            super(v);
            img = v.findViewById(R.id.img);
            name = v.findViewById(R.id.name);
            price = v.findViewById(R.id.price);
            qty = v.findViewById(R.id.qty);
            btnDelete = v.findViewById(R.id.btnDelete);
        }
    }
}
