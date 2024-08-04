package com.access.zenchallenge.services;

import com.access.zenchallenge.dto.BillDto;
import com.access.zenchallenge.dto.ProductDto;
import com.access.zenchallenge.dto.UserDto;
import com.access.zenchallenge.entity.BillEntity;
import com.access.zenchallenge.repository.BillRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class BillServiceTest {

    @Mock
    private BillRepository billRepository;

    @Mock
    private DiscountService discountService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private BillService billService;

    private BillDto billDto;
    private BillEntity billEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup mock objects
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("John Doe");

        ProductDto productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setName("Product 1");
        productDto.setPrice(100.0);

        billDto = new BillDto();
        billDto.setId(1L);
        billDto.setUserDto(userDto);
        billDto.setProductDtos(Arrays.asList(productDto));

        billEntity = new BillEntity();
        billEntity.setId(1L);

    }

    @Test
    void testGetAllBills() {
        when(billRepository.findAll()).thenReturn(Arrays.asList(billEntity));
        when(modelMapper.map(any(BillEntity.class), eq(BillDto.class))).thenReturn(billDto);

        List<BillDto> result = billService.getAllBills();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(billRepository, times(1)).findAll();
        verify(modelMapper, times(1)).map(any(BillEntity.class), eq(BillDto.class));
    }

    @Test
    void testGetBillById() {
        when(billRepository.findById(anyLong())).thenReturn(Optional.of(billEntity));
        when(modelMapper.map(any(BillEntity.class), eq(BillDto.class))).thenReturn(billDto);

        Optional<BillDto> result = billService.getBillById(1L);

        assertTrue(result.isPresent());
        assertEquals(billDto, result.get());
        verify(billRepository, times(1)).findById(anyLong());
        verify(modelMapper, times(1)).map(any(BillEntity.class), eq(BillDto.class));
    }

    @Test
    void testGetBillById_NotFound() {
        when(billRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            billService.getBillById(1L);
        });

        assertEquals("Bill not found with id: 1", exception.getMessage());
        verify(billRepository, times(1)).findById(anyLong());
    }

    @Test
    void testSaveBill() {
        when(modelMapper.map(any(BillDto.class), eq(BillEntity.class))).thenReturn(billEntity);
        when(discountService.calculateDiscount(any(BillEntity.class))).thenReturn(90.0);
        when(billRepository.save(any(BillEntity.class))).thenReturn(billEntity);

        BillEntity result = billService.saveBill(billDto);

        assertNotNull(result);
        assertEquals(90.0, result.getNetPayableAmount());
        verify(modelMapper, times(1)).map(any(BillDto.class), eq(BillEntity.class));
        verify(discountService, times(1)).calculateDiscount(any(BillEntity.class));
        verify(billRepository, times(1)).save(any(BillEntity.class));
    }

    @Test
    void testSaveBill_NullDto() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            billService.saveBill(null);
        });

        assertEquals("BillDto cannot be null", exception.getMessage());
    }

    @Test
    void testSaveBill_InvalidDto() {
        billDto.setUserDto(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            billService.saveBill(billDto);
        });

        assertEquals("BillDto must have valid user and product details", exception.getMessage());
    }

    @Test
    void testDeleteBillById() {
        when(billRepository.existsById(anyLong())).thenReturn(true);

        billService.deleteBillById(1L);

        verify(billRepository, times(1)).existsById(anyLong());
        verify(billRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void testDeleteBillById_NotFound() {
        when(billRepository.existsById(anyLong())).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            billService.deleteBillById(1L);
        });

        assertEquals("Bill not found with id: 1", exception.getMessage());
        verify(billRepository, times(1)).existsById(anyLong());
    }
}
