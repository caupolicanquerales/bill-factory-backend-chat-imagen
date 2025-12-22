package com.facturas_sinteticas_imagenes.facturas_sinteticas_imagenes.configurations;

import java.net.http.HttpClient;
import java.time.Duration;

import org.springframework.boot.web.client.RestClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;

@Configuration
public class RestClientConfiguration {
	
	 private static final int TIMEOUT_SECONDS = 300; 
    private static final Duration API_READ_TIMEOUT = Duration.ofSeconds(TIMEOUT_SECONDS); 
    private static final Duration API_CONNECT_TIMEOUT = Duration.ofSeconds(10); 

    /**
     * Customizes all RestClient.Builder instances in the application.
     * This ensures the internal RestClient used by OpenAiImageApi (when WebFlux is absent)
     * has the required 5-minute read timeout for DALL-E generation.
     */
    @Bean
    public RestClientCustomizer restClientCustomizer() {
        return restClientBuilder -> {
            
            HttpClient configuredHttpClient = HttpClient.newBuilder()
                    .connectTimeout(API_CONNECT_TIMEOUT) 
                    .build();
            JdkClientHttpRequestFactory jdkFactory = new JdkClientHttpRequestFactory(configuredHttpClient);
            jdkFactory.setReadTimeout(API_READ_TIMEOUT);
            restClientBuilder.requestFactory(jdkFactory);
        };
    }
}
