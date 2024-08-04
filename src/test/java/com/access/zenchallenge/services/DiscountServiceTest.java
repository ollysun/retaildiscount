package com.access.zenchallenge.services;


import com.access.zenchallenge.constant.ProductCategory;
import com.access.zenchallenge.constant.UserType;
import com.access.zenchallenge.entity.BillEntity;
import com.access.zenchallenge.entity.ProductEntity;
import com.access.zenchallenge.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DiscountServiceTest {

    private DiscountService discountService;

    private UserEntity employee;
    private UserEntity affiliate;
    private UserEntity longTermCustomer;
    private UserEntity regularCustomer;

    private ProductEntity groceryProduct;
    private ProductEntity nonGroceryProduct;

    @BeforeEach
    public void setUp() {
        discountService = new DiscountService();

        employee = new UserEntity();
        employee.setId(1L);
        employee.setName("Employee");
        employee.setUserType(UserType.EMPLOYEE);
        employee.setCreatedDate(LocalDate.now().minusYears(1));

        affiliate = new UserEntity();
        affiliate.setId(2L);
        affiliate.setName("Affiliate");
        affiliate.setUserType(UserType.AFFILIATE);
        affiliate.setCreatedDate(LocalDate.now().minusYears(1));

        longTermCustomer = new UserEntity();
        longTermCustomer.setId(3L);
        longTermCustomer.setName("LongTermCustomer");
        longTermCustomer.setUserType(UserType.CUSTOMER);
        longTermCustomer.setCreatedDate(LocalDate.now().minusYears(3));

        regularCustomer = new UserEntity();
        regularCustomer.setId(4L);
        regularCustomer.setName("RegularCustomer");
        regularCustomer.setUserType(UserType.CUSTOMER);
        regularCustomer.setCreatedDate(LocalDate.now().minusMonths(6));

        groceryProduct = new ProductEntity();
        groceryProduct.setId(1L);
        groceryProduct.setName("GroceryProduct");
        groceryProduct.setCategory(ProductCategory.GROCERY);
        groceryProduct.setPrice(100);

        nonGroceryProduct = new ProductEntity();
        nonGroceryProduct.setId(2L);
        nonGroceryProduct.setName("NonGroceryProduct");
        nonGroceryProduct.setCategory(ProductCategory.OTHER);
        nonGroceryProduct.setPrice(100);
    }

    @Test
     void testEmployeeDiscount() {
        BillEntity bill = new BillEntity();
        bill.setUserEntity(employee);
        bill.setProductEntities(Arrays.asList(nonGroceryProduct, nonGroceryProduct, nonGroceryProduct, nonGroceryProduct));

        double netPayable = discountService.calculateDiscount(bill);
        assertEquals(260, netPayable); // 400 - 30% = 120, Flat discount: 5*4 = 20, Total discount = 120 + 20 = 140, Net payable = 400 - 30 - 20 = 260
    }

    @Test
     void testAffiliateDiscount() {
        BillEntity bill = new BillEntity();
        bill.setUserEntity(affiliate);
        bill.setProductEntities(Arrays.asList(nonGroceryProduct, nonGroceryProduct, nonGroceryProduct, nonGroceryProduct));

        double netPayable = discountService.calculateDiscount(bill);
        assertEquals(340, netPayable); // 400 - 10% = 360
    }

    @Test
    void testLongTermCustomerDiscount() {
        BillEntity bill = new BillEntity();
        bill.setUserEntity(longTermCustomer);
        bill.setProductEntities(Arrays.asList(nonGroceryProduct, nonGroceryProduct, nonGroceryProduct, nonGroceryProduct));

        double netPayable = discountService.calculateDiscount(bill);
        assertEquals(360, netPayable); // 400 - 5% = 380
    }

    @Test
    void testFlatDiscount() {
        BillEntity bill = new BillEntity();
        bill.setUserEntity(regularCustomer);
        bill.setProductEntities(Arrays.asList(nonGroceryProduct, nonGroceryProduct, nonGroceryProduct, nonGroceryProduct, nonGroceryProduct));

        double netPayable = discountService.calculateDiscount(bill);
        assertEquals(475, netPayable); // 500 - (5*5) = 475
    }

    @Test
    public void testNoPercentageDiscountOnGroceries() {
        BillEntity bill = new BillEntity();
        bill.setUserEntity(employee);
        bill.setProductEntities(Arrays.asList(groceryProduct, groceryProduct, groceryProduct, groceryProduct));

        double netPayable = discountService.calculateDiscount(bill);
        assertEquals(380, netPayable); // No percentage discount, only flat discount applies
    }

    @Test
    public void testMixedProducts() {
        BillEntity bill = new BillEntity();
        bill.setUserEntity(employee);
        bill.setProductEntities(Arrays.asList(groceryProduct, nonGroceryProduct, nonGroceryProduct));

        double netPayable = discountService.calculateDiscount(bill);
        assertEquals(285, netPayable); // 100 (grocery) + 200 (non-grocery) - 30% of 200 = 210
    }
}
