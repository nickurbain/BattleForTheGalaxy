package com.bfg.backend.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.bfg.backend.model.Message;
import com.bfg.backend.model.OutputMessage;

@Controller
public class ChatController 
{
    @MessageMapping("/chat/{topic}")
    @SendTo("/topic/messages")
    public OutputMessage send(@DestinationVariable("topic") String topic,
			      Message message) throws Exception
    {
    	return new OutputMessage(message.getFrom(), message.getText(), topic);
    }
}
