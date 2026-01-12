package com.facturas_sinteticas_imagenes.facturas_sinteticas_imagenes.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.Test;

class AsyncUtilTest {

    @Test
    void executeAsyncSuccess() throws Exception {
        CompletableFuture<String> f = AsyncUtil.executeAsync(() -> "ok", "prefix");
        assertEquals("ok", f.get());
    }

    @Test
    void executeAsyncExceptionWrapped() {
        CompletableFuture<String> f = AsyncUtil.executeAsync(() -> { throw new RuntimeException("fail"); }, "p");
        // wait for completion and ensure it completes exceptionally
        assertThrows(Exception.class, () -> f.get());
    }
}
