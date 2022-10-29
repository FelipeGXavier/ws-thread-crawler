package com.example.threadcrawler.core;

import com.example.threadcrawler.contracts.ShopScrapper;
import com.example.threadcrawler.entity.Product;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.List;

public class ColomboScrapper implements ShopScrapper {

    private final Document document;

    public ColomboScrapper(Document document) {
        this.document = document;
    }

    public Product getProductFromElement(Element element) throws IOException {
        String productName = element.selectFirst("div.nm-product-name").text();
        String productUrl = "http:" + element.selectFirst("a").attr("href");
        String productPrice = element.selectFirst("div.nm-currency-price").text();
        Document detail = Jsoup.connect(productUrl).get();
        String code = detail.selectFirst("p:contains(Cód)").text();
        return new Product(productName, productUrl, productPrice, code);
    }

    public List<Element> getElements() {
        return document.select("li[itemtype='http://schema.org/Product']");
    }
}
