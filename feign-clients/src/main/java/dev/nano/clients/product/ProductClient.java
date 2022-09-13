package dev.nano.clients.product;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product", url = "${clients.product.url}")
public interface ProductClient {

    @GetMapping(path = "/api/v1/products/{productId}")
    ProductResponse getProduct(@PathVariable("productId") Long productId);
}
