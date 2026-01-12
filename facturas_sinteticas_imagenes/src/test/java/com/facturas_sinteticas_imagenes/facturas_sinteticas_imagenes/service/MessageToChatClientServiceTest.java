package com.facturas_sinteticas_imagenes.facturas_sinteticas_imagenes.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.messages.Message;

class MessageToChatClientServiceTest {

    @Test
    void buildsMessageWithTextFileAppended() {
        MessageToChatClientService svc = new MessageToChatClientService();
        String prompt = "start";
        Map<String, byte[]> files = Map.of("file.html", "<h1>hi</h1>".getBytes());

        List<Message> msgs = svc.buildMessage(prompt, files);
        assertEquals(1, msgs.size());
        String text = extractTextFromMessage(msgs.get(0));
        assertTrue(text.contains("start"));
        assertTrue(text.contains("FILE: file.html"));
        assertTrue(text.contains("<h1>hi</h1>"));
    }

    @Test
    void ignoresBinaryFiles() {
        MessageToChatClientService svc = new MessageToChatClientService();
        Map<String, byte[]> files = Map.of("img.png", new byte[]{1,2,3});
        List<Message> msgs = svc.buildMessage("p", files);
        assertEquals(1, msgs.size());
        String text = extractTextFromMessage(msgs.get(0));
        assertEquals("p", text);
    }

    private String extractTextFromMessage(Message m) {
        try {
            // try common getter names first
            for (var method : m.getClass().getMethods()) {
                if (method.getParameterCount() == 0 && method.getReturnType().equals(String.class)) {
                    String name = method.getName().toLowerCase();
                    if (name.contains("text") || name.contains("gettext") || name.contains("content") || name.equals("toString")) {
                        Object val = method.invoke(m);
                        if (val != null) return val.toString();
                    }
                }
            }

            // fallback: inspect fields reflectively
            Class<?> c = m.getClass();
            while (c != null) {
                for (var f : c.getDeclaredFields()) {
                    if (f.getType().equals(String.class)) {
                        f.setAccessible(true);
                        Object v = f.get(m);
                        if (v != null) return v.toString();
                    }
                }
                c = c.getSuperclass();
            }
        } catch (Exception e) {
            // fallthrough
        }
        return m.toString();
    }
}
