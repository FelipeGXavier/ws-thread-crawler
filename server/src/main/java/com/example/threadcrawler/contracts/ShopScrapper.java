package com.example.threadcrawler.contracts;

import com.example.threadcrawler.entity.Product;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.List;

public interface ShopScrapper {

     Product getProductFromElement(Element element) throws IOException, InterruptedException;

     List<Element> getElements();
}
