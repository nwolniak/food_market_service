package com.foodmarket.service;

import com.foodmarket.configuration.TestConfiguration;
import com.foodmarket.exceptions.EntityNotFoundException;
import com.foodmarket.model.dto.CartDto;
import com.foodmarket.model.dto.CartDto.ItemQuantity;
import com.foodmarket.model.dto.ItemDto;
import com.foodmarket.model.dto.OrderRequestDto;
import com.foodmarket.model.dto.OrderResponseDto;
import com.foodmarket.model.entity.ItemEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = TestConfiguration.class)
public class OrderServiceIT {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private CartService cartService;

    private final ItemDto itemDto1 = new ItemDto(null, "Bananas", "Fruit", "Bunch", 2.99, "Fresh, ripe bananas");
    private final ItemDto itemDto2 = new ItemDto(null, "Apples", "Fruit", "Bag", 4.99, "Juicy, crunchy apples");
    private final ItemDto itemDto3 = new ItemDto(null, "Oranges", "Fruit", "Bag", 3.99, "Sweet and tangy oranges");

    @Test
    public void addOrderTest() {
        // given
        ItemEntity itemEntity = itemService.addItemReturnEntity(itemDto1);
        List<ItemQuantity> cartItems = List.of(new ItemQuantity(itemEntity.getId(), 3));
        CartDto cartDto = cartService.addCart(new CartDto(null, cartItems));
        OrderRequestDto orderRequestDTO = new OrderRequestDto(cartDto.cartId());
        // when
        OrderResponseDto saved = orderService.addOrder(orderRequestDTO);
        // then
        assertNotNull(saved);
    }

    @Test
    public void addMultipleOrdersTest() {
        // given
        ItemEntity itemEntity1 = itemService.addItemReturnEntity(itemDto1);
        ItemEntity itemEntity2 = itemService.addItemReturnEntity(itemDto2);
        List<ItemQuantity> cartItems1 = List.of(new ItemQuantity(itemEntity1.getId(), 3));
        List<ItemQuantity> cartItems2 = List.of(new ItemQuantity(itemEntity2.getId(), 3));
        CartDto cartDto1 = cartService.addCart(new CartDto(null, cartItems1));
        CartDto cartDto2 = cartService.addCart(new CartDto(null, cartItems2));
        OrderRequestDto orderRequestDto1 = new OrderRequestDto(cartDto1.cartId());
        OrderRequestDto orderRequestDto2 = new OrderRequestDto(cartDto2.cartId());
        // when
        orderService.addOrder(orderRequestDto1);
        orderService.addOrder(orderRequestDto2);
        List<OrderResponseDto> orders = orderService.getAllOrders();
        // then
        assertNotNull(orders);
        assertFalse(orders.isEmpty());
        assertEquals(2, orders.size());
    }

    @Test
    public void addOrderMultipleTimesTest() {
        // given
        ItemEntity itemEntity = itemService.addItemReturnEntity(itemDto1);
        List<ItemQuantity> cartItems = List.of(new ItemQuantity(itemEntity.getId(), 3));
        CartDto cartDto = cartService.addCart(new CartDto(null, cartItems));
        OrderRequestDto orderRequestDTO = new OrderRequestDto(cartDto.cartId());
        // when
        orderService.addOrder(orderRequestDTO);
        orderService.addOrder(orderRequestDTO);
        List<OrderResponseDto> orders = orderService.getAllOrders();
        // then
        assertNotNull(orders);
        assertFalse(orders.isEmpty());
        assertEquals(2, orders.size());
    }

    @Test
    public void getNotExistingOrderShouldThrowException() {
        assertThrows(EntityNotFoundException.class, () -> orderService.getOrder(123));
    }

}
