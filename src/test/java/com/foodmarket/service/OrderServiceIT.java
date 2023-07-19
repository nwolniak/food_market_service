package com.foodmarket.service;

import com.foodmarket.configuration.TestConfiguration;
import com.foodmarket.exceptions.EntityNotFoundException;
import com.foodmarket.exceptions.OrderValidationException;
import com.foodmarket.exceptions.StockQuantityNotSatisfiedException;
import com.foodmarket.model.dto.ItemDTO;
import com.foodmarket.model.dto.ItemQuantityInStockDTO;
import com.foodmarket.model.dto.OrderDTO;
import com.foodmarket.model.dto.OrderDTO.ItemQuantity;
import com.foodmarket.model.entity.ItemEntity;
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
    private ItemService itemService;

    @Autowired
    private StockService stockService;

    private final ItemDTO itemDTO1 = new ItemDTO(null ,"Bananas", "Fruit", "Bunch", 2.99, "Fresh, ripe bananas");
    private final ItemDTO itemDTO2 = new ItemDTO(null, "Apples", "Fruit", "Bag", 4.99, "Juicy, crunchy apples");
    private final ItemDTO itemDTO3 = new ItemDTO(null, "Oranges", "Fruit", "Bag", 3.99, "Sweet and tangy oranges");

    @Test
    public void addOrderTest() {
        // given
        ItemEntity itemEntity = itemService.addItemReturnEntity(itemDTO1);
        stockService.setItemQuantity(new ItemQuantityInStockDTO(itemEntity.getId(), 10));
        List<ItemQuantity> orderedProducts = List.of(new ItemQuantity(itemEntity.getId(), 3));
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
        ItemEntity itemEntity1 = itemService.addItemReturnEntity(itemDTO1);
        ItemEntity itemEntity2 = itemService.addItemReturnEntity(itemDTO2);
        stockService.setItemQuantity(new ItemQuantityInStockDTO(itemEntity1.getId(), 10));
        stockService.setItemQuantity(new ItemQuantityInStockDTO(itemEntity2.getId(), 10));
        List<ItemQuantity> orderedProducts1 = List.of(new ItemQuantity(itemEntity1.getId(), 3));
        List<ItemQuantity> orderedProducts2 = List.of(new ItemQuantity(itemEntity2.getId(), 3));
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
        ItemEntity itemEntity = itemService.addItemReturnEntity(itemDTO1);
        stockService.setItemQuantity(new ItemQuantityInStockDTO(itemEntity.getId(), 10));
        List<ItemQuantity> orderedProducts = List.of(new ItemQuantity(itemEntity.getId(), 3));
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
    public void orderWithNoItemsShouldThrowExceptionTest() {
        assertThrows(OrderValidationException.class, () -> orderService.addOrder(new OrderDTO(List.of())));
    }

    @Test
    public void orderUnsatisfiedItemsQuantityShouldThrowExceptionTest() {
        // given
        ItemEntity itemEntity = itemService.addItemReturnEntity(itemDTO1);
        stockService.setItemQuantity(new ItemQuantityInStockDTO(itemEntity.getId(), 2));
        List<ItemQuantity> orderedItems = List.of(new ItemQuantity(itemEntity.getId(), 10));
        OrderDTO orderDTO = new OrderDTO(orderedItems);
        // then
        assertThrows(StockQuantityNotSatisfiedException.class, () -> orderService.addOrder(orderDTO));
    }

    @Test
    public void orderWithNotExistingItemsShouldThrowExceptionTest() {
        // given
        List<ItemQuantity> orderedItems = List.of(new ItemQuantity(123, 10));
        OrderDTO orderDTO = new OrderDTO(orderedItems);
        // then
        assertThrows(EntityNotFoundException.class, () -> orderService.addOrder(orderDTO));
    }

    @Test
    public void getNotExistingOrderShouldThrowException() {
        assertThrows(EntityNotFoundException.class, () -> orderService.getOrder(123));
    }

}
