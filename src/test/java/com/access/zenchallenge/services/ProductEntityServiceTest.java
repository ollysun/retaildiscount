package com.access.zenchallenge.services;

import com.access.zenchallenge.dto.ProductDto;
import com.access.zenchallenge.entity.ProductEntity;
import com.access.zenchallenge.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductEntityServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ProductService productService;

    private ProductEntity productEntity;
    private ProductDto productDto;

    @BeforeEach
    void setUp() {
        productEntity = new ProductEntity();
        productEntity.setId(1L);
        productEntity.setName("Test Product");

        productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setName("Test Product");
    }

    @Test
    void testFindAll() {
        when(productRepository.findAll()).thenReturn(Collections.singletonList(productEntity));
        when(modelMapper.map(any(ProductEntity.class), eq(ProductDto.class))).thenReturn(productDto);

        List<ProductDto> productDtos = productService.findAllProducts();

        assertNotNull(productDtos);
        assertEquals(1, productDtos.size());
        assertEquals("Test Product", productDtos.get(0).getName());

        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testGetById() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(productEntity));
        when(modelMapper.map(any(ProductEntity.class), eq(ProductDto.class))).thenReturn(productDto);

        Optional<ProductDto> productDtoOptional = productService.getProductById(1L);

        assertTrue(productDtoOptional.isPresent());
        assertEquals("Test Product", productDtoOptional.get().getName());

        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testGetByIdNotFound() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            productService.getProductById(1L);
        });

        String expectedMessage = "Product not found with id: 1";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testSave() {
        when(productRepository.save(any(ProductEntity.class))).thenReturn(productEntity);
        when(modelMapper.map(any(ProductDto.class), eq(ProductEntity.class))).thenReturn(productEntity);

        ProductEntity savedProductEntity = productService.saveProduct(productDto);

        assertNotNull(savedProductEntity);
        assertEquals("Test Product", savedProductEntity.getName());

        verify(productRepository, times(1)).save(productEntity);
    }

    @Test
    void testSaveNullProductDto() {
        assertThrows(IllegalArgumentException.class, () -> {
            productService.saveProduct(null);
        });
    }

    @Test
    void testDeleteById() {
        when(productRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(productRepository).deleteById(anyLong());

        productService.deleteProductById(1L);

        verify(productRepository, times(1)).existsById(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteByIdNotFound() {
        when(productRepository.existsById(anyLong())).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.deleteProductById(1L);
        });

        String expectedMessage = "Product not found with id: 1";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

        verify(productRepository, times(1)).existsById(1L);
    }
}
