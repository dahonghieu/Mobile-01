package com.example.dahonghieu_2122110267;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;

import java.text.DecimalFormat;

public class CartActivity extends AppCompatActivity {

    public static void open(android.content.Context c) {
        c.startActivity(new Intent(c, CartActivity.class));
    }

    private CartListAdapter adapter;
    private TextView tvTotal;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_cart);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        tvTotal = findViewById(R.id.tvTotal);
        RecyclerView rv = findViewById(R.id.rvCart);
        Button btnBackHome = findViewById(R.id.btnBackHome);

        // Nút back ở toolbar: quay lại màn trước (Home/Chi tiết)
        toolbar.setNavigationOnClickListener(v ->
                getOnBackPressedDispatcher().onBackPressed());

        // List giỏ
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CartListAdapter(CartManager.get().getItems(), this::updateTotal);
        rv.setAdapter(adapter);

        updateTotal();

        // Nút quay về trang chủ (xóa stack trên Home nếu đã có)
        btnBackHome.setOnClickListener(v -> {
            Intent i = new Intent(CartActivity.this, HomeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i);
            finish();
        });
    }

    private void updateTotal() {
        tvTotal.setText("Tổng: " + new DecimalFormat("#,###đ").format(CartManager.get().getTotal()));
    }
}
