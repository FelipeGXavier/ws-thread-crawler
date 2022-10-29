package com.example.threadcrawler.entity;

public class Product {

    private final String title;
    private final String url;
    private final String price;

    public Product(String title, String url, String price) {
        this.title = title;
        this.url = url;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getPrice() {
        return price;
    }
}
