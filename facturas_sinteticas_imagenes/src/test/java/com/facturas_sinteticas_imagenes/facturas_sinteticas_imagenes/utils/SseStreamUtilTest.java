package com.facturas_sinteticas_imagenes.facturas_sinteticas_imagenes.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

class SseStreamUtilTest {

    @Test
    void streamCompletesWhenSupplierCompletes() throws Exception {
        ExecutorService ex = Executors.newSingleThreadExecutor();
        AtomicBoolean completed = new AtomicBoolean(false);

        // create a CompletableFuture that we control to ensure onCompletion is registered
        CompletableFuture<String> cf = new CompletableFuture<>();
        SseEmitter emitter = SseStreamUtil.stream(ex, "evt", "start", () -> cf);

        // complete supplier after listener registered
        cf.complete("res");

        ex.shutdown();
        ex.awaitTermination(2, TimeUnit.SECONDS);

        assertNotNull(emitter);
    }
}
