package dev.nano.product;

import dev.nano.exceptionhandler.business.ProductException;
import dev.nano.exceptionhandler.core.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static dev.nano.product.ProductConstant.*;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductDTO getProduct(long productId) {
        return productRepository.findById(productId)
                .map(productMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(PRODUCT_NOT_FOUND, productId)
                ));
    }

    @Override
    public List<ProductDTO> getAllProducts(int page, int limit, String search) {
        if(page > 0) page = page - 1;

        Pageable pageableRequest = PageRequest.of(page, limit);
        Page<ProductEntity> productPage;

        if(search == null || search.isEmpty()) {
            productPage = productRepository.findAllProducts(pageableRequest);
        } else {
            productPage = productRepository.findAllProductsByCriteria(pageableRequest, search);
        }

        List<ProductEntity> products = productPage.getContent();
        if (products.isEmpty()) {
            throw new ResourceNotFoundException(NO_PRODUCTS_FOUND);
        }

        return productMapper.toListDTO(products);
    }

    @Override
    public ProductDTO create(ProductDTO productDTO) {
        try {
            ProductEntity product = productMapper.toEntity(productDTO);
            return productMapper.toDTO(productRepository.save(product));
        } catch (Exception e) {
            throw new ProductException(String.format(PRODUCT_CREATE_ERROR, e.getMessage()));
        }
    }

    @Override
    public ProductDTO update(long id, ProductDTO productDTO) {
        ProductEntity existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(PRODUCT_NOT_FOUND, id)
                ));

        existingProduct.setName(productDTO.getName());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setImage(productDTO.getImage());
        existingProduct.setAvailableQuantity(productDTO.getAvailableQuantity());

        return productMapper.toDTO(productRepository.save(existingProduct));
    }

    @Override
    public void delete(long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException(String.format(PRODUCT_NOT_FOUND, id));
        }
        try {
            productRepository.deleteById(id);
        } catch (Exception e) {
            throw new ProductException(String.format(PRODUCT_DELETE_ERROR, e.getMessage()));
        }
    }
}
