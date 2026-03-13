package dev.nano.ai.tools;

import dev.nano.clients.customer.CustomerClient;
import dev.nano.clients.customer.CustomerResponse;
import dev.nano.clients.order.OrderClient;
import dev.nano.clients.order.OrderRequest;
import dev.nano.clients.order.OrderResponse;
import dev.nano.clients.order.OrderViewClient;
import dev.nano.clients.order.OrderViewResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderTools {

    private final OrderClient orderClient;
    private final OrderViewClient orderViewClient;
    private final CustomerClient customerClient;

    @Tool(description = """
            Get enriched order details by order ID.
            Returns the full order view including customer name, product name, price, and amount.
            This is the CQRS read model with pre-joined data from customer and product services.
            """)
    public String getOrderView(
            @ToolParam(description = "The unique identifier of the order") Long orderId
    ) {
        log.debug("Tool call: getOrderView({})", orderId);
        OrderViewResponse view = orderViewClient.getOrderView(orderId);
        return formatOrderView(view);
    }

    @Tool(description = """
            Get all orders placed by a specific customer.
            Returns enriched order views with product and customer details.
            Useful for checking a customer's order history.
            """)
    public String getOrdersByCustomer(
            @ToolParam(description = "The unique identifier of the customer") Long customerId
    ) {
        log.debug("Tool call: getOrdersByCustomer({})", customerId);
        List<OrderViewResponse> views = orderViewClient.getOrderViewsByCustomer(customerId);

        if (views.isEmpty()) {
            return "No orders found for customer #%d.".formatted(customerId);
        }

        return views.stream()
                .map(this::formatOrderView)
                .collect(Collectors.joining("\n"));
    }

    @Tool(description = """
            Place a new order for a customer.
            Requires a valid customer ID, product ID, and the order amount (quantity).
            The system will validate the customer and product, create the order,
            and trigger an order confirmation notification.
            """)
    public String placeOrder(
            @ToolParam(description = "The customer ID who is placing the order") Long customerId,
            @ToolParam(description = "The product ID to order") Long productId,
            @ToolParam(description = "The quantity/amount to order") Integer amount
    ) {
        log.debug("Tool call: placeOrder(customerId={}, productId={}, amount={})", customerId, productId, amount);

        CustomerResponse customer = customerClient.getCustomer(customerId);

        OrderRequest request = new OrderRequest(
                customerId,
                customer.name(),
                customer.email(),
                productId,
                amount
        );

        OrderResponse order = orderClient.createOrder(request);
        return "Order placed successfully! Order #%d — Customer: %s, Product ID: %d, Amount: %d, Created: %s".formatted(
                order.id(), customer.name(), order.productId(), order.amount(), order.createAt()
        );
    }

    private String formatOrderView(OrderViewResponse v) {
        return "Order #%d: %s ordered %s (%.0f units) at $%.2f each — Total: $%.2f — Date: %s".formatted(
                v.orderId(),
                v.customerName(),
                v.productName(),
                v.amount(),
                v.productPrice(),
                v.amount() * v.productPrice(),
                v.createdAt()
        );
    }
}
