package com.access.zenchallenge.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class BillDto {
    private Long id;
    @NotNull(message = "Please enter the Userdetails")
    private UserDto userDto;
    @NotNull(message = "Please enter the product details")
    @NotEmpty(message = "Please provider the product details")
    private List<ProductDto> productDtos;
    private double netPayableAmount;
}
