package com.facturas_sinteticas_imagenes.facturas_sinteticas_imagenes.configurations;

import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.api.OpenAiImageApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebClientConfiguration {
	
	@Value("${spring.ai.openai.base-url}")
	private String baseUrl;
	
	
	@Bean
    public OpenAiImageApi openAiImageApi(
            @Value("${spring.ai.openai.api-key}") String apiKey
        ) {
        
        return OpenAiImageApi.builder()
            .apiKey(apiKey)
            .baseUrl(this.baseUrl)
            .build();
    }
	
	@Bean
    public OpenAiImageModel openAiImageModel(OpenAiImageApi openAiImageApi) {
        return new OpenAiImageModel(openAiImageApi);
    }
}
