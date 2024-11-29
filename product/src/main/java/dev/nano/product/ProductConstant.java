package dev.nano.product;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ProductConstant {
    public static final String PRODUCT_URI_REST_API = "/api/v1/products";
    public static final String PRODUCT_NOT_FOUND = "Product with ID %d not found";
    public static final String PRODUCT_CREATE_ERROR = "Failed to create product: %s";
    public static final String PRODUCT_DELETE_ERROR = "Failed to delete product: %s";
    public static final String NO_PRODUCTS_FOUND = "No products found";
}

