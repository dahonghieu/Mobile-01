package com.example.dahonghieu_2122110267;

import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class CheckoutActivity extends AppCompatActivity {

    // Dùng đúng project bạn cung cấp
    private static final String ORDERS_URL =
            "https://68a68abb639c6a54e99f0259.mockapi.io/orders";

    private RecyclerView rvCheckout;
    private TextView tvTotal;
    private EditText edtName, edtPhone, edtAddress;
    private RadioGroup rgMethod;
    private RadioButton rbCOD, rbOnline;
    private Button btnOrder;

    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_checkout);

        // Ánh xạ ID — NHỚ đúng ID trong activity_checkout.xml
        rvCheckout = findViewById(R.id.rvCheckout);
        tvTotal    = findViewById(R.id.tvTotal);
        edtName    = findViewById(R.id.edtName);
        edtPhone   = findViewById(R.id.edtPhone);
        edtAddress = findViewById(R.id.edtAddress);
        rgMethod   = findViewById(R.id.rgMethod);
        rbCOD      = findViewById(R.id.rbCOD);
        rbOnline   = findViewById(R.id.rbOnline);
        btnOrder   = findViewById(R.id.btnOrder);

        // Hiện giỏ ra để chắc chắn Activity đang đúng layout
        rvCheckout.setLayoutManager(new LinearLayoutManager(this));
        rvCheckout.setAdapter(new CartAdapter(CartManager.get().getItems()));

        double total = CartManager.get().getTotal();
        tvTotal.setText("Tổng: " + new DecimalFormat("#,### đ").format(total));

        queue = Volley.newRequestQueue(this);

        // Ping GET để báo nếu endpoint sai
        pingOrders();

        btnOrder.setOnClickListener(v -> {
            Toast.makeText(this, "Đã bấm nút ĐẶT HÀNG", Toast.LENGTH_SHORT).show(); // ✅ nhìn thấy ngay khi bấm
            if (!validate()) return;
            postOrder();
        });
    }

    private boolean validate() {
        if (edtName.getText().toString().trim().isEmpty()
                || edtPhone.getText().toString().trim().isEmpty()
                || edtAddress.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập Họ tên / SĐT / Địa chỉ", Toast.LENGTH_LONG).show();
            return false;
        }
        if (CartManager.get().getItems().isEmpty()) {
            Toast.makeText(this, "Giỏ hàng trống", Toast.LENGTH_LONG).show();
            return false;
        }
        Toast.makeText(this, "Validate OK — chuẩn bị gửi đơn", Toast.LENGTH_SHORT).show(); // ✅
        return true;
    }

    /** GET thử để kiểm tra endpoint hoạt động */
    private void pingOrders() {
        StringRequest getReq = new StringRequest(
                Request.Method.GET, ORDERS_URL,
                res -> Log.d("Checkout", "GET /orders OK, length=" + res.length()),
                err -> {
                    String msg = volleyErr(err);
                    Toast.makeText(this, "GET /orders lỗi: " + msg, Toast.LENGTH_LONG).show();
                    Log.e("Checkout", "GET /orders error: " + msg);
                }
        );
        getReq.setRetryPolicy(new DefaultRetryPolicy(8000, 1, 1f));
        queue.add(getReq);
    }

    /** POST đơn hàng — chỉ 6 field đúng như MockAPI của bạn */
    private void postOrder() {
        btnOrder.setEnabled(false);
        Toast.makeText(this, "Đang gửi đơn hàng...", Toast.LENGTH_SHORT).show(); // ✅

        try {
            String method = (rgMethod.getCheckedRadioButtonId() == R.id.rbCOD) ? "COD" : "ONLINE";
            String status = method.equals("COD") ? "PENDING" : "CREATED";

            JSONObject body = new JSONObject();
            body.put("customerName", edtName.getText().toString().trim());
            body.put("phone",        edtPhone.getText().toString().trim());
            body.put("address",      edtAddress.getText().toString().trim());
            body.put("paymentMethod",method);
            body.put("status",       status);
            body.put("total",        CartManager.get().getTotal()); // number

            final byte[] postData = body.toString().getBytes(StandardCharsets.UTF_8);

            StringRequest postReq = new StringRequest(
                    Request.Method.POST, ORDERS_URL,
                    res -> {
                        Toast.makeText(this, "Đặt hàng thành công!", Toast.LENGTH_LONG).show(); // ✅
                        CartManager.get().clear();
                        finish();
                    },
                    err -> {
                        String msg = volleyErr(err);
                        Toast.makeText(this, "POST /orders lỗi: " + msg, Toast.LENGTH_LONG).show(); // ✅
                        Log.e("Checkout", "POST /orders error: " + msg);
                        btnOrder.setEnabled(true);
                    }
            ) {
                @Override
                public byte[] getBody() throws AuthFailureError { return postData; }
                @Override
                public String getBodyContentType() { return "application/json; charset=UTF-8"; }
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> h = new HashMap<>();
                    h.put("Accept", "application/json");
                    return h;
                }
            };

            postReq.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1f));
            postReq.setShouldCache(false);
            queue.add(postReq);

        } catch (Exception e) {
            btnOrder.setEnabled(true);
            Toast.makeText(this, "Không tạo được body: " + e.getMessage(), Toast.LENGTH_LONG).show(); // ✅
            Log.e("Checkout", "build json error", e);
        }
    }

    private String volleyErr(com.android.volley.VolleyError err) {
        String msg = err.toString();
        if (err.networkResponse != null) {
            try {
                msg = "code=" + err.networkResponse.statusCode +
                        " body=" + new String(err.networkResponse.data, StandardCharsets.UTF_8);
            } catch (Exception ignore) {}
        }
        return msg;
    }
}
