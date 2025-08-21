package com.example.dahonghieu_2122110267;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";

    // API của bạn
    private static final String CATEGORIES_URL = "https://68a68abb639c6a54e99f0259.mockapi.io/categories";
    private static final String PRODUCTS_URL   = "https://68931182c49d24bce86949c7.mockapi.io/home";

    private EditText edtSearch;
    private ImageView imgBanner;
    private RecyclerView rvCategory, rvProduct;
    private BottomNavigationView bottomNav;

    private final List<CategoryItem> categories   = new ArrayList<>();
    private final List<ProductItem>  allProducts = new ArrayList<>();
    private final List<ProductItem>  showing     = new ArrayList<>();

    private CategoryAdapter categoryAdapter;
    private ProductAdapter  productAdapter;

    private String selectedCategoryId = ""; // "" = Tất cả
    private RequestQueue queue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // match view
        edtSearch  = findViewById(R.id.edtSearch);
        imgBanner  = findViewById(R.id.imgBanner);
        rvCategory = findViewById(R.id.rvCategory);
        rvProduct  = findViewById(R.id.rvProduct);
        bottomNav  = findViewById(R.id.bottomNav);

        queue = Volley.newRequestQueue(this);

        // RecyclerView danh mục ngang
        rvCategory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        categoryAdapter = new CategoryAdapter(categories, c -> {
            selectedCategoryId = safeId(c.getId());
            applyFilters();
        });
        rvCategory.setAdapter(categoryAdapter);
        // spacing 8dp (nếu đã có CategorySpaceItem thì giữ, không có thì bỏ dòng dưới)


        // RecyclerView sản phẩm lưới 2 cột
        rvProduct.setLayoutManager(new GridLayoutManager(this, 2));
        productAdapter = new ProductAdapter(this, showing, p -> {
            CartManager.get().add(p);
            Toast.makeText(this, "Đã thêm " + p.getName() + " vào giỏ", Toast.LENGTH_SHORT).show();
        });
        rvProduct.setAdapter(productAdapter);

        // Search realtime
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
            @Override public void onTextChanged(CharSequence s, int st, int b, int c) {}
            @Override public void afterTextChanged(Editable s) { applyFilters(); }
        });

        // Bottom nav
        bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_cart) {
                CartActivity.open(this);
                return true;
            }
            return true;
        });

        // Tải dữ liệu
        fetchCategories();   // load danh mục từ API riêng
        fetchProducts();     // load sản phẩm
    }

    /** Gọi API /categories để dựng dải danh mục (title + image) */
    private void fetchCategories() {
        JsonArrayRequest req = new JsonArrayRequest(
                Request.Method.GET, CATEGORIES_URL, null,
                arr -> {
                    try {
                        categories.clear();
                        // Tab "Tất cả"
                        categories.add(new CategoryItem("", "Tất cả", ""));
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject o = arr.getJSONObject(i);
                            String id    = safeId(o.optString("id"));
                            String title = o.optString("title");
                            String img   = o.optString("image", "");
                            categories.add(new CategoryItem(id, title, img));
                        }
                        categoryAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        Log.e(TAG, "Parse categories error", e);
                    }
                },
                err -> Log.e(TAG, "API /categories error: " + err)
        );
        req.setRetryPolicy(new DefaultRetryPolicy(
                8000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        queue.add(req);
    }

    /** Gọi API /home (products) để lấy danh sách sản phẩm */
    private void fetchProducts() {
        JsonArrayRequest req = new JsonArrayRequest(
                Request.Method.GET, PRODUCTS_URL, null,
                this::parseProducts,
                err -> {
                    Log.e(TAG, "API /products error: " + err);
                    Toast.makeText(this, "API /products lỗi", Toast.LENGTH_SHORT).show();
                }
        );
        req.setRetryPolicy(new DefaultRetryPolicy(
                8000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        queue.add(req);
    }

    /** Parse products JSON và hiển thị */
    private void parseProducts(JSONArray arr) {
        try {
            allProducts.clear();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);

                String id    = o.optString("id");
                String name  = o.optString("name");
                String img   = o.optString("image");
                double price = o.optDouble("price", 0);
                String desc  = o.optString("description", "");

                // categoryId có thể là number/string -> chuẩn hóa về chuỗi số
                String catId = "";
                if (o.has("categoryId")) {
                    Object raw = o.opt("categoryId");
                    if (raw instanceof Number) catId = String.valueOf(((Number) raw).intValue());
                    else catId = o.optString("categoryId", "");
                }
                catId = safeId(catId);

                allProducts.add(new ProductItem(id, name, price, img, catId, desc));
            }

            // hiển thị lần đầu (tất cả)
            showing.clear();
            showing.addAll(allProducts);
            applyFilters();
            productAdapter.notifyDataSetChanged();

            Toast.makeText(this, "Tải " + allProducts.size() + " sản phẩm", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Parse products error", e);
            Toast.makeText(this, "Lỗi parse dữ liệu", Toast.LENGTH_SHORT).show();
        }
    }

    /** Lọc theo danh mục + search */
    private void applyFilters() {
        String key = edtSearch.getText().toString().trim().toLowerCase();
        showing.clear();
        for (ProductItem p : allProducts) {
            boolean okCate   = selectedCategoryId.isEmpty()
                    || selectedCategoryId.equals(safeId(p.getCategoryId()));
            boolean okSearch = key.isEmpty()
                    || (p.getName() != null && p.getName().toLowerCase().contains(key));
            if (okCate && okSearch) showing.add(p);
        }
        productAdapter.notifyDataSetChanged();
    }

    /** Chuẩn hóa id: chỉ giữ số */
    private String safeId(String raw) {
        if (raw == null) return "";
        String t = raw.trim();
        if (t.matches("^\\d+$")) return t;
        return t.replaceAll("[^0-9]", "");
    }
}
