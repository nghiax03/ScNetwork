package com.nghianguyen.scnetwork.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class ChatController {

    //add WebSecurityConfig
    @MessageMapping("/send")
    @SendTo("/topic/messages")
    public String sendMessage(String message){
        //you can save message to db here
        return message;
    }
}
