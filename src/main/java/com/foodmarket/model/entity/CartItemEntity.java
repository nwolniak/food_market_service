package com.foodmarket.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@Table(name = "carts_items")
public class CartItemEntity {

    @EmbeddedId
    private CartItemPK id;

    @ManyToOne
    @MapsId("cartId")
    @JoinColumn(name = "cart_id")
    private CartEntity cartEntity;

    @ManyToOne
    @MapsId("itemId")
    @JoinColumn(name = "item_id")
    private ItemEntity itemEntity;

    @Column(name = "quantity")
    private int quantity;

    protected CartItemEntity() {
    }

    public CartItemEntity(CartEntity cartEntity, ItemEntity itemEntity, int quantity) {
        this.id = new CartItemPK(cartEntity.getId(), itemEntity.getId());
        this.cartEntity = cartEntity;
        this.itemEntity = itemEntity;
        this.quantity = quantity;
    }

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class CartItemPK implements Serializable {
        @Column(name = "cart_id")
        private Long cartId;

        @Column(name = "item_id")
        private Long itemId;
    }

}
