package com.example.dahonghieu_2122110267;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Quản lý giỏ hàng dạng singleton */
public class CartManager {
    private static CartManager INSTANCE;
    public static CartManager get() {
        if (INSTANCE == null) INSTANCE = new CartManager();
        return INSTANCE;
    }

    /** Lưu theo productId -> CartItem (chứa sản phẩm + số lượng) */
    private final Map<String, CartItem> map = new LinkedHashMap<>();

    /** Thêm 1 sản phẩm (mặc định +1) */
    public void add(ProductItem p) {
        if (p == null || p.getId() == null) return;
        CartItem cur = map.get(p.getId());
        if (cur == null) {
            map.put(p.getId(), new CartItem(p, 1));
        } else {
            cur.qty += 1;
        }
    }

    /** Tăng/giảm số lượng theo productId; nếu qty <= 0 thì xóa */
    public void setQty(String productId, int qty) {
        if (productId == null) return;
        CartItem ci = map.get(productId);
        if (ci == null) return;
        if (qty <= 0) map.remove(productId);
        else ci.qty = qty;
    }

    /** Giảm 1 đơn vị; nếu còn 0 thì xóa */
    public void minusOne(String productId) {
        CartItem ci = map.get(productId);
        if (ci == null) return;
        if (ci.qty <= 1) map.remove(productId);
        else ci.qty -= 1;
    }

    /** Xóa hẳn 1 sản phẩm khỏi giỏ */
    public void remove(String productId) {
        if (productId != null) map.remove(productId);
    }

    /** Xóa toàn bộ giỏ */
    public void clear() { map.clear(); }

    /** Danh sách mục giỏ (mỗi sản phẩm 1 dòng + số lượng) */
    public List<CartItem> getItems() { return new ArrayList<>(map.values()); }

    /** Tổng tiền (đã nhân số lượng) */
    public double getTotal() {
        double sum = 0;
        for (CartItem c : map.values()) {
            sum += c.product.getPrice() * c.qty;
        }
        return sum;
    }

    /** Tổng số lượng item (cộng dồn) */
    public int getTotalQuantity() {
        int q = 0;
        for (CartItem c : map.values()) q += c.qty;
        return q;
    }

    /** Trả về list sản phẩm “phẳng” (lặp theo số lượng) – tiện cho adapter cũ nếu cần */
    public List<ProductItem> getProductsFlat() {
        List<ProductItem> out = new ArrayList<>();
        for (CartItem c : map.values()) {
            for (int i = 0; i < c.qty; i++) out.add(c.product);
        }
        return out;
    }

    /** Mục giỏ hàng */
    public static class CartItem {
        public final ProductItem product;
        public int qty;

        public CartItem(ProductItem product, int qty) {
            this.product = product;
            this.qty = qty;
        }
    }
}
