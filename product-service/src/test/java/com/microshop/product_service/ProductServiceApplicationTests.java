package com.microshop.product_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microshop.product_service.dto.ProductRequest;
import com.microshop.product_service.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer( "mongo:4.4.2" );

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository repository;

    @DynamicPropertySource
    static void setProperties( DynamicPropertyRegistry dynamicPropertyRegistry ) {
        dynamicPropertyRegistry.add( "spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl );
    }

    @Test
    void shouldCreateProduct() throws Exception {
        ProductRequest productRequest = getProductRequest();
        String requestString = objectMapper.writeValueAsString( productRequest );

        mockMvc.perform( post( "/api/product" )
                                 .contentType( APPLICATION_JSON )
                                 .content( requestString ) )
               .andExpect( status().isCreated() );
        assertEquals( 1, repository.findAll().size() );
    }

    private ProductRequest getProductRequest() {
        return ProductRequest.builder()
                             .name( "iPhone 13" )
                             .description( "iPhone 13" )
                             .price( BigDecimal.TEN )
                             .build();
    }

}
