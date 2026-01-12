package com.facturas_sinteticas_imagenes.facturas_sinteticas_imagenes.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.Test;

class StoreFilesServiceTest {

    @Test
    void setAndGetFileParts() {
        StoreFilesService s = new StoreFilesService();
        Map<String, byte[]> m = Map.of("k", "v".getBytes());
        s.setFileParts(m);
        assertSame(m, s.getFileParts());
    }
}
