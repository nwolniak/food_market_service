package com.foodmarket.repository;

import com.foodmarket.configuration.TestConfiguration;
import com.foodmarket.model.entity.CartEntity;
import com.foodmarket.model.entity.CartItemEntity;
import com.foodmarket.model.entity.ItemEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.apache.commons.collections4.CollectionUtils.isEqualCollection;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = TestConfiguration.class)
public class CartRepositoryTest {

    @Autowired
    private CartRepository cartRepository;

    private final CartEntity cartEntity1 = new CartEntity();
    private final CartEntity cartEntity2 = new CartEntity();
    private final CartEntity cartEntity3 = new CartEntity();

    private final ItemEntity itemEntity1 = new ItemEntity("Bananas", "Fruit", "Bunch", 2.99, "Fresh, ripe bananas");
    private final ItemEntity itemEntity2 = new ItemEntity("Apples", "Fruit", "Bag", 4.99, "Juicy, crunchy apples");
    private final ItemEntity itemEntity3 = new ItemEntity("Oranges", "Fruit", "Bag", 3.99, "Sweet and tangy oranges");


    @BeforeEach
    public void init() {
        CartItemEntity cartItemEntity1 = new CartItemEntity(cartEntity1, itemEntity1, 5);
        CartItemEntity cartItemEntity2 = new CartItemEntity(cartEntity1, itemEntity2, 5);
        CartItemEntity cartItemEntity3 = new CartItemEntity(cartEntity1, itemEntity3, 5);
        cartEntity1.setCartItems(new HashSet<>(Set.of(cartItemEntity1)));
        cartEntity2.setCartItems(new HashSet<>(Set.of(cartItemEntity1, cartItemEntity2)));
        cartEntity3.setCartItems(new HashSet<>(Set.of(cartItemEntity1, cartItemEntity2, cartItemEntity3)));
    }

    @Test
    public void saveCartTest() {
        // when
        CartEntity saved = cartRepository.save(cartEntity1);
        // then
        assertNotNull(saved);
        assertEquals(cartEntity1, saved);
    }

    @Test
    public void saveMultipleCartsTest() {
        // given
        List<CartEntity> expectedCarts = List.of(cartEntity1, cartEntity2, cartEntity3);
        // when
        cartRepository.save(cartEntity1);
        cartRepository.save(cartEntity2);
        cartRepository.save(cartEntity3);
        List<CartEntity> allCarts = cartRepository.findAll();
        // then
        assertNotNull(allCarts);
        assertFalse(allCarts.isEmpty());
        assertTrue(isEqualCollection(expectedCarts, allCarts));
    }

    @Test
    public void saveCartMultipleTimesTest() {
        // given
        List<CartEntity> expectedCarts = List.of(cartEntity1);
        // when
        cartRepository.save(cartEntity1);
        cartRepository.save(cartEntity1);
        List<CartEntity> allCarts = cartRepository.findAll();
        // then
        assertNotNull(allCarts);
        assertFalse(allCarts.isEmpty());
        assertTrue(isEqualCollection(expectedCarts, allCarts));
    }

    @Test
    public void findByIdTest() {
        // when
        CartEntity saved = cartRepository.save(cartEntity1);
        Optional<CartEntity> cartEntityOptional = cartRepository.findById(saved.getId());
        // then
        assertTrue(cartEntityOptional.isPresent());
        CartEntity cartEntity = cartEntityOptional.get();
        assertEquals(cartEntity1, cartEntity);
        assertTrue(isEqualCollection(cartEntity1.getCartItems(), cartEntity.getCartItems()));
    }

    @Test
    public void deleteByIdTest() {
        // given
        CartEntity saved = cartRepository.save(cartEntity1);
        // when
        cartRepository.deleteById(saved.getId());
        Optional<CartEntity> deleted = cartRepository.findById(saved.getId());
        // then
        assertTrue(deleted.isEmpty());
    }

}
