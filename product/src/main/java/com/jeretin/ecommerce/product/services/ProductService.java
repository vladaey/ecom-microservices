package com.jeretin.ecommerce.product.services;

import com.jeretin.ecommerce.product.dtos.ProductRequest;
import com.jeretin.ecommerce.product.dtos.ProductResponse;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    ProductResponse createProduct(ProductRequest productRequest);

    Optional<ProductResponse> updateProduct(Long id, ProductRequest productRequest);

    Boolean deleteProduct(Long id);

    List<ProductResponse> getAllProducts();

    Optional<ProductResponse> getProductById(Long id);

    List<ProductResponse> searchProducts(String keyword);
}
