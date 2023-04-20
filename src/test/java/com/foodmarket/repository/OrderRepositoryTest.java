package com.foodmarket.repository;

import com.foodmarket.configuration.TestConfiguration;
import com.foodmarket.model.entity.OrderEntity;
import com.foodmarket.model.entity.ProductEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = TestConfiguration.class)
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    private final ProductEntity productEntity1 = new ProductEntity("Bananas", "Fruit", "Bunch", 2.99, "Fresh, ripe bananas");
    private final ProductEntity productEntity2 = new ProductEntity("Apples", "Fruit", "Bag", 4.99, "Juicy, crunchy apples");
    private final ProductEntity productEntity3 = new ProductEntity("Oranges", "Fruit", "Bag", 3.99, "Sweet and tangy oranges");

    private final Set<ProductEntity> productEntities1 = Set.of(productEntity1);
    private final Set<ProductEntity> productEntities2 = Set.of(productEntity1, productEntity2);
    private final Set<ProductEntity> productEntities3 = Set.of(productEntity1, productEntity2, productEntity3);

    private final OrderEntity orderEntity1 = new OrderEntity(productEntities1);
    private final OrderEntity orderEntity2 = new OrderEntity(productEntities2);
    private final OrderEntity orderEntity3 = new OrderEntity(productEntities3);

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
