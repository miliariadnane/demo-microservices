package dev.nano.clients.order;

import dev.nano.clients.config.InternalApiKeyFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "order", url = "${clients.order.url}", configuration = InternalApiKeyFeignConfig.class)
public interface OrderClient {
    @GetMapping(path = "/api/v1/orders/{orderId}")
    OrderResponse getOrder(@PathVariable("orderId") Long orderId);

    @PostMapping(path = "/api/v1/orders/add")
    OrderResponse createOrder(@RequestBody OrderRequest orderRequest);
}
