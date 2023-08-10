package com.foodmarket.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
@Table(name = "carts")
public class CartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "cart_id")
    @EqualsAndHashCode.Include
    private Long id;

    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    @OneToMany(mappedBy = "cartEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<CartItemEntity> cartItems;

    @CreatedBy
    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    public void addCartItem(CartItemEntity cartItemEntity) {
        this.cartItems.add(cartItemEntity);
    }

    public void removeCartItem(CartItemEntity cartItemEntity) {
        this.cartItems.remove(cartItemEntity);
    }


}
