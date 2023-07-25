package com.foodmarket.service;

import com.foodmarket.configuration.TestConfiguration;
import com.foodmarket.exceptions.EntityNotFoundException;
import com.foodmarket.model.dto.CartDto;
import com.foodmarket.model.dto.ItemDto;
import com.foodmarket.model.entity.ItemEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = TestConfiguration.class)
public class CartServiceIT {

    @Autowired
    private CartService cartService;

    @Autowired
    private ItemService itemService;

    private final ItemDto itemDto1 = new ItemDto(null, "Bananas", "Fruit", "Bunch", 2.99, "Fresh, ripe bananas");
    private final ItemDto itemDto2 = new ItemDto(null, "Apples", "Fruit", "Bag", 4.99, "Juicy, crunchy apples");
    private final ItemDto itemDto3 = new ItemDto(null, "Oranges", "Fruit", "Bag", 3.99, "Sweet and tangy oranges");

    @Test
    public void addCartTest() {
        // given
        ItemEntity itemEntity = itemService.addItemReturnEntity(itemDto1);
        List<CartDto.ItemQuantity> cartItems = List.of(new CartDto.ItemQuantity(itemEntity.getId(), 3));
        CartDto cartDTO = new CartDto(null, cartItems);
        // when
        CartDto saved = cartService.addCart(cartDTO);
        // then
        assertNotNull(saved);
        assertEquals(cartDTO.cartItems(), saved.cartItems());
    }

    @Test
    public void addMultipleCartsTest() {
        // given
        ItemEntity itemEntity1 = itemService.addItemReturnEntity(itemDto1);
        ItemEntity itemEntity2 = itemService.addItemReturnEntity(itemDto2);
        List<CartDto.ItemQuantity> cartedProducts1 = List.of(new CartDto.ItemQuantity(itemEntity1.getId(), 3));
        List<CartDto.ItemQuantity> cartedProducts2 = List.of(new CartDto.ItemQuantity(itemEntity2.getId(), 3));
        CartDto cartDto1 = new CartDto(null, cartedProducts1);
        CartDto cartDto2 = new CartDto(null, cartedProducts2);
        List<CartDto> expectedCarts = List.of(cartDto1, cartDto2);
        // when
        cartService.addCart(cartDto1);
        cartService.addCart(cartDto2);
        List<CartDto> carts = cartService.getCarts();
        // then
        assertNotNull(carts);
        assertFalse(carts.isEmpty());
        assertEquals(expectedCarts.size(), carts.size());
    }

    @Test
    public void addCartMultipleTimesTest() {
        // given
        ItemEntity itemEntity = itemService.addItemReturnEntity(itemDto1);
        List<CartDto.ItemQuantity> cartItems = List.of(new CartDto.ItemQuantity(itemEntity.getId(), 3));
        CartDto cartDTO = new CartDto(null, cartItems);
        List<CartDto> expectedCarts = List.of(cartDTO, cartDTO);
        // when
        cartService.addCart(cartDTO);
        cartService.addCart(cartDTO);
        List<CartDto> carts = cartService.getCarts();
        // then
        assertNotNull(carts);
        assertFalse(carts.isEmpty());
        assertEquals(expectedCarts.size(), carts.size());
    }

    @Test
    public void cartWithNotExistingItemsShouldThrowExceptionTest() {
        // given
        List<CartDto.ItemQuantity> cartedItems = List.of(new CartDto.ItemQuantity(123, 10));
        CartDto cartDTO = new CartDto(null, cartedItems);
        // then
        assertThrows(EntityNotFoundException.class, () -> cartService.addCart(cartDTO));
    }

    @Test
    public void getNotExistingCartShouldThrowException() {
        assertThrows(EntityNotFoundException.class, () -> cartService.getCart(123));
    }

}
