package dev.nano.product;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static dev.nano.product.ProductConstant.PRODUCT_URI_REST_API;

@RestController
@RequestMapping(path = PRODUCT_URI_REST_API)
@AllArgsConstructor @Slf4j
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable("productId") Long productId) {
        log.info("Retrieving product with id {}", productId);
        return ResponseEntity.ok(productService.getProduct(productId));
    }

    @GetMapping("/list")
    public ResponseEntity<List<ProductDTO>> getAllProducts(
            @RequestParam(value="page", defaultValue = "1") int page,
            @RequestParam(value="limit", defaultValue = "10") int limit,
            @RequestParam(value="search", defaultValue = "") String search) {
        log.info("Retrieving all products with page {}, limit {}, search {}", page, limit, search);
        return ResponseEntity.ok(productService.getAllProducts(page, limit, search));
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        log.info("Creating new product: {}", productDTO);
        return new ResponseEntity<>(
                productService.create(productDTO),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Long productId,
            @Valid @RequestBody ProductDTO productDTO) {
        log.info("Updating product with ID {}: {}", productId, productDTO);
        return ResponseEntity.ok(productService.update(productId, productDTO));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        log.info("Deleting product with ID: {}", productId);
        productService.delete(productId);
        return ResponseEntity.noContent().build();
    }
}
