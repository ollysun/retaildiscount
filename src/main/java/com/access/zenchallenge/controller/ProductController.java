package com.access.zenchallenge.controller;


import com.access.zenchallenge.dto.ProductDto;
import com.access.zenchallenge.entity.ProductEntity;
import com.access.zenchallenge.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<ProductDto> getAllProducts() {
        return productService.findAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        Optional<ProductDto> productDto = productService.getProductById(id);
        return productDto.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ProductEntity createProduct(@RequestBody @Valid ProductDto product) {

        return productService.saveProduct(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductEntity> updateProduct(@PathVariable Long id, @RequestBody @Valid ProductDto productDetails) {
        Optional<ProductDto> productDto = productService.getProductById(id);

        if (productDto.isPresent()) {
            ProductDto existingProduct = productDto.get();
            existingProduct.setName(productDetails.getName());
            existingProduct.setCategory(productDetails.getCategory());
            existingProduct.setPrice(productDetails.getPrice());
            return ResponseEntity.ok(productService.saveProduct(existingProduct));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (productService.getProductById(id).isPresent()) {
            productService.deleteProductById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}