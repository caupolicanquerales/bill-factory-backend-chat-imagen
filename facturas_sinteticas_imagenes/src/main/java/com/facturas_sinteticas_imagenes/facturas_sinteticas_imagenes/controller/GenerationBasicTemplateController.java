package com.facturas_sinteticas_imagenes.facturas_sinteticas_imagenes.controller;

import java.util.List;
import java.util.Map;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.facturas_sinteticas_imagenes.facturas_sinteticas_imagenes.request.GenerationImageRequest;
import com.facturas_sinteticas_imagenes.facturas_sinteticas_imagenes.response.GenerationSyntheticDataResponse;
import com.facturas_sinteticas_imagenes.facturas_sinteticas_imagenes.service.ConverterFileService;
import com.facturas_sinteticas_imagenes.facturas_sinteticas_imagenes.service.ExecutingPromptExtractTemplateService;
import com.facturas_sinteticas_imagenes.facturas_sinteticas_imagenes.service.StoreFilesService;
import com.facturas_sinteticas_imagenes.facturas_sinteticas_imagenes.utils.ConverterUtil;
import com.facturas_sinteticas_imagenes.facturas_sinteticas_imagenes.utils.SseStreamUtil;

@RestController
@RequestMapping("basic-template")
@CrossOrigin(origins = "http://localhost:4200")
public class GenerationBasicTemplateController {
	
	private final ExecutorService executor = Executors.newCachedThreadPool();
	private final ExecutingPromptExtractTemplateService executingPromptService;
	private final StoreFilesService storeFiles;
	private final ConverterFileService converterFile;
	private String prompt="";
	
	@Value(value="${event.name.chat.basic.template}")
	private String eventName;
	

    public GenerationBasicTemplateController(ExecutingPromptExtractTemplateService executingPromptService, 
    		StoreFilesService storeFiles, ConverterFileService converterFile) {
        this.executingPromptService= executingPromptService;
        this.storeFiles= storeFiles;
        this.converterFile= converterFile;
    }
	
	
	@PostMapping("/prompt")
	public ResponseEntity<GenerationSyntheticDataResponse> updatePrompt(@RequestBody GenerationImageRequest request){
		prompt= request.getPrompt().get(0);
		GenerationSyntheticDataResponse generation= ConverterUtil.getGenerationSyntheticDataResponse("OK");
		return ResponseEntity.ok(generation);
	}
	
	@PostMapping("/sending-files")
	public ResponseEntity<GenerationSyntheticDataResponse> getFilesToChatClient(@RequestPart("files") List<MultipartFile> fileParts){
		Map<String, byte[]> fileDataMap = converterFile.convertFileToMap(fileParts);
		storeFiles.setFileParts(fileDataMap);
		GenerationSyntheticDataResponse generation= ConverterUtil.getGenerationSyntheticDataResponse("OK");
		return ResponseEntity.ok(generation);
	}
	
	@GetMapping(path = "/stream-basic-template", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamMessages() {
        return SseStreamUtil.stream(executor, eventName, "Basic Template generation started for prompt",
                () -> executingPromptService.generateBasicTemplatAsync(prompt, storeFiles));
    }
	
}
