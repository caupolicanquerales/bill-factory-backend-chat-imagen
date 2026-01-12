package com.facturas_sinteticas_imagenes.facturas_sinteticas_imagenes.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;

class ExecutingPromptExtractTemplateServiceTest {

    @Test
    void generateBasicTemplateAsyncReturnsContent() throws Exception {
        ChatClient chat = mock(ChatClient.class, RETURNS_DEEP_STUBS);
        MessageToChatClientService messageSvc = mock(MessageToChatClientService.class);
        when(messageSvc.buildMessage(anyString(), any())).thenReturn(List.of(mock(Message.class)));
        when(chat.prompt().messages(anyList()).call().content()).thenReturn("result-content");

        ExecutingPromptExtractTemplateService svc = new ExecutingPromptExtractTemplateService(mock(ChatClient.Builder.class, RETURNS_DEEP_STUBS), messageSvc) {
            {
                // replace built client with our mock
                java.lang.reflect.Field f;
                try {
                    f = ExecutingPromptExtractTemplateService.class.getDeclaredField("chatClient");
                    f.setAccessible(true);
                    f.set(this, chat);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };

        var fut = svc.generateBasicTemplatAsync("p", new StoreFilesService());
        assertEquals("result-content", fut.get());
    }
}
