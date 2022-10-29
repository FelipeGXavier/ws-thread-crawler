package com.example.threadcrawler.entity;

import java.util.Arrays;

public class SearchRequest {

    private final String shop;
    private final String[] terms;

    public SearchRequest(String shop, String[] terms) {
        this.shop = shop;
        this.terms = terms;
    }

    public String getShop() {
        return shop;
    }

    public String[] getTerms() {
        return terms;
    }

    @Override
    public String toString() {
        return "Message{" +
                "shop='" + shop + '\'' +
                ", terms=" + Arrays.toString(terms) +
                '}';
    }
}
