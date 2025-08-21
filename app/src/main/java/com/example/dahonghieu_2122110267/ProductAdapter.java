package com.example.dahonghieu_2122110267;

import android.content.Context;
import android.content.Intent;
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

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.VH> {

    public interface OnAddToCart { void onAdd(ProductItem p); }

    private final Context ctx;
    private final List<ProductItem> data;
    private final OnAddToCart onAdd;

    public ProductAdapter(Context ctx, List<ProductItem> data, OnAddToCart onAdd) {
        this.ctx = ctx; this.data = data; this.onAdd = onAdd;
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.item_product, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        ProductItem p = data.get(pos);
        h.txtName.setText(p.getName());
        h.txtPrice.setText(new DecimalFormat("#,###đ").format(p.getPrice()));
        Glide.with(ctx).load(p.getImage()).placeholder(R.drawable.prod_1).into(h.img);

        h.btnAdd.setOnClickListener(v -> { if (onAdd != null) onAdd.onAdd(p); });

        h.btnDetail.setOnClickListener(v -> {
            Intent it = new Intent(ctx, ProductDetailActivity.class);
            it.putExtra("id", p.getId());
            it.putExtra("name", p.getName());
            it.putExtra("price", p.getPrice());
            it.putExtra("image", p.getImage());
            it.putExtra("description", p.getDescription()); // 👈 gửi mô tả
            ctx.startActivity(it);
        });
    }

    @Override
    public int getItemCount() { return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        ImageView img; TextView txtName, txtPrice;
        ImageButton btnAdd; View btnDetail;

        VH(@NonNull View v) {
            super(v);
            img       = v.findViewById(R.id.imgProduct);
            txtName   = v.findViewById(R.id.txtProductName);
            txtPrice  = v.findViewById(R.id.txtProductPrice);
            btnAdd    = v.findViewById(R.id.btnAddToCart);
            btnDetail = v.findViewById(R.id.btnDetail);
        }
    }
}
