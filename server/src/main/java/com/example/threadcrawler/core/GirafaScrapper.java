package com.example.threadcrawler.core;

import com.example.threadcrawler.contracts.ShopScrapper;
import com.example.threadcrawler.entity.Product;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.List;

public class GirafaScrapper implements ShopScrapper {

    private final Document document;

    public GirafaScrapper(Document document) {
        this.document = document;
    }

    public Product getProductFromElement(Element element) throws IOException, InterruptedException {
        String productName = element.select("a.informacao-produto > h2").first().text();
        String productUrl =  "https://www.girafa.com.br" + element.selectFirst("a").attr("href");
        String productPrice = element.selectFirst("span.valor-vista").text();
        Thread.sleep(500);
        Document detail = Jsoup.connect(productUrl).get();
        // Prevent request denied
        String codeValue = "-";
        Element code = detail.selectFirst("div.cod_produto");
        if (code != null) {
            codeValue = code.text();
        }
        return new Product(productName, productUrl, productPrice, codeValue, "Girafa");
    }

    public List<Element> getElements() {
        return document.select("div.box-produto");
    }
}
