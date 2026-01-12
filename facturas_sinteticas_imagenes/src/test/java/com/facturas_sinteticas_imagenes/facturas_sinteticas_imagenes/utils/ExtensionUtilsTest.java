package com.facturas_sinteticas_imagenes.facturas_sinteticas_imagenes.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.util.MimeType;

class ExtensionUtilsTest {

    @Test
    void knownExtensionReturnsMime() {
        MimeType mt = ExtensionUtils.getMapMimeType("index.html");
        assertNotNull(mt);
        assertEquals("text", mt.getType());
        assertEquals("html", mt.getSubtype());
    }

    @Test
    void unknownExtensionReturnsNull() {
        MimeType mt = ExtensionUtils.getMapMimeType("image.png");
        assertNull(mt);
    }
}
