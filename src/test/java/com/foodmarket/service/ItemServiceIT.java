package com.foodmarket.service;

import com.foodmarket.configuration.TestConfiguration;
import com.foodmarket.exceptions.EntityNotFoundException;
import com.foodmarket.model.dto.ItemDTO;
import com.foodmarket.model.entity.ItemEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.apache.commons.collections4.CollectionUtils.isEqualCollection;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = TestConfiguration.class)
public class ItemServiceIT {

    @Autowired
    private ItemService itemService;

    private final ItemDTO itemDTO1 = new ItemDTO(1L, "Bananas", "Fruit", "Bunch", 2.99, "Fresh, ripe bananas");
    private final ItemDTO itemDTO2 = new ItemDTO(2L, "Apples", "Fruit", "Bag", 4.99, "Juicy, crunchy apples");
    private final ItemDTO itemDTO3 = new ItemDTO(3L, "Oranges", "Fruit", "Bag", 3.99, "Sweet and tangy oranges");


    @Test
    public void addItemTest() {
        // when
        ItemDTO saved = itemService.addItem(itemDTO1);
        // then
        assertNotNull(saved);
        assertEquals(itemDTO1.name(), saved.name());
    }

    @Test
    public void getItemTest() {
        // given
        ItemEntity itemEntity = itemService.addItemReturnEntity(itemDTO1);
        // when
        ItemDTO product = itemService.getItem(itemEntity.getId());
        // then
        assertNotNull(product);
        assertEquals(itemDTO1, product);
    }

    @Test
    public void addMultipleItemsTest() {
        // given
        List<ItemDTO> expectedItems = List.of(itemDTO1, itemDTO2, itemDTO3);
        // when
        itemService.addItem(itemDTO1);
        itemService.addItem(itemDTO2);
        itemService.addItem(itemDTO3);
        List<ItemDTO> allItems = itemService.getAllItems();
        // then
        assertNotNull(allItems);
        assertFalse(allItems.isEmpty());
        assertTrue(isEqualCollection(expectedItems, allItems));
    }

    @Test
    public void addItemMultipleTimesTest() {
        // given
        List<ItemDTO> expectedItems = List.of(itemDTO1);
        // when
        itemService.addItem(itemDTO1);
        itemService.addItem(itemDTO1);
        List<ItemDTO> allItems = itemService.getAllItems();
        // then
        assertNotNull(allItems);
        assertFalse(allItems.isEmpty());
        assertTrue(isEqualCollection(expectedItems, allItems));
    }

    @Test
    public void getNotExistingItemShouldThrowException() {
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> itemService.getItem(123));
    }

    @Test
    public void getNotExistingItemEntityShouldThrowException() {
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> itemService.getItemEntity(123));
    }

}
