package com.example.dahonghieu_2122110267;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;

public class ProductDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_product_detail);

        ImageView img   = findViewById(R.id.imgProduct);
        TextView tvName = findViewById(R.id.tvName);
        TextView tvPrice= findViewById(R.id.tvPrice);
        TextView tvDesc = findViewById(R.id.tvDescription);
        Button btnAdd   = findViewById(R.id.btnAddToCart);
        Button btnGoCart= findViewById(R.id.btnGoCart);

        String id    = getIntent().getStringExtra("id");
        String name  = getIntent().getStringExtra("name");
        double price = getIntent().getDoubleExtra("price", 0);
        String image = getIntent().getStringExtra("image");
        String desc  = getIntent().getStringExtra("description");

        tvName.setText(name);
        tvPrice.setText(new DecimalFormat("#,###đ").format(price));
        tvDesc.setText(desc != null && !desc.isEmpty() ? desc : "Không có mô tả");

        Glide.with(this).load(image).placeholder(R.drawable.prod_1).into(img);

        btnAdd.setOnClickListener(v -> {
            CartManager.get().add(new ProductItem(id, name, price, image, "", desc));
            Toast.makeText(this, "Đã thêm vào giỏ", Toast.LENGTH_SHORT).show();
        });

        btnGoCart.setOnClickListener(v -> CartActivity.open(this));
    }
}
