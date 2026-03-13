package dev.nano.ai.config;

import dev.nano.ai.tools.CustomerTools;
import dev.nano.ai.tools.OrderTools;
import dev.nano.ai.tools.PaymentTools;
import dev.nano.ai.tools.ProductTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings;
import org.springframework.boot.web.client.RestClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class AiConfig {

    private static final String SYSTEM_PROMPT = """
            You are a helpful e-commerce assistant for our microservices platform.
            You have access to the following tools that let you interact with the platform's services:

            CUSTOMER TOOLS:
            - Look up individual customers by their ID
            - List all registered customers

            PRODUCT TOOLS:
            - Look up individual products by their ID
            - Search the product catalog by keyword
            - List products with pagination

            ORDER TOOLS:
            - Look up individual orders by ID (enriched view with customer and product details)
            - Look up all orders placed by a specific customer
            - Place new orders on behalf of customers (requires customerId, productId, and amount)

            PAYMENT TOOLS:
            - Look up payment details by payment ID
            - Process payments for existing orders (requires customerId and orderId)

            GUIDELINES:
            - Always use the available tools to fetch real data — never fabricate IDs, names, or prices.
            - When a user asks about orders, prefer the enriched order view which includes customer and product names.
            - For order placement, verify the customer and product exist before confirming.
            - Format monetary values clearly (e.g., "$1,999").
            - Keep responses concise and informative.
            - If a tool call fails (e.g., resource not found), explain the error clearly to the user.
            """;

    @Bean
    RestClientCustomizer restClientCustomizer() {
        return restClientBuilder -> restClientBuilder.requestFactory(
                ClientHttpRequestFactoryBuilder.detect().build(
                        ClientHttpRequestFactorySettings.defaults()
                                .withReadTimeout(Duration.ofSeconds(120))
                                .withConnectTimeout(Duration.ofSeconds(30))
                )
        );
    }

    @Bean
    ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .build();
    }

    @Bean
    ChatClient chatClient(
            ChatClient.Builder builder,
            ChatMemory chatMemory,
            CustomerTools customerTools,
            ProductTools productTools,
            OrderTools orderTools,
            PaymentTools paymentTools
    ) {
        return builder
                .defaultSystem(SYSTEM_PROMPT)
                .defaultTools(customerTools, productTools, orderTools, paymentTools)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }
}
