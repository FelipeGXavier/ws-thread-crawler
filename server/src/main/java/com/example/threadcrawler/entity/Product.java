package com.example.threadcrawler.entity;

public class Product {

    private final String title;
    private final String url;
    private final String price;
    private final String code;

    private final String shop;

    public Product(String title, String url, String price, String code, String shop) {
        this.title = title;
        this.url = url;
        this.price = price;
        this.code = code;
        this.shop = shop;
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

    public String getShop() {
        return shop;
    }
}
