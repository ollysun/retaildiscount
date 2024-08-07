package com.access.zenchallenge.services;


import com.access.zenchallenge.dto.BillDto;
import com.access.zenchallenge.dto.BillDtoResponse;
import com.access.zenchallenge.entity.BillEntity;
import com.access.zenchallenge.repository.BillRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BillService {

    private final BillRepository billRepository;
    private final DiscountService discountService;
    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public List<BillDto> getAllBills() {

        return billRepository.findAll().stream()
                .map(billEntity  -> modelMapper.map(billEntity, BillDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<BillDto> getBillById(Long id) {

        return Optional.ofNullable(billRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Bill not found with id: " + id)))
                .map(user -> modelMapper.map(user, BillDto.class));
    }

    @Transactional
    public BillDtoResponse saveBill(@Valid BillDto billDto) {
        if (billDto == null) {
            throw new IllegalArgumentException("BillDto cannot be null");
        }

        // Validate billDto fields before mapping to entity
        if (billDto.getUserDto() == null || billDto.getProductDtos() == null || billDto.getProductDtos().isEmpty()) {
            throw new IllegalArgumentException("BillDto must have valid user and product details");
        }
        double netPayableAmount = discountService.calculateDiscount(billDto);

        // Save the net payable amount to the bill
        billDto.setNetPayableAmount(netPayableAmount);
        BillEntity billEntity = modelMapper.map(billDto, BillEntity.class);
        BillDtoResponse billDtoResponse = new BillDtoResponse();
        billDtoResponse.setNetPayableAmount(billRepository.save(billEntity).getNetPayableAmount());
        return billDtoResponse;
    }

    @Transactional
    public void deleteBillById(Long id) {
        if (billRepository.existsById(id)) {
            billRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Bill not found with id: " + id);
        }

    }
}
