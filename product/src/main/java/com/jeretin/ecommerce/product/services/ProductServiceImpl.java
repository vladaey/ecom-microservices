package com.jeretin.ecommerce.product.services;

import com.jeretin.ecommerce.product.dtos.ProductRequest;
import com.jeretin.ecommerce.product.dtos.ProductResponse;
import com.jeretin.ecommerce.product.models.Product;
import com.jeretin.ecommerce.product.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = modelMapper.map(productRequest, Product.class);
        Product savedProduct = productRepository.save(product);
        return new ModelMapper().map(savedProduct, ProductResponse.class);
    }

    @Override
    public Optional<ProductResponse> updateProduct(Long id, ProductRequest productRequest) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    existingProduct.setName(productRequest.getName());
                    existingProduct.setDescription(productRequest.getDescription());
                    existingProduct.setPrice(productRequest.getPrice());
                    existingProduct.setStockQuantity(productRequest.getStockQuantity());
                    existingProduct.setCategory(productRequest.getCategory());
                    existingProduct.setImageUrl(productRequest.getImageUrl());
                    Product updatedProduct = productRepository.save(existingProduct);

                    return modelMapper.map(updatedProduct, ProductResponse.class);
                });
    }

    @Override
    public Boolean deleteProduct(Long id) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    existingProduct.setActive(false);
                    productRepository.save(existingProduct);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findByActiveTrue().stream()
                .map(existingProduct -> modelMapper.map(existingProduct, ProductResponse.class))
                .toList();
    }

    @Override
    public Optional<ProductResponse> getProductById(Long id) {
        return productRepository.findByIdAndActiveTrue(id)
                .map(existingProduct -> modelMapper.map(existingProduct, ProductResponse.class));
    }

    @Override
    public List<ProductResponse> searchProducts(String keyword) {
        return productRepository.searchProducts(keyword).stream()
                .map(existingProduct -> modelMapper.map(existingProduct, ProductResponse.class))
                .toList();
    }
}
