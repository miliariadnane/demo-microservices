package dev.nano.ai.controller;

import dev.nano.ai.model.ChatRequest;
import dev.nano.ai.model.ChatResponse;
import dev.nano.ai.service.AiAssistantService;
import dev.nano.swagger.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import static dev.nano.ai.AiAssistantConstant.AI_URI_REST_API;

@RestController
@RequestMapping(path = AI_URI_REST_API)
@Tag(name = BaseController.AI_ASSISTANT_TAG, description = BaseController.AI_ASSISTANT_DESCRIPTION)
@RequiredArgsConstructor
@Slf4j
public class AiAssistantController {

    private final AiAssistantService aiAssistantService;

    @Operation(
            summary = "Chat with the AI assistant",
            description = """
                    Send a message to the AI assistant and receive a response.
                    The assistant can look up customers, products, orders, and payments,
                    as well as place orders and process payments on your behalf.
                    Provide a conversationId to maintain multi-turn conversation context.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "AI response generated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ChatResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@Valid @RequestBody ChatRequest request) {
        log.info("Chat request: {}", request.message());
        return ResponseEntity.ok(aiAssistantService.chat(request));
    }

    @Operation(
            summary = "Chat with the AI assistant (streaming)",
            description = """
                    Send a message and receive the response as a Server-Sent Events stream.
                    Each event contains a chunk of the AI response as it is generated in real time.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Streaming response started"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(value = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatStream(@Valid @RequestBody ChatRequest request) {
        log.info("Streaming chat request: {}", request.message());
        return aiAssistantService.chatStream(request);
    }
}
