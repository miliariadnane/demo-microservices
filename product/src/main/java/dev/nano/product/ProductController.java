package dev.nano.product;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static dev.nano.product.ProductConstant.PRODUCT_URI_REST_API;

@RestController
@RequestMapping(path = PRODUCT_URI_REST_API)
@AllArgsConstructor @Slf4j
public class ProductController {

    private final ProductService productService;

    @GetMapping(path = "/{productId}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable("productId") Long productId) {

        log.info("Retrieving product with id {}", productId);
        return new ResponseEntity<>(
                productService.getProduct(productId),
                HttpStatus.OK
        );
    }

    @GetMapping(
            path = "/list",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<List<ProductDTO>> getAllProducts (
            @RequestParam(value="page", defaultValue = "1") int page,
            @RequestParam(value="limit", defaultValue = "10")  int limit ,
            @RequestParam(value="search", defaultValue = "") String search
    ) {

        log.info("Retrieving all products with page {}, limit {}, search {}", page, limit, search);
        List<ProductDTO> products = productService.getAllProducts(page, limit, search);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}
