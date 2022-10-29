package com.example.threadcrawler.entity;

public class Product {

    private final String title;
    private final String url;
    private final String price;

    private final String code;

    public Product(String title, String url, String price, String code) {
        this.title = title;
        this.url = url;
        this.price = price;
        this.code = code;
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

    public String getCode() {
        return code;
    }
}
