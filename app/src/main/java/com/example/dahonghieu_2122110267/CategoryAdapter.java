package com.example.dahonghieu_2122110267;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.VH> {

    public interface OnClick { void onClick(CategoryItem c); }

    private final List<CategoryItem> data;
    private final OnClick onClick;
    private int selectedPos = 0; // mặc định chọn "Tất cả"

    public CategoryAdapter(List<CategoryItem> data, OnClick onClick) {
        this.data = data; this.onClick = onClick;
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        CategoryItem c = data.get(pos);
        h.tv.setText(c.getTitle());

        Glide.with(h.img.getContext())
                .load(c.getImage())
                .placeholder(R.drawable.prod_1)
                .into(h.img);

        boolean selected = (pos == selectedPos);
        h.itemView.setSelected(selected);
        ((View) h.img.getParent()).setSelected(selected); // khung tròn
        h.tv.setSelected(selected);

        h.itemView.setOnClickListener(v -> {
            int old = selectedPos;
            selectedPos = h.getAdapterPosition();
            notifyItemChanged(old);
            notifyItemChanged(selectedPos);
            if (onClick != null) onClick.onClick(c);
        });
    }

    @Override
    public int getItemCount() { return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        ImageView img; TextView tv;
        VH(@NonNull View v) {
            super(v);
            img = v.findViewById(R.id.imgCat);
            tv  = v.findViewById(R.id.tvCatName);
        }
    }
}
