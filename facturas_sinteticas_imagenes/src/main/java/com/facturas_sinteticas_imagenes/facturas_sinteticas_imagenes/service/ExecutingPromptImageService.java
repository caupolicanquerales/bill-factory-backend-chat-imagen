package com.facturas_sinteticas_imagenes.facturas_sinteticas_imagenes.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class ExecutingPromptImageService {
	
	private final ImageModel imageModel;
	
	@Value(value="${event.name.image}")
	private String eventName;
	
	@Value(value="${image.model.name}")
	private String imageModelName;
	
	public ExecutingPromptImageService(@Qualifier("customOpenAiImageModel") ImageModel imageModel) {
		this.imageModel=imageModel;
	}
	
	
	public CompletableFuture<String> generateImageAsync(String prompt) {
	        
	    return CompletableFuture.supplyAsync(() -> {
	        var options = OpenAiImageOptions.builder()
		        //.responseFormat("b64_json")
		        .model(imageModelName)
		        .build();
		        
	        ImagePrompt imagePrompt = new ImagePrompt(prompt, options);
	        try {
	            ImageResponse response = imageModel.call(imagePrompt);
	            return response.getResults().get(0).getOutput().getB64Json();
	            
	        } catch (Exception e) {
	            System.err.println("Async error generating image for prompt: " + prompt + " - " + e.getMessage());
	            throw new RuntimeException("Failed to generate image asynchronously.", e);
	        }
	        
	    });
	 }
}

