package dev.nano.ai.tools;

import dev.nano.clients.customer.CustomerClient;
import dev.nano.clients.customer.CustomerResponse;
import dev.nano.clients.payment.PaymentClient;
import dev.nano.clients.payment.PaymentRequest;
import dev.nano.clients.payment.PaymentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentTools {

    private final PaymentClient paymentClient;
    private final CustomerClient customerClient;

    @Tool(description = "Get payment details by payment ID. Returns the payment status, associated customer, and order.")
    public String getPayment(
            @ToolParam(description = "The unique identifier of the payment") Long paymentId
    ) {
        log.debug("Tool call: getPayment({})", paymentId);
        PaymentResponse payment = paymentClient.getPayment(paymentId);
        return formatPayment(payment);
    }

    @Tool(description = """
            Process a payment for a customer's existing order.
            Requires the customer ID and the order ID.
            The system will validate the order, process the payment,
            and send a payment confirmation notification to the customer.
            """)
    public String processPayment(
            @ToolParam(description = "The customer ID who owns the order") Long customerId,
            @ToolParam(description = "The order ID to pay for") Long orderId
    ) {
        log.debug("Tool call: processPayment(customerId={}, orderId={})", customerId, orderId);

        CustomerResponse customer = customerClient.getCustomer(customerId);

        PaymentRequest request = new PaymentRequest(
                customerId,
                customer.name(),
                customer.email(),
                orderId
        );

        PaymentResponse payment = paymentClient.createPayment(request);
        return "Payment processed! Payment #%d for Order #%d — Customer: %s — Status: %s — Date: %s".formatted(
                payment.id(), payment.orderId(), customer.name(),
                payment.status() != null ? payment.status() : "COMPLETED",
                payment.createAt()
        );
    }

    private String formatPayment(PaymentResponse p) {
        return "Payment #%d: Customer #%d, Order #%d — Status: %s — Date: %s".formatted(
                p.id(), p.customerId(), p.orderId(),
                p.status() != null ? p.status() : "N/A",
                p.createAt()
        );
    }
}
