package dev.nano.ai.service;

import dev.nano.ai.model.ChatRequest;
import dev.nano.ai.model.ChatResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiAssistantService {

    private final ChatClient chatClient;

    public ChatResponse chat(ChatRequest request) {
        String conversationId = resolveConversationId(request.conversationId());
        String userContext = buildUserContext();
        log.debug("Processing chat request for conversation: {} | user: {}", conversationId, getUserName());

        String response = chatClient.prompt()
                .system(systemSpec -> systemSpec.text(userContext))
                .user(request.message())
                .advisors(advisor -> advisor.param(CONVERSATION_ID, conversationId))
                .call()
                .content();

        return new ChatResponse(conversationId, response);
    }

    public Flux<String> chatStream(ChatRequest request) {
        String conversationId = resolveConversationId(request.conversationId());
        String userContext = buildUserContext();
        log.debug("Processing streaming chat request for conversation: {} | user: {}", conversationId, getUserName());

        return chatClient.prompt()
                .system(systemSpec -> systemSpec.text(userContext))
                .user(request.message())
                .advisors(advisor -> advisor.param(CONVERSATION_ID, conversationId))
                .stream()
                .content();
    }

    private String buildUserContext() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            return "";
        }

        String username = getUserName();
        Collection<String> roles = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        boolean isAdmin = roles.contains("ROLE_app_admin");

        return """
                CURRENT USER CONTEXT:
                - Username: %s
                - Roles: %s
                - Access level: %s
                You are speaking to this user. Respect their role: if they lack permission \
                for an operation, explain clearly instead of attempting the tool call."""
                .formatted(username, String.join(", ", roles), isAdmin ? "admin" : "standard user");
    }

    private String getUserName() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof Jwt jwt) {
            return jwt.getClaimAsString("preferred_username");
        }
        return auth != null ? auth.getName() : "unknown";
    }

    private String resolveConversationId(String provided) {
        if (provided != null && !provided.isBlank()) {
            return provided;
        }
        return UUID.randomUUID().toString();
    }
}
