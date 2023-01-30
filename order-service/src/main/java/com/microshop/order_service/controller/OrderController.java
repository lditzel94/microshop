package com.microshop.order_service.controller;

import com.microshop.order_service.dto.OrderRequest;
import com.microshop.order_service.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping( "/api/order" )
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;

    @PostMapping
    @ResponseStatus( CREATED )
    @CircuitBreaker( name = "inventory", fallbackMethod = "fallbackMethod" )
    @TimeLimiter( name = "inventory" )
    @Retry( name = "inventory" )
    CompletableFuture<String> placeOrder( @RequestBody OrderRequest request ) {
        return CompletableFuture.supplyAsync( () -> service.placeOrder( request ) );
    }

    CompletableFuture<String> fallbackMethod( OrderRequest request, RuntimeException runtimeException ) {
        return CompletableFuture.supplyAsync( () -> "Oops! Something went wrong, please order after some time" );
    }
}
