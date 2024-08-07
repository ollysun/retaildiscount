package com.access.zenchallenge.controllers;

import com.access.zenchallenge.controller.BillController;
import com.access.zenchallenge.dto.BillDto;
import com.access.zenchallenge.dto.BillDtoResponse;
import com.access.zenchallenge.dto.ProductDto;
import com.access.zenchallenge.dto.UserDto;
import com.access.zenchallenge.entity.BillEntity;
import com.access.zenchallenge.services.BillService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BillController.class)
class BillControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BillService billService;

    private BillDto billDto;
    private BillEntity billEntity;
    private BillDtoResponse billDtoResponse;

    @BeforeEach
    void setUp() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("John Doe");

        ProductDto productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setName("Product 1");
        productDto.setPrice(100.0);


        // Initialize the BillDto and BillEntity
        billDto = new BillDto();
        billDto.setId(1L);
        billDto.setUserDto(userDto);
        billDto.setProductDtos(Arrays.asList(productDto));

        billEntity = new BillEntity();
        billEntity.setId(1L);

        billDtoResponse = new BillDtoResponse();

    }

    @Test
    void testGetAllBills() throws Exception {
        when(billService.getAllBills()).thenReturn(Arrays.asList(billDto));

        mockMvc.perform(get("/bills"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(billDto.getId()));
    }

    @Test
    void testGetBillById() throws Exception {
        when(billService.getBillById(anyLong())).thenReturn(Optional.of(billDto));

        mockMvc.perform(get("/bills/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(billDto.getId()));
    }

    @Test
    void testGetBillById_NotFound() throws Exception {
        when(billService.getBillById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/bills/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateBill() throws Exception {
        when(billService.saveBill(billDto)).thenReturn(billDtoResponse);
        String content = (new ObjectMapper()).writeValueAsString(billDto);

        mockMvc.perform(post("/bills")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)) // Add other properties to the JSON as needed
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.netPayableAmount").value(billDtoResponse.getNetPayableAmount()));
    }

    @Test
    void testDeleteBill() throws Exception {
        when(billService.getBillById(anyLong())).thenReturn(Optional.of(billDto));

        mockMvc.perform(delete("/bills/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteBill_NotFound() throws Exception {
        when(billService.getBillById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(delete("/bills/1"))
                .andExpect(status().isNotFound());
    }
}
