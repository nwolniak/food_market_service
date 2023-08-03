package com.foodmarket.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
@Table(name = "carts")
@ToString(onlyExplicitlyIncluded = true)
public class CartEntity {

    @Id
    @Column(name = "cart_id")
    private Long id;

    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    @OneToMany(mappedBy = "cartEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartItemEntity> cartItems;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    public void addCartItem(CartItemEntity cartItemEntity) {
        this.cartItems.add(cartItemEntity);
    }

    public void removeCartItem(CartItemEntity cartItemEntity) {
        this.cartItems.remove(cartItemEntity);
    }


}
