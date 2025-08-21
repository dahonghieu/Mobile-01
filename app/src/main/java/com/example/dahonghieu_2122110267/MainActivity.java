package com.example.dahonghieu_2122110267;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    private MaterialButton btnLogin, btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ẩn ActionBar để màn cover full đẹp
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Ánh xạ nút (trùng với id trong activity_main.xml bạn đã làm)
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        // Điều hướng
        btnLogin.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, LoginActivity.class))
        );

        btnRegister.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, RegisterActivity.class))
        );
    }
}
