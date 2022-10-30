package com.example.threadcrawler.core;

import com.example.threadcrawler.entity.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RunningStats {
    private int runningSearches;
    private final long startTime = System.currentTimeMillis();
    private final SimpMessagingTemplate template;

    private final Logger logger = LoggerFactory.getLogger(RunningStats.class);

    public RunningStats(SimpMessagingTemplate template) {
        this.template = template;
    }

    public synchronized void increment() {
        ++this.runningSearches;
        this.template.convertAndSend("/topic/link.stats.count", this.runningSearches);
    }

    public void finishTerm(String term, String shop) {
        this.template.convertAndSend("/topic/link.stats.term", new TermObjectResponse(term, shop));
    }

    public synchronized void finish(List<Product> products) {
        long endTime = System.currentTimeMillis();
        long runningTime = (endTime - this.startTime) / 1000;
        logger.debug("Running time {}", runningTime);
        this.template.convertAndSend("/topic/link.finish", new FinishObjectResponse(products));
    }

}

class TermObjectResponse {
    private final String term;
    private final String shop;

    TermObjectResponse(String term, String shop) {
        this.term = term;
        this.shop = shop;
    }

    public String getTerm() {
        return term;
    }

    public String getShop() {
        return shop;
    }
}

class FinishObjectResponse {
    private final List<Product> products;

    FinishObjectResponse(List<Product> products) {
        this.products = products;
    }


    public List<Product> getProducts() {
        return products;
    }
}
