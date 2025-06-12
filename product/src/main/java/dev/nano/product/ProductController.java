package dev.nano.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import dev.nano.swagger.BaseController;

import java.util.List;

import static dev.nano.product.ProductConstant.PRODUCT_URI_REST_API;

@RestController
@RequestMapping(path = PRODUCT_URI_REST_API)
@Tag(name = BaseController.PRODUCT_TAG, description = BaseController.PRODUCT_DESCRIPTION)
@AllArgsConstructor @Slf4j
public class ProductController {

    private final ProductService productService;


    @Operation(
            summary = "Create new product",
            description = "Add a new product to the catalog"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Product created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid product data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    @PreAuthorize("hasRole('app_admin')")
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        log.info("Creating new product: {}", productDTO);
        return new ResponseEntity<>(
                productService.create(productDTO),
                HttpStatus.CREATED
        );
    }

    @Operation(
            summary = "Get product by ID",
            description = "Retrieve detailed information about a specific product using its unique identifier"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Product found successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable("productId") Long productId) {
        log.info("Retrieving product with id {}", productId);
        return ResponseEntity.ok(productService.getProduct(productId));
    }

    @Operation(
            summary = "Get all products with filtering and pagination",
            description = "Retrieve a paginated list of products with optional search criteria"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Products retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ProductDTO.class))
                    )
            ),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/list")
    public ResponseEntity<List<ProductDTO>> getAllProducts(
            @RequestParam(value="page", defaultValue = "1") int page,
            @RequestParam(value="limit", defaultValue = "10") int limit,
            @RequestParam(value="search", defaultValue = "") String search) {
        log.info("Retrieving all products with page {}, limit {}, search {}", page, limit, search);
        return ResponseEntity.ok(productService.getAllProducts(page, limit, search));
    }

    @Operation(
            summary = "Update product",
            description = "Modify existing product information"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Product updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid product data"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{productId}")
    @PreAuthorize("hasRole('app_admin')")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable("productId") Long productId,
            @Valid @RequestBody ProductDTO productDTO) {
        log.info("Updating product with ID {}: {}", productId, productDTO);
        return ResponseEntity.ok(productService.update(productId, productDTO));
    }

    @Operation(
            summary = "Delete product",
            description = "Remove a product from the catalog"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{productId}")
    @PreAuthorize("hasRole('app_admin')")
    public ResponseEntity<Void> deleteProduct(@PathVariable("productId") Long productId) {
        log.info("Deleting product with ID: {}", productId);
        productService.delete(productId);
        return ResponseEntity.noContent().build();
    }
}
