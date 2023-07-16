package com.foodmarket.configuration;

import com.foodmarket.model.mapping.OrderMapper;
import com.foodmarket.model.mapping.ProductCountMapper;
import com.foodmarket.model.mapping.ProductMapper;
import com.foodmarket.repository.OrderRepository;
import com.foodmarket.repository.ProductRepository;
import com.foodmarket.repository.StockRepository;
import com.foodmarket.service.OrderService;
import com.foodmarket.service.ProductService;
import com.foodmarket.service.StockService;
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
    public OrderMapper orderMapper() {
        return OrderMapper.INSTANCE;
    }

    @Bean
    public ProductMapper productMapper() {
        return ProductMapper.INSTANCE;
    }

    @Bean
    public ProductCountMapper productCountMapper() {
        return ProductCountMapper.INSTANCE;
    }

    @Bean
    public OrderService orderService() {
        return new OrderService(orderRepository, productService(), stockService(), orderMapper());
    }

    @Bean
    public StockService stockService() {
        return new StockService(stockRepository, productService(), productCountMapper());
    }

    @Bean
    public ProductService productService() {
        return new ProductService(productRepository, productMapper());
    }

}
