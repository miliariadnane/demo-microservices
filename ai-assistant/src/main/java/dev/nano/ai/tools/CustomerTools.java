package dev.nano.ai.tools;

import dev.nano.clients.customer.CustomerClient;
import dev.nano.clients.customer.CustomerResponse;
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
public class CustomerTools {

    private final CustomerClient customerClient;

    @Tool(description = "Get customer details by their unique ID. Returns name, email, and ID.")
    public String getCustomer(
            @ToolParam(description = "The unique identifier of the customer") Long customerId
    ) {
        log.debug("Tool call: getCustomer({})", customerId);
        CustomerResponse customer = customerClient.getCustomer(customerId);
        return formatCustomer(customer);
    }

    @Tool(description = "List all registered customers. Returns a list with each customer's ID, name, and email.")
    public String getAllCustomers() {
        log.debug("Tool call: getAllCustomers()");
        List<CustomerResponse> customers = customerClient.getAllCustomers();
        return customers.stream()
                .map(this::formatCustomer)
                .collect(Collectors.joining("\n"));
    }

    private String formatCustomer(CustomerResponse c) {
        return "Customer #%d: %s (email: %s)".formatted(c.id(), c.name(), c.email());
    }
}
