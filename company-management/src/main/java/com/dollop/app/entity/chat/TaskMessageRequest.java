package com.dollop.app.entity.chat;

import java.util.List;

import com.dollop.app.entity.payload.chat.ChatFileResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskMessageRequest {
	  
	  //   private Long messageId;
		
	    private Integer  groupId;
	 
	    private String message;
	    
//	    private String sent;
//	 
//	    private String conversationId;
//		
//		private List<ChatFileResponse> chatFiles;
//		
//		private List<ChatFileResponse> images;
}
