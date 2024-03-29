package com.foodmarket.configuration;

import com.foodmarket.model.mapping.*;
import com.foodmarket.repository.*;
import com.foodmarket.service.CartService;
import com.foodmarket.service.ItemService;
import com.foodmarket.service.OrderService;
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

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

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
    public CartMapper cartMapper() {
        return CartMapper.INSTANCE;
    }

    @Bean
    public PaymentMapper paymentMapper() {
        return PaymentMapper.INSTANCE;
    }

    @Bean
    public AuthMapper authMapper() {
        return AuthMapper.INSTANCE;
    }

    @Bean
    public OrderService orderService() {
        return new OrderService(userRepository, orderRepository, cartService(), orderMapper());
    }

    @Bean
    public StockService stockService() {
        return new StockService(stockRepository, itemService(), productCountMapper());
    }

    @Bean
    public ItemService itemService() {
        return new ItemService(itemRepository, productMapper());
    }

    @Bean
    public CartService cartService() {
        return new CartService(cartRepository, userRepository, itemService(), cartMapper());
    }

}
