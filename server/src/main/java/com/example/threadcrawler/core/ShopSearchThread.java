package com.example.threadcrawler.core;

import com.example.threadcrawler.entity.Product;
import com.example.threadcrawler.entity.SearchRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;


public class ShopSearchThread extends Thread {

    private final RunningStats stats;
    private final SearchRequest request;

    public ShopSearchThread(RunningStats stats, SearchRequest request) {
        this.stats = stats;
        this.request = request;
    }

    @Override
    public void run() {
        List<Future<List<Product>>> productsSearching = new ArrayList<>();
        var executor = Executors.newFixedThreadPool(10);
        for (var term : request.getTerms()) {
            var t = new TermSearchThread(this.request.getShop(), term, this.stats);
            Future<List<Product>> productsFromTerm = executor.submit(t);
            productsSearching.add(productsFromTerm);
        }
        var products = productsSearching.stream().flatMap(productsSearched -> {
            try {
                return productsSearched.get().stream();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
        this.stats.finish(this.request, products);
    }
}
