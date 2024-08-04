package com.access.zenchallenge.controller;

import com.access.zenchallenge.dto.BillDto;
import com.access.zenchallenge.entity.BillEntity;

import com.access.zenchallenge.services.BillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bills")
public class BillController {

    private final BillService billService;

    @GetMapping
    public List<BillDto> getAllBills() {
        return billService.getAllBills();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BillDto> getBillById(@PathVariable Long id) {
        Optional<BillDto> bill = billService.getBillById(id);
        return bill.map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public BillEntity createBill(@RequestBody @Valid BillDto billDto) {
        return billService.saveBill(billDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBill(@PathVariable Long id) {
        if (billService.getBillById(id).isPresent()) {
            billService.deleteBillById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
