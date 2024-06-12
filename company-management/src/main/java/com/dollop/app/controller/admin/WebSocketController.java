package com.dollop.app.controller.admin;
 

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@CrossOrigin("*")
public class WebSocketController {


	 @MessageMapping("/socket")
	    @SendTo("/queue/messages")
	    public String send(String message) {
	         return message;
	    }
	
}

