package com.access.zenchallenge.services;


import com.access.zenchallenge.constant.ProductCategory;
import com.access.zenchallenge.constant.UserType;
import com.access.zenchallenge.dto.BillDto;
import com.access.zenchallenge.dto.ProductDto;
import com.access.zenchallenge.dto.UserDto;
import com.access.zenchallenge.entity.BillEntity;
import com.access.zenchallenge.entity.ProductEntity;
import com.access.zenchallenge.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class DiscountService {

    public double calculateDiscount(BillDto billDto) {
        UserDto userDto = billDto.getUserDto();
        double total = billDto.getProductDtos().stream().mapToDouble(ProductDto::getPrice).sum();
        double discount = 0;

        // Percentage-based discount
        if (billDto.getProductDtos().stream().noneMatch(p -> p.getCategory().equals(ProductCategory.GROCERY))) {
            if (userDto.getUserType().equals(UserType.EMPLOYEE)) {
                discount = total * 0.30;
            } else if (userDto.getUserType().equals(UserType.AFFILIATE)) {
                discount = total * 0.10;
            } else if (userDto.getUserType().equals(UserType.CUSTOMER) &&
                    ChronoUnit.YEARS.between(userDto.getCreatedDate(), LocalDate.now()) > 2) {
                discount = total * 0.05;
            }
        }

        // Flat discount every $100 on the bill, there would be a $5 discount (e.g. for $ 990, you get $ 45)
        discount += (int) (total / 100) * 5;

        return total - discount;
    }


}
