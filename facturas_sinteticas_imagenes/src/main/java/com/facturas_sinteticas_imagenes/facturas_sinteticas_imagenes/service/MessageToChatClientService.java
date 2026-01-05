package com.facturas_sinteticas_imagenes.facturas_sinteticas_imagenes.service;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;

import com.facturas_sinteticas_imagenes.facturas_sinteticas_imagenes.utils.ExtensionUtils;

@Service
public class MessageToChatClientService {
	
	private static final Logger log = LoggerFactory.getLogger(MessageToChatClientService.class);
	
	public List<Message> buildMessage(String prompt, Map<String,byte[]> files) {
		List<String> fileInString= files.entrySet().stream().map(file -> {
		            MimeType mimeType = ExtensionUtils.getMapMimeType(file.getKey());
		            if (Objects.nonNull(mimeType) && mimeType.getType().equals("text")) {
		                return  
		                    "\n\n--- FILE: " + file.getKey() + " (" + mimeType.getSubtype() + ") ---\n" + 
		                    new String(file.getValue(), StandardCharsets.UTF_8) + 
		                    "\n--- END OF FILE ---\n";
		            }
		            return "";
		        }).collect(Collectors.toList());

		  
        StringBuilder fullText = new StringBuilder(prompt);
        
        for (Object content : fileInString) {
            if (content instanceof String textPart) {
                fullText.append(textPart); 
            }
        }
        UserMessage userMessage = UserMessage.builder()
                .text(fullText.toString())
                .build();
	       
        return List.of(userMessage);
	}
	
}
