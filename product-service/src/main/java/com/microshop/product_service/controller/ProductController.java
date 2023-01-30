package com.microshop.product_service.controller;

import com.microshop.product_service.dto.ProductRequest;
import com.microshop.product_service.dto.ProductResponse;
import com.microshop.product_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping( "/api/product" )
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @PostMapping
    @ResponseStatus( CREATED )
    void createProduct( @RequestBody ProductRequest request ) {
        service.createProduct( request );
    }

    @GetMapping
    @ResponseStatus( OK )
    List<ProductResponse> getAllProducts() {
        return service.getAllProducts();
    }
}
