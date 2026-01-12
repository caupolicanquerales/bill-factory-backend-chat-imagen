package com.facturas_sinteticas_imagenes.facturas_sinteticas_imagenes.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImageResponse;

class ExecutingPromptImageServiceTest {

    @Test
    void generateImageAsyncReturnsB64() throws Exception {
        ImageModel imageModel = mock(ImageModel.class, RETURNS_DEEP_STUBS);
        ImageResponse resp = mock(ImageResponse.class, RETURNS_DEEP_STUBS);
        when(imageModel.call(any())).thenReturn(resp);
        when(resp.getResults().get(0).getOutput().getB64Json()).thenReturn("b64data");

        ExecutingPromptImageService svc = new ExecutingPromptImageService(imageModel);
        var fut = svc.generateImageAsync("p");
        assertEquals("b64data", fut.get());
    }
}
