package com.facturas_sinteticas_imagenes.facturas_sinteticas_imagenes.service;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import com.facturas_sinteticas_imagenes.facturas_sinteticas_imagenes.utils.AsyncUtil;

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
		Supplier<String> supplier = () -> {
			var options = OpenAiImageOptions.builder()
					.model(imageModelName)
					.build();

			ImagePrompt imagePrompt = new ImagePrompt(prompt, options);
			ImageResponse response = imageModel.call(imagePrompt);
			return response.getResults().get(0).getOutput().getB64Json();
		};

		return AsyncUtil.executeAsync(supplier, "Async error generating image for prompt: " + prompt);
	}
}

