package com.foodmarket.service;

import com.foodmarket.configuration.TestConfiguration;
import com.foodmarket.exceptions.EntityNotFoundException;
import com.foodmarket.exceptions.OrderValidationException;
import com.foodmarket.exceptions.StockQuantityNotSatisfiedException;
import com.foodmarket.model.dto.OrderDTO;
import com.foodmarket.model.dto.ProductCountDTO;
import com.foodmarket.model.dto.ProductDTO;
import com.foodmarket.model.entity.ProductEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.apache.commons.collections4.CollectionUtils.isEqualCollection;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = TestConfiguration.class)
public class OrderServiceIT {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private StockService stockService;

    private final ProductDTO productDTO1 = new ProductDTO(null ,"Bananas", "Fruit", "Bunch", 2.99, "Fresh, ripe bananas");
    private final ProductDTO productDTO2 = new ProductDTO(null, "Apples", "Fruit", "Bag", 4.99, "Juicy, crunchy apples");
    private final ProductDTO productDTO3 = new ProductDTO(null, "Oranges", "Fruit", "Bag", 3.99, "Sweet and tangy oranges");

    @Test
    public void addOrderTest() {
        // given
        ProductEntity productEntity = productService.addProductReturnEntity(productDTO1);
        stockService.setProductCount(new ProductCountDTO(productEntity.getId(), 10));
        List<ProductCountDTO> orderedProducts = List.of(new ProductCountDTO(productEntity.getId(), 3));
        OrderDTO orderDTO = new OrderDTO(orderedProducts);
        // when
        OrderDTO saved = orderService.addOrder(orderDTO);
        // then
        assertNotNull(saved);
        assertEquals(orderDTO, saved);
    }

    @Test
    public void addMultipleOrdersTest() {
        // given
        ProductEntity productEntity1 = productService.addProductReturnEntity(productDTO1);
        ProductEntity productEntity2 = productService.addProductReturnEntity(productDTO2);
        stockService.setProductCount(new ProductCountDTO(productEntity1.getId(), 10));
        stockService.setProductCount(new ProductCountDTO(productEntity2.getId(), 10));
        List<ProductCountDTO> orderedProducts1 = List.of(new ProductCountDTO(productEntity1.getId(), 3));
        List<ProductCountDTO> orderedProducts2 = List.of(new ProductCountDTO(productEntity2.getId(), 3));
        OrderDTO orderDTO1 = new OrderDTO(orderedProducts1);
        OrderDTO orderDTO2 = new OrderDTO(orderedProducts2);
        List<OrderDTO> expectedOrders = List.of(orderDTO1, orderDTO2);
        // when
        orderService.addOrder(orderDTO1);
        orderService.addOrder(orderDTO2);
        List<OrderDTO> orders = orderService.getAllOrders();
        // then
        assertNotNull(orders);
        assertFalse(orders.isEmpty());
        assertTrue(isEqualCollection(expectedOrders, orders));
    }

    @Test
    public void addOrderMultipleTimesTest() {
        // given
        ProductEntity productEntity = productService.addProductReturnEntity(productDTO1);
        stockService.setProductCount(new ProductCountDTO(productEntity.getId(), 10));
        List<ProductCountDTO> orderedProducts = List.of(new ProductCountDTO(productEntity.getId(), 3));
        OrderDTO orderDTO = new OrderDTO(orderedProducts);
        List<OrderDTO> expectedOrders = List.of(orderDTO, orderDTO);
        // when
        orderService.addOrder(orderDTO);
        orderService.addOrder(orderDTO);
        List<OrderDTO> orders = orderService.getAllOrders();
        // then
        assertNotNull(orders);
        assertFalse(orders.isEmpty());
        assertEquals(2, orders.size());
        assertTrue(isEqualCollection(expectedOrders, orders));
    }

    @Test
    public void orderWithNoProductsShouldThrowExceptionTest() {
        assertThrows(OrderValidationException.class, () -> orderService.addOrder(new OrderDTO(List.of())));
    }

    @Test
    public void orderUnsatisfiedProductsQuantityShouldThrowExceptionTest() {
        // given
        ProductEntity productEntity = productService.addProductReturnEntity(productDTO1);
        stockService.setProductCount(new ProductCountDTO(productEntity.getId(), 2));
        List<ProductCountDTO> orderedProducts = List.of(new ProductCountDTO(productEntity.getId(), 10));
        OrderDTO orderDTO = new OrderDTO(orderedProducts);
        // then
        assertThrows(StockQuantityNotSatisfiedException.class, () -> orderService.addOrder(orderDTO));
    }

    @Test
    public void orderWithNotExistingProductsShouldThrowExceptionTest() {
        // given
        List<ProductCountDTO> orderedProducts = List.of(new ProductCountDTO(123, 10));
        OrderDTO orderDTO = new OrderDTO(orderedProducts);
        // then
        assertThrows(EntityNotFoundException.class, () -> orderService.addOrder(orderDTO));
    }

    @Test
    public void getNotExistingOrderShouldThrowException() {
        assertThrows(EntityNotFoundException.class, () -> orderService.getOrder(123));
    }

}
