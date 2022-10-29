package com.example.threadcrawler.service;

import com.example.threadcrawler.RunningStats;
import com.example.threadcrawler.entity.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class TermSearchThread implements Callable<List<Product>> {

    private final RunningStats stats;
    private final String shop;
    private final String term;

    public TermSearchThread(String shop, String term, RunningStats stats) {
        this.shop = shop;
        this.term = term;
        this.stats = stats;
    }

    @Override
    public List<Product> call() throws Exception {
        for (int i = 0; i < 5; i++) {
            try {
                Thread.sleep(1000);
                this.stats.increment();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        this.stats.finishTerm(this.term, this.shop);
        return new ArrayList<>();
    }
}
