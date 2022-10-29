package com.example.threadcrawler.controller;

import com.example.threadcrawler.core.RunningStats;
import com.example.threadcrawler.entity.SearchRequest;
import com.example.threadcrawler.core.ShopSearchThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ScrapperController {

    @Autowired
    private SimpMessagingTemplate template;

    @MessageMapping("/links.start")
    public String sendMessage(@Payload SearchRequest request) {
        new ShopSearchThread(new RunningStats(this.template), request).start();
        return "Ok";
    }

}
