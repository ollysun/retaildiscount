package com.access.zenchallenge.dto;

import com.access.zenchallenge.constant.ProductCategory;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;



@Data
public class ProductDto {
    private Long id;
    @NotEmpty(message = "Please enter the name of the product")
    private String name;
    @NotNull(message = "Please specify the product category")
    private ProductCategory category;
    @NotNull(message = "Please enter valid price")
    private double price;
}
