package com.facturas_sinteticas_imagenes.facturas_sinteticas_imagenes.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

class ConverterFileServiceTest {

    @Test
    void convertsMultipartFilesToMap() {
        ConverterFileService svc = new ConverterFileService();
        MockMultipartFile f1 = new MockMultipartFile("files", "a.txt", "text/plain", "hello".getBytes());
        MockMultipartFile f2 = new MockMultipartFile("files", "b.txt", "text/plain", "world".getBytes());

        Map<String, byte[]> map = svc.convertFileToMap(List.of(f1, f2));
        assertEquals(2, map.size());
        assertArrayEquals("hello".getBytes(), map.get("a.txt"));
        assertArrayEquals("world".getBytes(), map.get("b.txt"));
    }

    @Test
    void duplicateNamesKeepFirst() {
        ConverterFileService svc = new ConverterFileService();
        MockMultipartFile a1 = new MockMultipartFile("files", "a.txt", "text/plain", "one".getBytes());
        MockMultipartFile a2 = new MockMultipartFile("files", "a.txt", "text/plain", "two".getBytes());
        Map<String, byte[]> map = svc.convertFileToMap(List.of(a1, a2));
        assertEquals(1, map.size());
        assertArrayEquals("one".getBytes(), map.get("a.txt"));
    }
}
