package dev.nano.ai.service;

import dev.nano.ai.model.ChatRequest;
import dev.nano.ai.model.ChatResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.UUID;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiAssistantService {

    private final ChatClient chatClient;

    public ChatResponse chat(ChatRequest request) {
        String conversationId = resolveConversationId(request.conversationId());
        log.debug("Processing chat request for conversation: {}", conversationId);

        String response = chatClient.prompt()
                .user(request.message())
                .advisors(advisor -> advisor.param(CONVERSATION_ID, conversationId))
                .call()
                .content();

        return new ChatResponse(conversationId, response);
    }

    public Flux<String> chatStream(ChatRequest request) {
        String conversationId = resolveConversationId(request.conversationId());
        log.debug("Processing streaming chat request for conversation: {}", conversationId);

        return chatClient.prompt()
                .user(request.message())
                .advisors(advisor -> advisor.param(CONVERSATION_ID, conversationId))
                .stream()
                .content();
    }

    private String resolveConversationId(String provided) {
        if (provided != null && !provided.isBlank()) {
            return provided;
        }
        return UUID.randomUUID().toString();
    }
}
