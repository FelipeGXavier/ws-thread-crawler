package com.example.threadcrawler;

import com.example.threadcrawler.contracts.ShopScrapper;
import com.example.threadcrawler.core.*;
import com.example.threadcrawler.entity.Product;
import com.example.threadcrawler.entity.SearchRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class SequentialTest {

    private final RunningStats stats;
    private final SearchRequest request;

    public SequentialTest(RunningStats stats, SearchRequest request) {
        this.stats = stats;
        this.request = request;
    }

    public void run() throws Exception {
        List<List<Product>> productsSearching = new ArrayList<>();
        for (var term : request.getTerms()) {
            var termSearch = new SequentialTermSearch(this.request.getShop(), term, this.stats);
            List<Product> productsFromTerm = termSearch.call();
            try {
                // Prevent request denied
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            productsSearching.add(productsFromTerm);
        }
        var products = productsSearching.stream().flatMap(Collection::stream).collect(Collectors.toList());
        this.stats.finish(products);
    }
}

class SequentialTermSearch {

    private final RunningStats stats;
    private final String shop;
    private final String term;

    public SequentialTermSearch(String shop, String term, RunningStats stats) {
        this.shop = shop;
        this.term = term;
        this.stats = stats;
    }

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
        } else if (this.shop.toLowerCase().equals(ShopOption.GIRAFA.toString())) {
            Document document = Jsoup.connect(String.format("https://www.girafa.com.br/busca/?q=%s", "tv")).get();
            return new GirafaScrapper(document);
        }
        return null;
    }
}