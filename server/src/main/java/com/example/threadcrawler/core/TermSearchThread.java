package com.example.threadcrawler.core;

import com.example.threadcrawler.contracts.ShopScrapper;
import com.example.threadcrawler.entity.Product;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
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
        ShopScrapper shopScrapper = this.getScrapper();
        List<Element> elements = shopScrapper.getElements();
        List<Product> products = new ArrayList<>();
        int threshold = elements.size() < 15 ? 0 : 15;
        for (int i = 0; i < threshold; i++) {
            products.add(shopScrapper.getProductFromElement(elements.get(i)));
            this.stats.increment();
        }
        this.stats.finishTerm(this.term, this.shop);
        return products;
    }

    private ShopScrapper getScrapper() throws IOException {
        if (this.shop.toLowerCase().equals(ShopOption.COLOMBO.toString())) {
            Document document = Jsoup.connect(String.format("https://pesquisa.colombo.com.br/busca?q=%s&televendas=0", this.term)).get();
            return new ColomboScrapper(document);
        } else if (this.shop.toLowerCase().equals(ShopOption.PONTOFRIO.toString())) {
            //return "https://pesquisa.colombo.com.br/busca?q=%s&televendas=0";
        }
        return null;
    }

}
