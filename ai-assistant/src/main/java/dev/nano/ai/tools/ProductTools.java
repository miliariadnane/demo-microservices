package dev.nano.ai.tools;

import dev.nano.clients.product.ProductClient;
import dev.nano.clients.product.ProductResponse;
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
public class ProductTools {

    private final ProductClient productClient;

    @Tool(description = "Get product details by ID. Returns the product name, price, available quantity, and image URL.")
    public String getProduct(
            @ToolParam(description = "The unique identifier of the product") Long productId
    ) {
        log.debug("Tool call: getProduct({})", productId);
        ProductResponse product = productClient.getProduct(productId);
        return formatProduct(product);
    }

    @Tool(description = """
            Search products in the catalog. Supports keyword search by product name.
            Returns a paginated list of matching products with their details.
            Use an empty or null search term to list all products.
            """)
    public String searchProducts(
            @ToolParam(description = "Search keyword to filter products by name (optional)") String search,
            @ToolParam(description = "Page number, starting from 1", required = false) Integer page,
            @ToolParam(description = "Number of results per page, default 10", required = false) Integer limit
    ) {
        int p = (page != null && page > 0) ? page : 1;
        int l = (limit != null && limit > 0) ? limit : 10;
        log.debug("Tool call: searchProducts(search={}, page={}, limit={})", search, p, l);

        List<ProductResponse> products = productClient.getAllProducts(p, l, search);

        if (products.isEmpty()) {
            return "No products found" + (search != null ? " matching '" + search + "'" : "") + ".";
        }

        return products.stream()
                .map(this::formatProduct)
                .collect(Collectors.joining("\n"));
    }

    private String formatProduct(ProductResponse p) {
        return "Product #%d: %s — $%d (stock: %d)".formatted(
                p.id(), p.name(), p.price(),
                p.availableQuantity() != null ? p.availableQuantity() : 0
        );
    }
}
