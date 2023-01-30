package com.microshop.inventory_service.service;

import com.microshop.inventory_service.dto.InventoryResponse;
import com.microshop.inventory_service.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository repository;

    @Transactional( readOnly = true )
    public List<InventoryResponse> isInStock( List<String> skuCodes ) {
        return repository.findBySkuCodeIn( skuCodes ).stream()
                         .map( inventory -> InventoryResponse.builder()
                                                             .skuCode( inventory.getSkuCode() )
                                                             .isInStock( inventory.getQuantity() > 0 )
                                                             .build() )
                         .toList();
    }
}