package com.foodmarket.service;

import com.foodmarket.configuration.TestConfiguration;
import com.foodmarket.exceptions.EntityNotFoundException;
import com.foodmarket.model.dto.CartDTO;
import com.foodmarket.model.dto.ItemDTO;
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

    private final ItemDTO itemDTO1 = new ItemDTO(null, "Bananas", "Fruit", "Bunch", 2.99, "Fresh, ripe bananas");
    private final ItemDTO itemDTO2 = new ItemDTO(null, "Apples", "Fruit", "Bag", 4.99, "Juicy, crunchy apples");
    private final ItemDTO itemDTO3 = new ItemDTO(null, "Oranges", "Fruit", "Bag", 3.99, "Sweet and tangy oranges");

    @Test
    public void addCartTest() {
        // given
        ItemEntity itemEntity = itemService.addItemReturnEntity(itemDTO1);
        List<CartDTO.ItemQuantity> cartItems = List.of(new CartDTO.ItemQuantity(itemEntity.getId(), 3));
        CartDTO cartDTO = new CartDTO(null, cartItems);
        // when
        CartDTO saved = cartService.addCart(cartDTO);
        // then
        assertNotNull(saved);
        assertEquals(cartDTO.cartItems(), saved.cartItems());
    }

    @Test
    public void addMultipleCartsTest() {
        // given
        ItemEntity itemEntity1 = itemService.addItemReturnEntity(itemDTO1);
        ItemEntity itemEntity2 = itemService.addItemReturnEntity(itemDTO2);
        List<CartDTO.ItemQuantity> cartedProducts1 = List.of(new CartDTO.ItemQuantity(itemEntity1.getId(), 3));
        List<CartDTO.ItemQuantity> cartedProducts2 = List.of(new CartDTO.ItemQuantity(itemEntity2.getId(), 3));
        CartDTO cartDTO1 = new CartDTO(null, cartedProducts1);
        CartDTO cartDTO2 = new CartDTO(null, cartedProducts2);
        List<CartDTO> expectedCarts = List.of(cartDTO1, cartDTO2);
        // when
        cartService.addCart(cartDTO1);
        cartService.addCart(cartDTO2);
        List<CartDTO> carts = cartService.getCarts();
        // then
        assertNotNull(carts);
        assertFalse(carts.isEmpty());
        assertEquals(expectedCarts.size(), carts.size());
    }

    @Test
    public void addCartMultipleTimesTest() {
        // given
        ItemEntity itemEntity = itemService.addItemReturnEntity(itemDTO1);
        List<CartDTO.ItemQuantity> cartItems = List.of(new CartDTO.ItemQuantity(itemEntity.getId(), 3));
        CartDTO cartDTO = new CartDTO(null, cartItems);
        List<CartDTO> expectedCarts = List.of(cartDTO, cartDTO);
        // when
        cartService.addCart(cartDTO);
        cartService.addCart(cartDTO);
        List<CartDTO> carts = cartService.getCarts();
        // then
        assertNotNull(carts);
        assertFalse(carts.isEmpty());
        assertEquals(expectedCarts.size(), carts.size());
    }

    @Test
    public void cartWithNotExistingItemsShouldThrowExceptionTest() {
        // given
        List<CartDTO.ItemQuantity> cartedItems = List.of(new CartDTO.ItemQuantity(123, 10));
        CartDTO cartDTO = new CartDTO(null, cartedItems);
        // then
        assertThrows(EntityNotFoundException.class, () -> cartService.addCart(cartDTO));
    }

    @Test
    public void getNotExistingCartShouldThrowException() {
        assertThrows(EntityNotFoundException.class, () -> cartService.getCart(123));
    }

}
