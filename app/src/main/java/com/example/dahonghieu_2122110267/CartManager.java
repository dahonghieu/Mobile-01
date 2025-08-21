package com.example.dahonghieu_2122110267;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CartManager {
    private static CartManager INSTANCE;
    public static CartManager get() { if (INSTANCE == null) INSTANCE = new CartManager(); return INSTANCE; }

    private final Map<String, CartItem> map = new LinkedHashMap<>();

    public void add(ProductItem p) {
        if (p == null) return;
        CartItem cur = map.get(p.getId());
        if (cur == null) map.put(p.getId(), new CartItem(p, 1));
        else cur.qty++;
    }

    public void remove(String productId) { map.remove(productId); }

    public List<CartItem> getItems() { return new ArrayList<>(map.values()); }

    public double getTotal() {
        double sum = 0;
        for (CartItem c : map.values()) sum += c.product.getPrice() * c.qty;
        return sum;
    }

    public static class CartItem {
        public final ProductItem product;
        public int qty;
        public CartItem(ProductItem product, int qty) { this.product = product; this.qty = qty; }
    }
}
