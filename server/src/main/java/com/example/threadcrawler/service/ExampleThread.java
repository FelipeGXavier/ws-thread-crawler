package com.example.threadcrawler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


public class ExampleThread extends Thread {

    private SimpMessagingTemplate template;

    public ExampleThread(SimpMessagingTemplate template) {
        this.template = template;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.template.convertAndSend("/topic/link.finish", "Finished");
    }
}
