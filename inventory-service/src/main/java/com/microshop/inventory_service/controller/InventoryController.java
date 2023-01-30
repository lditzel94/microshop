package com.microshop.inventory_service.controller;

import com.microshop.inventory_service.dto.InventoryResponse;
import com.microshop.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping( "/api/inventory" )
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService service;

    @GetMapping
    @ResponseStatus( OK )
    List<InventoryResponse> isInStock( @RequestParam List<String> skuCode ) {
        return service.isInStock( skuCode );
    }
}
