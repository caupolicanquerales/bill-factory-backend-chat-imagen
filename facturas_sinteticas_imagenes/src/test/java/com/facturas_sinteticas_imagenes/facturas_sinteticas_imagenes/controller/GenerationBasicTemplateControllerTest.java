package com.facturas_sinteticas_imagenes.facturas_sinteticas_imagenes.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.facturas_sinteticas_imagenes.facturas_sinteticas_imagenes.request.GenerationImageRequest;
import com.facturas_sinteticas_imagenes.facturas_sinteticas_imagenes.response.GenerationSyntheticDataResponse;
import com.facturas_sinteticas_imagenes.facturas_sinteticas_imagenes.service.ConverterFileService;
import com.facturas_sinteticas_imagenes.facturas_sinteticas_imagenes.service.ExecutingPromptExtractTemplateService;
import com.facturas_sinteticas_imagenes.facturas_sinteticas_imagenes.service.StoreFilesService;

class GenerationBasicTemplateControllerTest {

    @Test
    void updatePromptSetsPrompt() {
        var exec = mock(ExecutingPromptExtractTemplateService.class);
        var store = new StoreFilesService();
        var conv = new ConverterFileService();
        GenerationBasicTemplateController ctrl = new GenerationBasicTemplateController(exec, store, conv);

        GenerationImageRequest req = new GenerationImageRequest();
        req.setPrompt(List.of("p"));
        GenerationSyntheticDataResponse r = ctrl.updatePrompt(req).getBody();
        assertEquals("OK", r.getResponse());
    }

    @Test
    void sendingFilesStoresFiles() {
        var exec = mock(ExecutingPromptExtractTemplateService.class);
        var store = new StoreFilesService();
        var conv = new ConverterFileService();
        GenerationBasicTemplateController ctrl = new GenerationBasicTemplateController(exec, store, conv);

        MockMultipartFile f = new MockMultipartFile("files", "a.txt", "text/plain", "hi".getBytes());
        GenerationSyntheticDataResponse resp = ctrl.getFilesToChatClient(List.of(f)).getBody();
        assertEquals("OK", resp.getResponse());
        assertNotNull(store.getFileParts());
        assertArrayEquals("hi".getBytes(), store.getFileParts().get("a.txt"));
    }

    @Test
    void streamMessagesReturnsEmitter() {
        var exec = mock(ExecutingPromptExtractTemplateService.class);
        when(exec.generateBasicTemplatAsync(anyString(), any())).thenReturn(CompletableFuture.completedFuture("out"));
        var store = new StoreFilesService();
        var conv = new ConverterFileService();
        GenerationBasicTemplateController ctrl = new GenerationBasicTemplateController(exec, store, conv);

        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));

        var emitter = ctrl.streamMessages();
        assertNotNull(emitter);
    }
}
