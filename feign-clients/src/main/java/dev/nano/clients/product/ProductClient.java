package dev.nano.clients.product;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "product", url = "${clients.product.url}")
public interface ProductClient {

    @GetMapping(path = "/api/v1/products/{productId}")
    ProductResponse getProduct(@PathVariable("productId") Long productId);

    @GetMapping(path = "/api/v1/products/list")
    List<ProductResponse> getAllProducts(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            @RequestParam(value = "search", required = false) String search
    );
}
