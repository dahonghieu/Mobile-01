package com.example.dahonghieu_2122110267;

public class ProductItem {
    private final String id;
    private final String name;
    private final double price;
    private final String image;
    private final String categoryId;
    private final String description;   // 👈 thêm mô tả

    public ProductItem(String id, String name, double price,
                       String image, String categoryId, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.categoryId = categoryId;
        this.description = description;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getImage() { return image; }
    public String getCategoryId() { return categoryId; }
    public String getDescription() { return description; }
}
