package com.example.threadcrawler.controller;

import com.example.threadcrawler.entity.Message;
import com.example.threadcrawler.service.ExampleThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

@Controller
public class ScrapperController {

    @Autowired
    SimpMessagingTemplate template;

    @MessageMapping("/links.start")
    public String sendMessage(@Payload Message message) {
        System.out.println(message);
        new ExampleThread(this.template).start();
        return "Teste";
    }

    /*@MessageMapping("/links.user")
    @SendTo("/topic/public")
    public Message addUser(@Payload Message message, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", message.getUser());
        message.setTime(ApplicationUtils.getTime());
        return message;
    }*/

    @Scheduled(fixedDelay = 1000)
    public void sendAdhocMessage() {
        //template.convertAndSend("/topic/stats", 0);
    }
}
