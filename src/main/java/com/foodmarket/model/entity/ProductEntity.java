package com.foodmarket.model.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "products")
public class ProductEntity {

    @Id
    @Column(name = "product_id")
    private Long id;

    @Column(name = "name")
    @EqualsAndHashCode.Include
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

    @OneToOne(mappedBy = "productEntity")
    private ProductCountEntity productCountEntity;

    @OneToMany(mappedBy = "productEntity")
    private Set<OrderProductEntity> orderProductEntities;

    protected ProductEntity() {}

    public ProductEntity(String name, String category, String unitType, double unitPrice, String description) {
        this.id = (long) name.hashCode();
        this.name = name;
        this.category = category;
        this.unitType = unitType;
        this.unitPrice = unitPrice;
        this.description = description;
    }

}
