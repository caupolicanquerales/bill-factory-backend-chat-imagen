package com.facturas_sinteticas_imagenes.facturas_sinteticas_imagenes.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.api.OpenAiImageApi;
import org.springframework.ai.image.ImageModel;

/**
 * Configuration to manually expose the OpenAiImageModel using our custom OpenAiImageApi
 * which has the extended timeout configured.
 */
@Configuration
public class OpenAiImageModelConfiguration {
    
    @Bean
    public ImageModel customOpenAiImageModel(OpenAiImageApi openAiImageApi) {
        
        return new OpenAiImageModel(openAiImageApi);
    }
}
