package com.foodmarket.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "orders_items")
public class OrderItemEntity {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private OrderItemPK id;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    private OrderEntity orderEntity;

    @ManyToOne
    @MapsId("itemId")
    @JoinColumn(name = "item_id")
    private ItemEntity itemEntity;

    @Column(name = "quantity")
    private int quantity;

    protected OrderItemEntity() {
    }

    public OrderItemEntity(OrderEntity orderEntity, ItemEntity itemEntity, int quantity) {
        this.id = new OrderItemPK(orderEntity.getId(), itemEntity.getId());
        this.orderEntity = orderEntity;
        this.itemEntity = itemEntity;
        this.quantity = quantity;
    }

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class OrderItemPK implements Serializable {

        @Column(name = "order_id")
        private Long orderId;

        @Column(name = "item_id")
        private Long itemId;

    }

}
