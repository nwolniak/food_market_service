package com.foodmarket.service;

import com.foodmarket.configuration.TestConfiguration;
import com.foodmarket.exceptions.EntityNotFoundException;
import com.foodmarket.exceptions.OrderValidationException;
import com.foodmarket.exceptions.StockQuantityNotSatisfiedException;
import com.foodmarket.model.dto.OrderDTO;
import com.foodmarket.model.dto.ProductDTO;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.SetUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = TestConfiguration.class)
public class OrderServiceIT {

    @Autowired
    private OrderService orderService;

    @Autowired
    private MarketService marketService;

    private final ProductDTO productDTO1 = new ProductDTO("Bananas", "Fruit", "Bunch", 2.99, "Fresh, ripe bananas");
    private final ProductDTO productDTO2 = new ProductDTO("Apples", "Fruit", "Bag", 4.99, "Juicy, crunchy apples");
    private final ProductDTO productDTO3 = new ProductDTO("Oranges", "Fruit", "Bag", 3.99, "Sweet and tangy oranges");

    private final Map<ProductDTO, Integer> orderedProducts1 = Map.of(productDTO1, 5);
    private final Map<ProductDTO, Integer> orderedProducts2 = Map.of(productDTO1, 5, productDTO2, 5);
    private final Map<ProductDTO, Integer> orderedProducts3 = Map.of(productDTO1, 5, productDTO2, 5, productDTO3, 5);

    private final OrderDTO orderDTO1 = new OrderDTO(orderedProducts1);
    private final OrderDTO orderDTO2 = new OrderDTO(orderedProducts2);
    private final OrderDTO orderDTO3 = new OrderDTO(orderedProducts3);

    @BeforeEach
    public void init() {
        marketService.addProduct(productDTO1);
        marketService.addProduct(productDTO2);
        marketService.addProduct(productDTO3);
    }

    @Test
    public void addOrderTest() {
        // when
        OrderDTO saved = orderService.addOrder(orderDTO1);
        // then
        assertNotNull(saved);
        assertEquals(orderDTO1, saved);
    }

    @Test
    public void addMultipleOrdersTest() {
        // given

        // when
        orderService.addOrder(orderDTO1);
        orderService.addOrder(orderDTO2);
        orderService.addOrder(orderDTO3);
        List<OrderDTO> allOrders = orderService.getAllOrders();
        // then
        List<OrderDTO> expectedOrdersList = List.of(orderDTO1, orderDTO2, orderDTO3);
        assertFalse(allOrders.isEmpty());
        assertEquals(expectedOrdersList, allOrders);
    }

    @Test
    public void addOrderMultipleTimesTest() {
        // when
        orderService.addOrder(orderDTO1);
        orderService.addOrder(orderDTO1);
        List<OrderDTO> allOrders = orderService.getAllOrders();
        // then
        List<OrderDTO> expectedOrdersList = List.of(orderDTO1, orderDTO1);
        assertFalse(allOrders.isEmpty());
        assertEquals(expectedOrdersList, allOrders);
    }

    @Test
    public void orderWithNoProductsShouldThrowExceptionTest() {
        assertThrows(OrderValidationException.class, () -> orderService.addOrder(new OrderDTO(Map.of())));
    }

    @Test
    public void orderUnsatisfiedProductsQuantityShouldThrowExceptionTest() {
        // given
        orderService.addOrder(orderDTO3);
        // then
        assertThrows(StockQuantityNotSatisfiedException.class,() -> orderService.addOrder(orderDTO3));
    }

    @Test
    public void orderWithNotExistenceProductsShouldThrowExceptionTest() {
        // given
        OrderDTO orderDTO = new OrderDTO(Map.of(new ProductDTO("NotExistingName", "NotExistingCategory", "NotExistingType", 1.0, ""), 1));
        // then
        assertThrows(EntityNotFoundException.class, () -> orderService.addOrder(orderDTO));
    }

}
