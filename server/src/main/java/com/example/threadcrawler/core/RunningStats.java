package com.example.threadcrawler.core;

import com.example.threadcrawler.entity.Product;
import com.example.threadcrawler.entity.SearchRequest;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RunningStats {
    private int runningSearches;
    private final long startTime = System.currentTimeMillis();

    private final SimpMessagingTemplate template;

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

    public synchronized void finish(SearchRequest request, List<Product> products) {
        long endTime = System.currentTimeMillis();
        long runningTime = (endTime - this.startTime) / 1000;
        this.template.convertAndSend("/topic/link.finish", new FinishObjectResponse(runningTime, products));
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
    private final long time;
    private final List<Product> products;

    FinishObjectResponse(long time, List<Product> products) {
        this.time = time;
        this.products = products;
    }

    public long getTime() {
        return time;
    }

    public List<Product> getProducts() {
        return products;
    }
}
