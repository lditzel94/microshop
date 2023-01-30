package com.microshop.order_service.service;

import com.microshop.order_service.dto.InventoryResponse;
import com.microshop.order_service.dto.OrderLineItemDto;
import com.microshop.order_service.dto.OrderRequest;
import com.microshop.order_service.model.Order;
import com.microshop.order_service.model.OrderLineItem;
import com.microshop.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static java.lang.Boolean.TRUE;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository repository;
    private final WebClient.Builder webClientBuilder;

    public String placeOrder( OrderRequest request ) {
        Order order = new Order();
        order.setOrderNumber( UUID.randomUUID().toString() );
        List<OrderLineItem> orderLineItems = request.getOrderLineItemDtoList()
                                                    .stream()
                                                    .map( this::toEntity )
                                                    .toList();
        order.setOrderLineItems( orderLineItems );
        List<String> skuCodes = order.getOrderLineItems().stream()
                                     .map( OrderLineItem::getSkuCode )
                                     .toList();

        InventoryResponse[] inventoryResponses = webClientBuilder.build()
                                                                 .get()
                                                                 .uri( "http://inventory-service/api/inventory",
                                                                       uriBuilder -> uriBuilder.queryParam( "skuCode", skuCodes ).build() )
                                                                 .retrieve()
                                                                 .bodyToMono( InventoryResponse[].class )
                                                                 .block();

        boolean allProductsInStock = Arrays.stream( inventoryResponses )
                                           .allMatch( InventoryResponse::isInStock );

        if ( TRUE.equals( allProductsInStock ) ) {
            repository.save( order );
            return "Order Placed Successfully";
        } else throw new IllegalArgumentException( "Product is not in stock, please try again later" );
    }

    private OrderLineItem toEntity( OrderLineItemDto orderLineItemDto ) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setPrice( orderLineItemDto.getPrice() );
        orderLineItem.setQuantity( orderLineItemDto.getQuantity() );
        orderLineItem.setSkuCode( orderLineItemDto.getSkuCode() );
        return orderLineItem;
    }
}
