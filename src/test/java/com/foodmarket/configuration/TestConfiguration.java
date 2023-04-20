package com.foodmarket.configuration;

import com.foodmarket.model.mapping.ServiceMapper;
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
    public ServiceMapper serviceMapper() {
        return ServiceMapper.INSTANCE;
    }

    @Bean
    public MarketService marketService(ProductRepository productRepository, StockRepository stockRepository, ServiceMapper serviceMapper) {
        return new MarketService(productRepository, stockRepository, serviceMapper);
    }

}
