package dev.nano.product;

import java.util.List;

public interface ProductService {
    ProductDTO create(ProductDTO product);
    ProductDTO getProduct(long productId);
    List<ProductDTO> getAllProducts(int page, int limit, String search);
    ProductDTO update(long id, ProductDTO request);
    void delete(long id);
}
