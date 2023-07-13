package com.foodmarket.configuration;

import com.foodmarket.model.mapping.ServiceMapper;
import com.foodmarket.repository.OrderRepository;
import com.foodmarket.repository.ProductRepository;
import com.foodmarket.repository.StockRepository;
import com.foodmarket.service.MarketService;
import com.foodmarket.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Bean
    public ServiceMapper serviceMapper() {
        return ServiceMapper.INSTANCE;
    }

    @Bean
    public OrderService orderService() {
        return new OrderService(productRepository, stockRepository, orderRepository, serviceMapper());
    }

    @Bean
    public MarketService marketService() {
        return new MarketService(productRepository, stockRepository, orderService(), serviceMapper());
    }

}
