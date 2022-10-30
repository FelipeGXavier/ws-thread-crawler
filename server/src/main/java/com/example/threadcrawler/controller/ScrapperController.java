package com.example.threadcrawler.controller;

import com.example.threadcrawler.core.RunningStats;
import com.example.threadcrawler.core.ShopSearchThread;
import com.example.threadcrawler.entity.SearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ScrapperController {

    @Autowired
    private SimpMessagingTemplate template;

    @MessageMapping("/links.start")
    public String sendMessage(@Payload List<SearchRequest> data)  {
        var stats = new RunningStats(this.template);
        for (var request : data) {
            new ShopSearchThread(stats, request).start();
        }
        return "Ok";
    }

}
