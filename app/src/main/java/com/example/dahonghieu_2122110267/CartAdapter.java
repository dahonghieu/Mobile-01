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

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartVH> {

    private final List<CartManager.CartItem> items;
    private final Runnable onChanged; // callback để activity cập nhật lại tổng tiền (có thể null)

    public CartAdapter(List<CartManager.CartItem> items) {
        this(items, null);
    }

    public CartAdapter(List<CartManager.CartItem> items, Runnable onChanged) {
        this.items = items;
        this.onChanged = onChanged;
    }

    @NonNull
    @Override
    public CartVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartVH(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartVH h, int pos) {
        CartManager.CartItem c = items.get(pos);

        // Tên + số lượng + thành tiền cho dòng đó
        h.name.setText(c.product.getName());
        h.qty.setText("x" + c.qty);
        h.price.setText(new DecimalFormat("#,### đ").format(c.product.getPrice() * c.qty));

        // Ảnh
        Glide.with(h.itemView.getContext())
                .load(c.product.getImage())
                .placeholder(R.drawable.prod_1)
                .into(h.img);

        // Xóa khỏi giỏ
        h.btnDelete.setOnClickListener(v -> {
            CartManager.get().remove(c.product.getId());
            int idx = h.getBindingAdapterPosition();
            if (idx != RecyclerView.NO_POSITION) {
                items.remove(idx);
                notifyItemRemoved(idx);
            } else {
                notifyDataSetChanged();
            }
            if (onChanged != null) onChanged.run();
        });
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class CartVH extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name, price, qty;
        ImageButton btnDelete;
        CartVH(@NonNull View v) {
            super(v);
            img       = v.findViewById(R.id.img);        // ✅ trùng item_cart.xml
            name      = v.findViewById(R.id.name);       // ✅
            price     = v.findViewById(R.id.price);      // ✅
            qty       = v.findViewById(R.id.qty);        // ✅
            btnDelete = v.findViewById(R.id.btnDelete);  // ✅
        }
    }
}
