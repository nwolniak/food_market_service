package com.foodmarket.configuration;

import com.foodmarket.model.mapping.ProductMapper;
import com.foodmarket.repository.ProductRepository;
import com.foodmarket.repository.StockRepository;
import com.foodmarket.service.MarketService;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.foodmarket.repository")
@EntityScan(basePackages = "com.foodmarket.model.entity")
@EnableJpaAuditing
public class TestConfiguration {

    @Bean
    public ProductMapper productMapper() {
        return ProductMapper.INSTANCE;
    }

    @Bean
    public MarketService marketService(ProductRepository productRepository, StockRepository stockRepository, ProductMapper productMapper) {
        return new MarketService(productRepository, stockRepository, productMapper);
    }

}
