package com.example.threadcrawler;

import com.example.threadcrawler.entity.Product;
import com.example.threadcrawler.entity.SearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    SimpMessagingTemplate template;

    public synchronized void increment() {
        System.out.println("Incremento: " + this.runningSearches);
        this.runningSearches++;
    }

    public void finishTerm(String term, String shop) {
        System.out.println("Finish term " + term + " " + shop);
    }

    public synchronized void finish(SearchRequest request, List<Product> products) {
        long endTime = System.currentTimeMillis();
        long runningTime = (endTime - this.startTime) / 1000;
        System.out.println(runningTime);

    }
}
