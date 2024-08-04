package com.access.zenchallenge.services;


import com.access.zenchallenge.constant.ProductCategory;
import com.access.zenchallenge.constant.UserType;
import com.access.zenchallenge.entity.BillEntity;
import com.access.zenchallenge.entity.ProductEntity;
import com.access.zenchallenge.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class DiscountService {

    public double calculateDiscount(BillEntity billEntity) {
        UserEntity user = billEntity.getUserEntity();
        double total = billEntity.getProductEntities().stream().mapToDouble(ProductEntity::getPrice).sum();
        double discount = 0;

        // Percentage-based discount
        if (billEntity.getProductEntities().stream().noneMatch(p -> p.getCategory().equals(ProductCategory.GROCERY))) {
            if (user.getUserType().equals(UserType.EMPLOYEE)) {
                discount = total * 0.30;
            } else if (user.getUserType().equals(UserType.AFFILIATE)) {
                discount = total * 0.10;
            } else if (user.getUserType().equals(UserType.CUSTOMER) &&
                    ChronoUnit.YEARS.between(user.getCreatedDate(), LocalDate.now()) > 2) {
                discount = total * 0.05;
            }
        }

        // Flat discount every $100 on the bill, there would be a $5 discount (e.g. for $ 990, you get $ 45)
        discount += (int) (total / 100) * 5;

        return total - discount;
    }


}
