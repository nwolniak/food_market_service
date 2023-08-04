package com.foodmarket.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "items")
public class ItemEntity {

    @Id
    @Column(name = "item_id")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "category")
    private String category;

    @Column(name = "unit_type")
    private String unitType;

    @Column(name = "unit_price")
    private double unitPrice;

    @Column(name = "description")
    private String description;

    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    @OneToOne(mappedBy = "itemEntity", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private ItemQuantityInStockEntity itemQuantityInStockEntity;

    @OneToMany(mappedBy = "itemEntity")
    private List<CartItemEntity> carts;

    @OneToMany(mappedBy = "itemEntity")
    private List<OrderItemEntity> orders;

    protected ItemEntity() {
    }

    public ItemEntity(String name, String category, String unitType, double unitPrice, String description) {
        this.id = (long) name.hashCode();
        this.name = name;
        this.category = category;
        this.unitType = unitType;
        this.unitPrice = unitPrice;
        this.description = description;
    }

}
