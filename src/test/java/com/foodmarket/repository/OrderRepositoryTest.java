package com.foodmarket.repository;

import com.foodmarket.configuration.TestConfiguration;
import com.foodmarket.model.entity.OrderEntity;
import com.foodmarket.model.entity.OrderProductEntity;
import com.foodmarket.model.entity.ProductEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.collections4.CollectionUtils.isEqualCollection;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = TestConfiguration.class)
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    private final OrderEntity orderEntity1 = new OrderEntity();
    private final OrderEntity orderEntity2 = new OrderEntity();
    private final OrderEntity orderEntity3 = new OrderEntity();

    private ProductEntity productEntity1 = new ProductEntity("Bananas", "Fruit", "Bunch", 2.99, "Fresh, ripe bananas");
    private ProductEntity productEntity2 = new ProductEntity("Apples", "Fruit", "Bag", 4.99, "Juicy, crunchy apples");
    private ProductEntity productEntity3 = new ProductEntity("Oranges", "Fruit", "Bag", 3.99, "Sweet and tangy oranges");

    private OrderProductEntity orderProductEntity1;
    private OrderProductEntity orderProductEntity2;
    private OrderProductEntity orderProductEntity3;


    @BeforeEach
    public void init() {
        orderProductEntity1 = new OrderProductEntity(orderEntity1, productEntity1, 5);
        orderProductEntity2 = new OrderProductEntity(orderEntity1, productEntity2, 5);
        orderProductEntity3 = new OrderProductEntity(orderEntity1, productEntity3, 5);
        orderEntity1.setOrderedProducts(new ArrayList<>(List.of(orderProductEntity1)));
        orderEntity2.setOrderedProducts(new ArrayList<>(List.of(orderProductEntity1, orderProductEntity2)));
        orderEntity3.setOrderedProducts(new ArrayList<>(List.of(orderProductEntity1, orderProductEntity2, orderProductEntity3)));
    }

    @Test
    public void saveOrderTest() {
        // when
        OrderEntity saved = orderRepository.save(orderEntity1);
        // then
        assertNotNull(saved);
        assertEquals(orderEntity1, saved);
    }

    @Test
    public void saveMultipleOrdersTest() {
        // given
        List<OrderEntity> expectedOrders = List.of(orderEntity1, orderEntity2, orderEntity3);
        // when
        orderRepository.save(orderEntity1);
        orderRepository.save(orderEntity2);
        orderRepository.save(orderEntity3);
        List<OrderEntity> allOrders = orderRepository.findAll();
        // then
        assertNotNull(allOrders);
        assertFalse(allOrders.isEmpty());
        assertTrue(isEqualCollection(expectedOrders, allOrders));
    }

    @Test
    public void saveOrderMultipleTimesTest() {
        // given
        List<OrderEntity> expectedOrders = List.of(orderEntity1);
        // when
        orderRepository.save(orderEntity1);
        orderRepository.save(orderEntity1);
        List<OrderEntity> allOrders = orderRepository.findAll();
        // then
        assertNotNull(allOrders);
        assertFalse(allOrders.isEmpty());
        assertTrue(isEqualCollection(expectedOrders, allOrders));
    }

    @Test
    public void findByIdTest() {
        // when
        OrderEntity saved = orderRepository.save(orderEntity1);
        Optional<OrderEntity> orderEntityOptional = orderRepository.findById(saved.getId());
        // then
        assertTrue(orderEntityOptional.isPresent());
        OrderEntity orderEntity = orderEntityOptional.get();
        assertEquals(orderEntity1, orderEntity);
        assertTrue(isEqualCollection(orderEntity1.getOrderedProducts(), orderEntity.getOrderedProducts()));
    }

}
