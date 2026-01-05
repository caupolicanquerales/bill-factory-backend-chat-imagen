package com.facturas_sinteticas_imagenes.facturas_sinteticas_imagenes.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ExecutingPromptExtractTemplateService {
	
	private final ChatClient chatClient;
	private final MessageToChatClientService messageToChat;
	
	@Value(value="${event.name.chat.basic.template}")
	private String eventName;
	
	public ExecutingPromptExtractTemplateService(ChatClient.Builder chatClientBuilder,
			MessageToChatClientService messageToChat) {
		this.chatClient = chatClientBuilder.build();
		this.messageToChat=messageToChat;
	}
	
	public CompletableFuture<String> generateBasicTemplatAsync(String prompt, StoreFilesService storeFiles){
		Map<String,byte[]> files= Optional.ofNullable(storeFiles.getFileParts()).orElse(new HashMap<String,byte[]>());
		storeFiles.setFileParts(new HashMap<String,byte[]>());
		List<Message> listMessage = messageToChat.buildMessage(prompt, files);
		return CompletableFuture.supplyAsync(() -> {
			try {
		            return this.chatClient.prompt()
		                    .messages(listMessage)
		                    .call() 
		                    .content();
		            
		        } catch (Exception e) {
		            System.err.println("Async error generating basic template for prompt: " + prompt + " - " + e.getMessage());
		            throw new RuntimeException("Failed to generate image asynchronously.", e);
		        }
		        
		    });
	}
	
}
