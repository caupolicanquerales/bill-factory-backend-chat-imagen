package com.facturas_sinteticas_imagenes.facturas_sinteticas_imagenes.controller;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.facturas_sinteticas_imagenes.facturas_sinteticas_imagenes.request.GenerationImageRequest;
import com.facturas_sinteticas_imagenes.facturas_sinteticas_imagenes.response.GenerationSyntheticDataResponse;
import com.facturas_sinteticas_imagenes.facturas_sinteticas_imagenes.service.ExecutingPromptImageService;
import com.facturas_sinteticas_imagenes.facturas_sinteticas_imagenes.utils.ConverterUtil;
import com.facturas_sinteticas_imagenes.facturas_sinteticas_imagenes.utils.SseStreamUtil;

@RestController
@RequestMapping("image")
@CrossOrigin(origins = "http://localhost:4200")
public class GenerationImageController {
	
	private final ExecutorService executor = Executors.newCachedThreadPool();
	private final ExecutingPromptImageService executingImage;
	private String prompt="";
	
	@Value(value="${event.name.image}")
	private String eventName;
	
	public GenerationImageController(ExecutingPromptImageService executingImage) {
		this.executingImage=executingImage;
	}
	
	@PostMapping("/set-prompt")
	public ResponseEntity<GenerationSyntheticDataResponse> setPromptImage(@RequestBody GenerationImageRequest request){
		prompt= request.getPrompt().get(0);
		GenerationSyntheticDataResponse generation= ConverterUtil.getGenerationSyntheticDataResponse("OK");
		return ResponseEntity.ok(generation);
	}
	
	@GetMapping(path = "/stream-image", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamImageGeneration() {
        return SseStreamUtil.stream(executor, eventName, "Image generation started for prompt",
                () -> executingImage.generateImageAsync(prompt));
	}
}
