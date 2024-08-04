package com.access.zenchallenge.services;

import com.access.zenchallenge.dto.ProductDto;
import com.access.zenchallenge.entity.ProductEntity;
import com.access.zenchallenge.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final ModelMapper modelMapper;

    /**
     * Retrieve all products.
     * @return List of all products
     */
    @Transactional(readOnly = true)
    public List<ProductDto> findAllProducts() {
        return productRepository.findAll().stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .collect(Collectors.toList());
    }

    /**
     * Find a product by its ID.
     *
     * @param id The ID of the product to find
     * @return Optional containing the product if found, empty otherwise
     */
    @Transactional(readOnly = true)
    public Optional<ProductDto> getProductById(Long id) {

        return Optional.ofNullable(productRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Product not found with id: " + id)))
                .map(product -> modelMapper.map(product, ProductDto.class));
    }


    /**
     * Save a new product.
     * @param productDto The product data to save
     * @return The saved product
     */
    @Transactional
    public ProductEntity saveProduct(ProductDto productDto) {

        if (productDto == null) {
            throw new IllegalArgumentException("ProductDto cannot be null");
        }

        ProductEntity productEntity = modelMapper.map(productDto, ProductEntity.class);
        return productRepository.save(productEntity);
    }

    /**
     * Delete a product by its ID.
     * @param id The ID of the product to delete
     */
    @Transactional
    public void deleteProductById(Long id) {

        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Product not found with id: " + id);
        }


    }
}
