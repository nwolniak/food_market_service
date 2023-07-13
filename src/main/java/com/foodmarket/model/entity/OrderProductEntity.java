package com.foodmarket.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "orders_products")
public class OrderProductEntity {

    @EmbeddedId
    private OrderProductKey id;

    @ManyToOne
    @MapsId("orderId")
    private OrderEntity orderEntity;

    @ManyToOne
    @MapsId("productId")
    private ProductEntity productEntity;

    private int quantity;

    protected OrderProductEntity() {}

    public OrderProductEntity(OrderEntity orderEntity, ProductEntity productEntity, int quantity) {
        this.orderEntity = orderEntity;
        this.productEntity = productEntity;
        this.quantity = quantity;
    }

}
