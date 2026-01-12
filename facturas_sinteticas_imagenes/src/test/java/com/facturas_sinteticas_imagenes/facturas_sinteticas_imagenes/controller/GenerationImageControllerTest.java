package com.facturas_sinteticas_imagenes.facturas_sinteticas_imagenes.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.facturas_sinteticas_imagenes.facturas_sinteticas_imagenes.request.GenerationImageRequest;
import com.facturas_sinteticas_imagenes.facturas_sinteticas_imagenes.response.GenerationSyntheticDataResponse;
import com.facturas_sinteticas_imagenes.facturas_sinteticas_imagenes.service.ExecutingPromptImageService;

class GenerationImageControllerTest {

    @Test
    void setPromptReturnsOk() {
        ExecutingPromptImageService svc = mock(ExecutingPromptImageService.class);
        GenerationImageController ctrl = new GenerationImageController(svc);

        GenerationImageRequest req = new GenerationImageRequest();
        req.setPrompt(java.util.List.of("hello"));

        ResponseEntity<GenerationSyntheticDataResponse> resp = ctrl.setPromptImage(req);
        assertEquals(200, resp.getStatusCodeValue());
        assertEquals("OK", resp.getBody().getResponse());
    }

    @Test
    void streamImageReturnsEmitter() {
        ExecutingPromptImageService svc = mock(ExecutingPromptImageService.class);
        when(svc.generateImageAsync(anyString())).thenReturn(CompletableFuture.completedFuture("data"));

        GenerationImageController ctrl = new GenerationImageController(svc);

        // ensure RequestContext exists for emitter
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));

        var emitter = ctrl.streamImageGeneration();
        assertNotNull(emitter);
    }
}
