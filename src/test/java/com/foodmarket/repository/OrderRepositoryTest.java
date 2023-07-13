package com.foodmarket.repository;

import com.foodmarket.configuration.TestConfiguration;
import com.foodmarket.model.entity.OrderEntity;
import com.foodmarket.model.entity.OrderProductEntity;
import com.foodmarket.model.entity.ProductEntity;
import org.apache.commons.collections4.SetUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = TestConfiguration.class)
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    private final OrderEntity orderEntity1 = new OrderEntity();
    private final OrderEntity orderEntity2 = new OrderEntity();
    private final OrderEntity orderEntity3 = new OrderEntity();

    private final ProductEntity productEntity1 = new ProductEntity("Bananas", "Fruit", "Bunch", 2.99, "Fresh, ripe bananas");
    private final ProductEntity productEntity2 = new ProductEntity("Apples", "Fruit", "Bag", 4.99, "Juicy, crunchy apples");
    private final ProductEntity productEntity3 = new ProductEntity("Oranges", "Fruit", "Bag", 3.99, "Sweet and tangy oranges");

    private final OrderProductEntity orderProductEntity1 = new OrderProductEntity(orderEntity1, productEntity1, 5);
    private final OrderProductEntity orderProductEntity2 = new OrderProductEntity(orderEntity1, productEntity2, 5);
    private final OrderProductEntity orderProductEntity3 = new OrderProductEntity(orderEntity1, productEntity3, 5);

    private final Set<OrderProductEntity> orderProductEntities1 = SetUtils.hashSet(orderProductEntity1);
    private final Set<OrderProductEntity> orderProductEntities2 = SetUtils.hashSet(orderProductEntity1, orderProductEntity2);
    private final Set<OrderProductEntity> orderProductEntities3 = SetUtils.hashSet(orderProductEntity1, orderProductEntity2, orderProductEntity3);

    @BeforeEach
    public void init() {
        orderEntity1.setOrderedProducts(orderProductEntities1);
        orderEntity2.setOrderedProducts(orderProductEntities2);
        orderEntity3.setOrderedProducts(orderProductEntities3);
    }

    @Test
    public void saveOrderTest() {
        // when
        OrderEntity saved = orderRepository.save(orderEntity1);
        // then
        assertNotNull(saved);
        assertFalse(saved.getOrderedProducts()
                .isEmpty());
    }

    @Test
    public void saveMultipleOrdersTest() {
        // when
        orderRepository.save(orderEntity1);
        orderRepository.save(orderEntity2);
        orderRepository.save(orderEntity3);
        List<OrderEntity> allOrderEntities = orderRepository.findAll();
        // then
        List<OrderEntity> expectedOrderEntities = List.of(orderEntity1, orderEntity2, orderEntity3);
        assertFalse(allOrderEntities.isEmpty());
        assertEquals(expectedOrderEntities, allOrderEntities);
    }

    @Test
    public void saveOrderMultipleTimesTest() {
        // when
        orderRepository.save(orderEntity1);
        orderRepository.save(orderEntity1);
        List<OrderEntity> allOrderEntities = orderRepository.findAll();
        // then
        List<OrderEntity> expectedOrderEntities = List.of(orderEntity1);
        assertFalse(allOrderEntities.isEmpty());
        assertEquals(expectedOrderEntities, allOrderEntities);
    }

    @Test
    public void findByIdTest() {
        // when
        orderRepository.save(orderEntity1);
        Optional<OrderEntity> fromRepositoryOptional = orderRepository.findById(orderEntity1.getId());
        // then
        assertTrue(fromRepositoryOptional.isPresent());
        OrderEntity fromRepository = fromRepositoryOptional.get();
        assertEquals(orderEntity1, fromRepository);
    }

}
