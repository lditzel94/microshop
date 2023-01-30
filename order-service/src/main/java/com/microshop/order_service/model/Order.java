package com.microshop.order_service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.GenerationType.AUTO;

@Entity
@Table( name = "t_orders" )
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue( strategy = AUTO )
    private Long id;
    private String orderNumber;

    @OneToMany( cascade = ALL )
    private List<OrderLineItem> orderLineItems;
}
