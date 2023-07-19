package com.foodmarket.configuration;

import com.foodmarket.model.mapping.ItemMapper;
import com.foodmarket.model.mapping.ItemQuantityInStockMapper;
import com.foodmarket.model.mapping.OrderMapper;
import com.foodmarket.repository.OrderRepository;
import com.foodmarket.repository.ItemRepository;
import com.foodmarket.repository.StockRepository;
import com.foodmarket.service.OrderService;
import com.foodmarket.service.ItemService;
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
    private ItemRepository itemRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Bean
    public OrderMapper orderMapper() {
        return OrderMapper.INSTANCE;
    }

    @Bean
    public ItemMapper productMapper() {
        return ItemMapper.INSTANCE;
    }

    @Bean
    public ItemQuantityInStockMapper productCountMapper() {
        return ItemQuantityInStockMapper.INSTANCE;
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
    public ItemService productService() {
        return new ItemService(itemRepository, productMapper());
    }

}
