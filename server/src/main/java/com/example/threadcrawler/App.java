package com.example.threadcrawler;

import com.example.threadcrawler.core.RunningStats;
import com.example.threadcrawler.core.ShopSearchThread;
import com.example.threadcrawler.entity.SearchRequest;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@SpringBootApplication
public class App {

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context = SpringApplication.run(App.class, args);
        /*testBenchmarkConcurrent(context);
        testBenchmarkSequential(context);*/
    }

    public static void testBenchmarkConcurrent(ConfigurableApplicationContext context) throws InterruptedException {
        String[] terms = {"tv", "pc", "hd"};
        var request = new SearchRequest("colombo", terms);
        var stats = new RunningStats(context.getBean(SimpMessagingTemplate.class));
        var t = new ShopSearchThread(stats, request);
        long start = System.currentTimeMillis();
        t.start();
        t.join();
        long end = System.currentTimeMillis();
        System.out.println("Concorrente: " + (end - start));
    }
    public static void testBenchmarkSequential(ConfigurableApplicationContext context) throws Exception {
        String[] terms = {"tv", "pc", "hd"};
        var request = new SearchRequest("colombo", terms);
        var stats = new RunningStats(context.getBean(SimpMessagingTemplate.class));
        long start = System.currentTimeMillis();
        new SequentialTest(stats, request).run();
        long end = System.currentTimeMillis();
        System.out.println("Sequencial: " + (end - start));
    }

}
