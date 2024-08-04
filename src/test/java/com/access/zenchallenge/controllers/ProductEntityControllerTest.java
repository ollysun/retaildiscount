package com.access.zenchallenge.controllers;

import com.access.zenchallenge.constant.ProductCategory;
import com.access.zenchallenge.controller.ProductController;
import com.access.zenchallenge.dto.ProductDto;
import com.access.zenchallenge.entity.ProductEntity;
import com.access.zenchallenge.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@ExtendWith(MockitoExtension.class)
class ProductEntityControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private MockMvc mockMvc;

    private ProductDto productDto;
    private ProductEntity productEntity;

    @BeforeEach
    void setUp() {
        mockMvc = standaloneSetup(productController).build();

        productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setName("Test Product");
        productDto.setCategory(ProductCategory.GROCERY);
        productDto.setPrice(100.0);

        productEntity = new ProductEntity();
        productEntity.setId(1L);
        productEntity.setName("Test Product");
        productEntity.setCategory(ProductCategory.OTHER);
        productEntity.setPrice(100.0);
    }

    @Test
    void testGetAllProducts() throws Exception {
        when(productService.findAllProducts()).thenReturn(Collections.singletonList(productDto));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Product"));

        verify(productService, times(1)).findAllProducts();
    }

    @Test
    void testGetProductById() throws Exception {
        when(productService.getProductById(anyLong())).thenReturn(Optional.of(productDto));

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Product"));

        verify(productService, times(1)).getProductById(1L);
    }

    @Test
    void testGetProductByIdNotFound() throws Exception {
        when(productService.getProductById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isNotFound());

        verify(productService, times(1)).getProductById(1L);
    }

    @Test
    void testCreateProduct() throws Exception {
        when(productService.saveProduct(any(ProductDto.class))).thenReturn(productEntity);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test Product\",\"category\":\"GROCERY\",\"price\":100.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Product"));

        verify(productService, times(1)).saveProduct(any(ProductDto.class));
    }

    @Test
    void testUpdateProduct() throws Exception {
        when(productService.getProductById(anyLong())).thenReturn(Optional.of(productDto));
        when(productService.saveProduct(any(ProductDto.class))).thenReturn(productEntity);

        mockMvc.perform(put("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Product\",\"category\":\"OTHER\",\"price\":150.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Product"));

        verify(productService, times(1)).getProductById(1L);
        verify(productService, times(1)).saveProduct(any(ProductDto.class));
    }

    @Test
    void testUpdateProductNotFound() throws Exception {
        when(productService.getProductById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(put("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Product\",\"category\":\"OTHER\",\"price\":150.0}"))
                .andExpect(status().isNotFound());

        verify(productService, times(1)).getProductById(1L);
        verify(productService, times(0)).saveProduct(any(ProductDto.class));
    }

    @Test
    void testDeleteProduct() throws Exception {
        when(productService.getProductById(anyLong())).thenReturn(Optional.of(productDto));

        mockMvc.perform(delete("/products/1"))
                .andExpect(status().isOk());

        verify(productService, times(1)).getProductById(1L);
        verify(productService, times(1)).deleteProductById(1L);
    }

    @Test
    void testDeleteProductNotFound() throws Exception {
        when(productService.getProductById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(delete("/products/1"))
                .andExpect(status().isNotFound());

        verify(productService, times(1)).getProductById(1L);
        verify(productService, times(0)).deleteProductById(anyLong());
    }
}
