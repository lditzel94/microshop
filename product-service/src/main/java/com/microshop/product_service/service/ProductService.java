package com.microshop.product_service.service;

import com.microshop.product_service.dto.ProductRequest;
import com.microshop.product_service.dto.ProductResponse;
import com.microshop.product_service.model.Product;
import com.microshop.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository repository;

    public void createProduct( ProductRequest productRequest ) {
        Product product = Product.builder()
                                 .name( productRequest.getName() )
                                 .description( productRequest.getDescription() )
                                 .price( productRequest.getPrice() )
                                 .build();
        repository.save( product );
        log.info( "Product {} is saved", product.getId() );
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = repository.findAll();

        return products.stream()
                       .map( this::toResponse )
                       .toList();
    }

    private ProductResponse toResponse( Product product ) {
        return ProductResponse.builder()
                              .id( product.getId() )
                              .name( product.getName() )
                              .description( product.getDescription() )
                              .price( product.getPrice() )
                              .build();
    }
}
