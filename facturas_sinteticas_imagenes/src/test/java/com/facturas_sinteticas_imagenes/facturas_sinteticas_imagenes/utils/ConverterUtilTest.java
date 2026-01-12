package com.facturas_sinteticas_imagenes.facturas_sinteticas_imagenes.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.http.codec.ServerSentEvent;

import com.facturas_sinteticas_imagenes.facturas_sinteticas_imagenes.response.DataMessage;
import com.facturas_sinteticas_imagenes.facturas_sinteticas_imagenes.response.GenerationSyntheticDataResponse;

class ConverterUtilTest {

    @Test
    void generationResponseHasMessage() {
        GenerationSyntheticDataResponse resp = ConverterUtil.getGenerationSyntheticDataResponse("OK");
        assertNotNull(resp);
        assertEquals("OK", resp.getResponse());
    }

    @Test
    void dataMessageSetAndServerSentEvent() {
        DataMessage dm = ConverterUtil.setDataMessage("hello");
        assertNotNull(dm);
        assertEquals("hello", dm.getMessage());

        ServerSentEvent<DataMessage> sse = ConverterUtil.setServerSentEvent(dm, "evt");
        assertNotNull(sse);
        assertEquals("evt", sse.event());
        assertEquals(dm, sse.data());
    }
}
