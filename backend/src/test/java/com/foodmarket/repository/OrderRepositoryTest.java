package com.foodmarket.repository;

import com.foodmarket.configuration.TestConfiguration;
import com.foodmarket.model.entity.ItemEntity;
import com.foodmarket.model.entity.OrderEntity;
import com.foodmarket.model.entity.OrderItemEntity;
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

    private ItemEntity itemEntity1 = new ItemEntity("Bananas", "Fruit", "Bunch", 2.99, "Fresh, ripe bananas");
    private ItemEntity itemEntity2 = new ItemEntity("Apples", "Fruit", "Bag", 4.99, "Juicy, crunchy apples");
    private ItemEntity itemEntity3 = new ItemEntity("Oranges", "Fruit", "Bag", 3.99, "Sweet and tangy oranges");

    private OrderItemEntity orderItemEntity1;
    private OrderItemEntity orderItemEntity2;
    private OrderItemEntity orderItemEntity3;


    @BeforeEach
    public void init() {
        orderItemEntity1 = new OrderItemEntity(orderEntity1, itemEntity1, 5);
        orderItemEntity2 = new OrderItemEntity(orderEntity1, itemEntity2, 5);
        orderItemEntity3 = new OrderItemEntity(orderEntity1, itemEntity3, 5);
        orderEntity1.setOrderItems(new ArrayList<>(List.of(orderItemEntity1)));
        orderEntity2.setOrderItems(new ArrayList<>(List.of(orderItemEntity1, orderItemEntity2)));
        orderEntity3.setOrderItems(new ArrayList<>(List.of(orderItemEntity1, orderItemEntity2, orderItemEntity3)));
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
        assertTrue(isEqualCollection(orderEntity1.getOrderItems(), orderEntity.getOrderItems()));
    }

    @Test
    public void deleteByIdTest() {
        // given
        OrderEntity saved = orderRepository.save(orderEntity1);
        // when
        orderRepository.deleteById(saved.getId());
        Optional<OrderEntity> deleted = orderRepository.findById(saved.getId());
        // then
        assertTrue(deleted.isEmpty());
    }

}
