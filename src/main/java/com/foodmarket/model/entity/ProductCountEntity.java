package com.foodmarket.model.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "product_counts")
public class ProductCountEntity {

    @Id
    @Column(name = "product_id")
    @EqualsAndHashCode.Include
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "product_id")
    private ProductEntity productEntity;

    @Column(name = "quantity_in_stock")
    private int quantityInStock;

    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    protected ProductCountEntity() {}

    public ProductCountEntity(ProductEntity productEntity, int quantityInStock) {
        this.productEntity = productEntity;
        this.quantityInStock = quantityInStock;
    }

}
