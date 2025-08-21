package com.example.dahonghieu_2122110267;

public class CategoryItem {
    private final String id;
    private final String title;
    private final String image;

    public CategoryItem(String id, String title, String image) {
        this.id = id;
        this.title = title;
        this.image = image;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getImage() { return image; }
}
