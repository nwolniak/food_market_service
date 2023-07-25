package com.foodmarket.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "items_quantity")
public class ItemQuantityInStockEntity {

    @Id
    @Column(name = "item_id")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "quantity_in_stock")
    private int quantityInStock;

    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    @OneToOne(cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "item_id")
    private ItemEntity itemEntity;

    protected ItemQuantityInStockEntity() {
    }

    public ItemQuantityInStockEntity(ItemEntity itemEntity, int quantityInStock) {
        this.itemEntity = itemEntity;
        this.quantityInStock = quantityInStock;
    }

}
