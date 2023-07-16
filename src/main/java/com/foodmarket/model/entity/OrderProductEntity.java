package com.foodmarket.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "orders_products")
public class OrderProductEntity {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private OrderProductKey id;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    private OrderEntity orderEntity;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private ProductEntity productEntity;

    @Column(name = "quantity")
    private int quantity;

    protected OrderProductEntity() {
    }

    public OrderProductEntity(OrderEntity orderEntity, ProductEntity productEntity, int quantity) {
        this.id = new OrderProductKey(orderEntity.getId(), productEntity.getId());
        this.orderEntity = orderEntity;
        this.productEntity = productEntity;
        this.quantity = quantity;
    }

}
